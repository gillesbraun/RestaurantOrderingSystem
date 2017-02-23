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
import lu.btsi.bragi.ros.models.message.Message;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by gillesbraun on 14/02/2017.
 */
public class MainFrame extends Application implements Callback {
    private Client client;

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

    private ServiceListener mdnsListener = new ServiceListener() {
        @Override
        public void serviceAdded(ServiceEvent event) {
            if(event.getInfo().getSubtype().equals("roswebsocket")){
                client = null;
                try {
                    client = new Client(new URI("ws://" + event.getDNS().getName() + ":8887"));
                    client.setMainCallback(MainFrame.this);
                    client.connect();
                } catch (URISyntaxException e) {
                }
            } else {
                statusTextArea.appendText("Found other webservice ("+event.getInfo().getSubtype()+"), ignoring");
            }
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
            System.out.println("Service removed: " + event.getInfo());
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            System.out.println("Resolved: "+ event.getInfo() );
        }
    };

    private void newClient() {
        try {
            JmDNS jmDNS = JmDNS.create(InetAddress.getLocalHost());
            jmDNS.addServiceListener("_ws._tcp.local.", mdnsListener);
            statusTextArea.appendText("Looking for ROS Server...\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(String input) {
        try {
            if(client != null) {
                client.send(input);
            }
        } catch (WebsocketNotConnectedException ex) {
            connectionClosed("Server ded");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFrame.fxml"));
        loader.setController(this);
        Parent root = loader.load();

        primaryStage.setOnCloseRequest(onClose);
        primaryStage.setTitle("ROS Client");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();

        newClient();
    }

    @Override
    public void handleCallback(String message) {
        System.out.println(message);
        /*try {
            Message decoded = Message.fromString(message);
            System.out.println(decoded);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
        statusTextArea.appendText(message + "\n");
    }

    @Override
    public void connectionClosed(String message) {

        Platform.runLater(() -> {
            statusTextArea.appendText(message+" Retrying in 2 seconds...\n");
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
            statusTextArea.appendText(message + "\n");
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
