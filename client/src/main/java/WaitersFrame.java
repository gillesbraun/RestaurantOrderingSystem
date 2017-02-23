import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageType;
import lu.btsi.bragi.ros.models.pojos.Waiter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gillesbraun on 17/02/2017.
 */
public class WaitersFrame extends Stage {
    private Client client;

    @FXML
    private ListView listWaiters;

    public WaitersFrame(Client client) throws IOException {
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WaitersFrame.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        initModality(Modality.APPLICATION_MODAL);

        setTitle("Waiters");
        setScene(new Scene(root, 600, 300));
        show();

        if(client == null) {
            close();
        }
        loadWaiters();
    }

    private void loadWaiters() {
        client.sendWithAction(new Message<>(MessageType.Get, Waiter.class), (String m) -> {
            Message<Waiter> mess = new Message<>(m, Waiter.class);
            List<Waiter> waiters = mess.getPayload();

            List<String> waiterNames = waiters.stream().map(Waiter::getName).collect(Collectors.toList());
            listWaiters.setItems(FXCollections.observableList(waiterNames));
        });
    }


}
