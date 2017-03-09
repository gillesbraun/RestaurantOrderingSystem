package lu.btsi.bragi.ros.client.editViews;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.message.MessageType;
import lu.btsi.bragi.ros.models.pojos.Table;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by gillesbraun on 27/02/2017.
 */
public class TablesStage extends Stage {
    private Client client;

    @FXML private ListView<Table> listTables;

    @FXML private Button buttonDelete, buttonCreate, buttonRefresh;

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
                Table table = listTables.getSelectionModel().getSelectedItem();
                tableID.setText(table.getId()+"");
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy H:mm");
                labelCreated.setText("Created: "+table.getCreatedAt().toLocalDateTime().format(dateFormat));
                labelUpdated.setText("Updated: "+table.getUpdatedAt().toLocalDateTime().format(dateFormat));
            } else {
                buttonDelete.setDisable(true);
                tableID.setText("");
                labelCreated.setText("");
                labelUpdated.setText("");
            }
        });

        loadData();
    }

    private void loadData() {
        client.sendWithAction(new MessageGet<>(Table.class), (text) -> {
            Message<Table> tables = null;
            try {
                tables = new Message<>(text);
            } catch (MessageException e) {
                ExceptionDialog d = new ExceptionDialog(e);
                d.show();
            }

            ObservableList<Table> list = FXCollections.observableList(tables.getPayload());
            Platform.runLater(() -> {
                listTables.setItems(list);
            });
        });
    }

    public void deleteButtonPressed(ActionEvent actionEvent) {
        Table selectedItem = listTables.getSelectionModel().getSelectedItem();
        Message<Table> update = new Message<>(MessageType.Delete, selectedItem, Table.class);
        client.send(update.toString());
        loadData();
    }

    public void buttonCreatePressed(ActionEvent actionEvent) {
        Table table = new Table();
        table.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        Message<Table> message = new Message<>(MessageType.Create, table, Table.class);
        client.send(message.toString());
        loadData();
    }

    public void buttonRefreshPressed(ActionEvent event) {
        loadData();
    }
}
