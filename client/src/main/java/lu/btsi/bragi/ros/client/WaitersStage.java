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
import lu.btsi.bragi.ros.models.pojos.Waiter;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by gillesbraun on 17/02/2017.
 */
public class WaitersStage extends Stage {
    private Client client;

    @FXML
    private ListView<Waiter> listWaiters;

    @FXML
    private Button deleteButton, buttonUpdate, buttonCreate, buttonRefresh;

    @FXML
    private Label waiterID, labelCreated, labelUpdated;

    @FXML
    private TextField waiterName;

    public WaitersStage(Client client) throws IOException {
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/WaitersStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        initModality(Modality.APPLICATION_MODAL);

        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        buttonRefresh.setGraphic(fa.create(FontAwesome.Glyph.REFRESH));

        setTitle("Waiters");
        setScene(new Scene(root, 600, 300));
        listWaiters.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listWaiters.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(listWaiters.getSelectionModel().getSelectedItems().size() > 0) {
                deleteButton.setDisable(false);
            } else {
                deleteButton.setDisable(true);
            }
            if(listWaiters.getSelectionModel().getSelectedItems().size() == 1) {
                buttonUpdate.setDisable(false);
                Waiter waiter = listWaiters.getSelectionModel().getSelectedItem();
                waiterID.setText(waiter.getId() + "");
                waiterName.setText(waiter.getName());
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy H:mm");
                labelCreated.setText("Created: "+waiter.getCreatedAt().toLocalDateTime().format(dateFormat));
                labelUpdated.setText("Updated: "+waiter.getUpdatedAt().toLocalDateTime().format(dateFormat));
            } else {
                buttonUpdate.setDisable(true);
                waiterID.setText("");
                waiterName.setText("");
                labelCreated.setText("");
                labelUpdated.setText("");
            }
        });

        loadWaiters();
    }

    private void loadWaiters() {
        client.sendWithAction(new MessageGet<>(Waiter.class), (String m) -> {
            Message<Waiter> mess = new Message<>(m, Waiter.class);
            List<Waiter> waiters = mess.getPayload();
            Platform.runLater(() -> {
                listWaiters.setItems(FXCollections.observableList(waiters));
            });
        });
    }

    public void deleteButtonPressed(ActionEvent actionEvent) {
        ObservableList<Waiter> selectedItems = listWaiters.getSelectionModel().getSelectedItems();
        Message<Waiter> waiterMessage = new Message<>(MessageType.Delete, selectedItems, Waiter.class);
        client.send(waiterMessage.toString());
        loadWaiters();
    }

    public void saveButtonPressed(ActionEvent actionEvent) {
        if(listWaiters.getSelectionModel().getSelectedItems().size() == 1 && waiterName.getText().trim().length() > 0) {
            Waiter waiter = listWaiters.getSelectionModel().getSelectedItem();
            waiter.setName(waiterName.getText());
            Message<Waiter> message = new Message<>(MessageType.Update, waiter);
            client.send(message.toString());
            loadWaiters();
        }
    }

    public void createButtonPressed(ActionEvent actionEvent) {
        if(waiterName.getText().trim().length() == 0)
            return;
        Waiter waiter = new Waiter();
        waiter.setName(waiterName.getText());
        Message<Waiter> message = new Message<>(MessageType.Create, waiter);
        client.send(message.toString());
        loadWaiters();
    }

    public void buttonRefreshPressed(ActionEvent event) {
        loadWaiters();
    }
}
