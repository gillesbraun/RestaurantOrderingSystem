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
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.message.MessageType;
import lu.btsi.bragi.ros.models.pojos.Waiter;

import java.io.IOException;
import java.util.List;

/**
 * Created by gillesbraun on 17/02/2017.
 */
public class WaitersFrame extends Stage {
    private Client client;

    @FXML
    private ListView<Waiter> listWaiters;

    @FXML
    private Button deleteButton, buttonUpdate, buttonCreate;

    @FXML
    private Label waiterID;

    @FXML
    private TextField waiterName;

    public WaitersFrame(Client client) throws IOException {
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WaitersFrame.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        initModality(Modality.APPLICATION_MODAL);

        setTitle("Waiters");
        setScene(new Scene(root, 600, 300));
        show();
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
            } else {
                buttonUpdate.setDisable(true);
                waiterID.setText("");
                waiterName.setText("");
            }
        });

        if(client == null) {
            close();
        }
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
        if(listWaiters.getSelectionModel().getSelectedItems().size() == 1) {
            Waiter waiter = listWaiters.getSelectionModel().getSelectedItem();
            waiter.setName(waiterName.getText());
            Message<Waiter> message = new Message<>(MessageType.Update, waiter);
            client.send(message.toString());
            loadWaiters();
        }
    }

    public void createButtonPressed(ActionEvent actionEvent) {
        Waiter waiter = new Waiter();
        waiter.setName(waiterName.getText());
        Message<Waiter> message = new Message<Waiter>(MessageType.Create, waiter);
        client.send(message.toString());
        loadWaiters();
    }
}
