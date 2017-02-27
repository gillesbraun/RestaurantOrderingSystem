package lu.btsi.bragi.ros.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.pojos.Table;

import java.io.IOException;

/**
 * Created by gillesbraun on 27/02/2017.
 */
public class TablesStage extends Stage {
    private Client client;

    @FXML
    private TextField tableID, tablePlaces;

    @FXML
    private ListView<Table> listTables;

    public TablesStage(Client client) throws IOException {
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TablesStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        initModality(Modality.APPLICATION_MODAL);

        setTitle("Waiters");
        setScene(new Scene(root, 600, 300));
        show();

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

    }

    public void saveButtonPressed(ActionEvent actionEvent) {

    }

    public void createButtonPressed(ActionEvent actionEvent) {

    }

}
