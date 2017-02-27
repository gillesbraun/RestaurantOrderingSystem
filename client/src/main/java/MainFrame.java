import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * Created by gillesbraun on 14/02/2017.
 */
public class MainFrame extends Application implements UICallback {
    private Client client;

    private ConnectionManager connectionManager = new ConnectionManager(this);

    @FXML
    private TextArea statusTextArea;

    @FXML
    private VBox ordersPane, invoicesPane;

    private EventHandler<WindowEvent> onClose = event -> {
        if(client != null) {
            client.close();
        }
        System.exit(0);
    };


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFrame.fxml"));
        loader.setController(this);
        Parent root = loader.load();

        primaryStage.setOnCloseRequest(onClose);
        primaryStage.setTitle("ROS Client");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();

        connectionManager.newClient();
    }

    @Override
    public void connectionOpened(String message, Client client) {
        this.client = client;
        displayMessage(message);
    }

    @Override
    public void displayMessage(String text) {
        Platform.runLater(() -> {
            statusTextArea.appendText(text + "\n");
        });
    }

    public void menuItemQuitPressed(ActionEvent actionEvent) {
        if(client != null) {
            client.close();
        }
        System.exit(0);
    }

    public void menuItemWaitersPressed(ActionEvent actionEvent) {
        try {
            WaitersFrame wf = new WaitersFrame(client);
            wf.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
