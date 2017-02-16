import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lu.btsi.bragi.ros.models.pojo.Table;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageType;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by gillesbraun on 14/02/2017.
 */
public class MainFrame extends Application implements Callback {
    private Client client;
    private boolean connectionOpen = false;

    private TextField inputText = new TextField(new Message(MessageType.Get, Table.class).toString());
    private TextArea textArea = new TextArea("");

    private EventHandler<ActionEvent> submitPressed = event -> {
        String input = inputText.getText();
        inputText.setText("");
        inputText.requestFocus();
        send(input);
    };

    private EventHandler<WindowEvent> onClose = event -> {
        if(client != null) {
            client.close();
        }
        System.exit(0);
    };

    private void send(String input) {
        try {
            client.send(input);
        } catch (WebsocketNotConnectedException ex) {
            connectionClosed("Server ded");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();

        VBox vbox = new VBox();
            inputText.setFont(Font.font("Courier New"));
            Button submitInput = new Button();
            submitInput.setText("Send message to server");
            textArea.setFont(Font.font("Courier New"));
            textArea.setPrefRowCount(10);

        submitInput.setOnAction(submitPressed);
        inputText.setOnAction(submitPressed);

        vbox.getChildren().addAll(inputText, submitInput, textArea);
        root.setCenter(vbox);
        primaryStage.setOnCloseRequest(onClose);
        primaryStage.setTitle("ROS Client");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();

        newClient();
    }

    @Override
    public void handleCallback(String message) {
        try {
            Message decoded = Message.fromString(message);
            System.out.println(decoded);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        textArea.appendText(message + "\n");
    }

    @Override
    public void connectionClosed(String message) {

        Platform.runLater(() -> {
            textArea.appendText(message+" Retrying in 2 seconds...\n");
            new Thread(new javafx.concurrent.Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    newClient();
                    return null;
                }
            }).start();
            /*
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("Connection closed");
            error.setContentText(message + "\nAttempting reconnect.");
            error.showAndWait();*/
        });
    }

    @Override
    public void connectionOpened(String message) {
        Platform.runLater(() -> {
            textArea.appendText(message + "\n");
        });
    }

    private void newClient() {
        this.client = null;
        Client client = null;
        try {
            client = new Client(new URI("ws://ws222-2.local:8887"));
            client.setCallback(this);
            client.connect();
            this.client = client;
        } catch (URISyntaxException e) { }
    }
}
