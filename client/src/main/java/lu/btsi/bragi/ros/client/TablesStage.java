package lu.btsi.bragi.ros.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.message.MessageType;
import lu.btsi.bragi.ros.models.pojos.Table;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.jooq.types.UShort;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Created by gillesbraun on 27/02/2017.
 */
public class TablesStage extends Stage {
    private Client client;

    @FXML private TextField tablePlaces;

    @FXML private ListView<Table> listTables;

    @FXML private Button buttonDelete, buttonUpdate, buttonCreate, buttonRefresh;

    @FXML private Label tableID, labelUpdated, labelCreated;

    public TablesStage(Client client) throws IOException {
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TablesStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        initModality(Modality.APPLICATION_MODAL);

        setTitle("Tables");
        setScene(new Scene(root, 600, 300));

        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        buttonRefresh.setGraphic(fa.create(FontAwesome.Glyph.REFRESH));
        buttonDelete.setGraphic(fa.create(FontAwesome.Glyph.TRASH));
        buttonCreate.setGraphic(fa.create(FontAwesome.Glyph.PLUS_CIRCLE));

        listTables.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(listTables.getSelectionModel().getSelectedItems().size() == 1) {
                buttonDelete.setDisable(false);
                buttonUpdate.setDisable(false);
                Table table = listTables.getSelectionModel().getSelectedItem();
                tableID.setText(table.getId()+"");
                tablePlaces.setText(table.getPlaces()+"");
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy H:mm");
                labelCreated.setText("Created: "+table.getCreatedAt().toLocalDateTime().format(dateFormat));
                labelUpdated.setText("Updated: "+table.getUpdatedAt().toLocalDateTime().format(dateFormat));
            } else {
                buttonDelete.setDisable(true);
                buttonUpdate.setDisable(true);
                tableID.setText("");
                tablePlaces.setText("");
                labelCreated.setText("");
                labelUpdated.setText("");
            }
        });

        loadData();
    }

    private void loadData() {
        client.sendWithAction(new MessageGet<>(Table.class), (text) -> {
            Message<Table> tables = new Message<>(text);

            ObservableList<Table> list = FXCollections.observableList(tables.getPayload());
            Platform.runLater(() -> {
                listTables.setItems(list);
            });
        });
    }

    public void deleteButtonPressed(ActionEvent actionEvent) {
        Table selectedItem = listTables.getSelectionModel().getSelectedItem();
        Message<Table> update = new Message<>(MessageType.Delete, selectedItem);
        client.send(update.toString());
        loadData();
    }

    public void updateButtonPressed(ActionEvent actionEvent) {
        if(tablePlaces.getText().trim().length() == 0)
            return;
        Table selectedItem = listTables.getSelectionModel().getSelectedItem();
        selectedItem.setPlaces(UShort.valueOf(tablePlaces.getText()));
        Message<Table> update = new Message<>(MessageType.Update, selectedItem);
        client.send(update.toString());
        loadData();
    }

    public void buttonCreatePressed(ActionEvent actionEvent) {
        TextInputDialog inputDialog = new TextInputDialog("");
        inputDialog.setTitle("Create a new Table");
        inputDialog.setHeaderText("Create a new Table");
        inputDialog.setContentText("Enter the number of seats at the table: ");
        Optional<String> enteredName = inputDialog.showAndWait();
        try {
            if(enteredName.isPresent()) {
                Table table = new Table();
                table.setPlaces(UShort.valueOf(enteredName.get()));
                Message<Table> message = new Message<>(MessageType.Create, table);
                client.send(message.toString());
                loadData();
            }
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occured");
            alert.setContentText("Please enter a number.");
            alert.show();
        }
    }

    public void buttonRefreshPressed(ActionEvent event) {
        loadData();
    }
}
