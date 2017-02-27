package lu.btsi.bragi.ros.client;

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
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.message.MessageType;
import lu.btsi.bragi.ros.models.pojos.Table;
import org.jooq.types.UShort;

import java.io.IOException;

/**
 * Created by gillesbraun on 27/02/2017.
 */
public class TablesStage extends Stage {
    private Client client;

    @FXML private TextField tablePlaces;

    @FXML private ListView<Table> listTables;

    @FXML private Button buttonDelete, buttonUpdate, buttonCreate;

    @FXML private Label tableID;

    public TablesStage(Client client) throws IOException {
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TablesStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        initModality(Modality.APPLICATION_MODAL);

        setTitle("Tables");
        setScene(new Scene(root, 600, 300));
        show();

        listTables.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(listTables.getSelectionModel().getSelectedItems().size() == 1) {
                buttonDelete.setDisable(false);
                buttonUpdate.setDisable(false);
                Table table = listTables.getSelectionModel().getSelectedItem();
                tableID.setText(table.getId()+"");
                tablePlaces.setText(table.getPlaces()+"");
            } else {
                buttonDelete.setDisable(true);
                buttonUpdate.setDisable(true);
                tableID.setText("");
                tablePlaces.setText("");
            }
        });

        loadData();
    }

    private void loadData() {
        client.sendWithAction(new MessageGet(Table.class), (text) -> {
            Message<Table> tables = new Message<>(text, Table.class);

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

    public void createButtonPressed(ActionEvent actionEvent) {
        if(tablePlaces.getText().trim().length() == 0)
            return;
        Table table = new Table();
        table.setPlaces(UShort.valueOf(tablePlaces.getText()));
        Message<Table> message = new Message(MessageType.Create, table);
        client.send(message.toString());
        loadData();
    }

}
