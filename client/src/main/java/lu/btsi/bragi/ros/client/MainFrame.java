package lu.btsi.bragi.ros.client;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.client.connection.ConnectionManager;
import lu.btsi.bragi.ros.client.connection.UICallback;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.pojos.Order;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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
    private Stage parent;

    private Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
        client.sendWithAction(new MessageGet<>(Order.class), m -> {
            try {
                List<Order> payload = new Message<Order>(m).getPayload();
                ordersPane.getChildren().clear();
                payload.stream()
                    .sorted((o1, o2) -> o2.getCreatedAt().toLocalDateTime().compareTo(o1.getCreatedAt().toLocalDateTime()))
                    .filter(o -> o.getCreatedAt().toLocalDateTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth())
                    .forEach(order -> {
                    ordersPane.getChildren().add(new OrderPanel(client, order));
                });
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }));

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainFrame.fxml"));
        loader.setController(this);
        Parent root = loader.load();

        primaryStage.setOnCloseRequest(onClose);
        primaryStage.setTitle("ROS Client");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
        this.parent = primaryStage;

        connectionManager.newClient();
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    @Override
    public void connectionOpened(String message, Client client) {
        this.client = client;
        displayMessage(message);
        timeline.play();
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
            if(client != null) {
                WaitersStage waitersStage = new WaitersStage(client);
                waitersStage.initOwner(parent);
                waitersStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuItemTablesPressed(ActionEvent actionEvent) {
        try {
            if(client != null) {
                TablesStage tablesStage = new TablesStage(client);
                tablesStage.initOwner(parent);
                tablesStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuItemProductsPressed(ActionEvent event) {
        if(client != null) {
            try {
                ProductsStage productsStage = new ProductsStage(client);
                productsStage.initOwner(parent);
                productsStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void menuItemLanguagesPressed(ActionEvent evt) {
        if(client != null) {
            try {
                LanguagesStage languagesStage = new LanguagesStage(client);
                languagesStage.initOwner(parent);
                languagesStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void menuItemProductCategoriesPressed(ActionEvent evt) {
        if(client != null) {
            try {
                ProductCategoriesStage productCategoriesStage = new ProductCategoriesStage(client);
                productCategoriesStage.initOwner(parent);
                productCategoriesStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void menuItemAllergensPressed(ActionEvent evt) {
        if(client != null) {
            try {
                AllergensStage allergensStage = new AllergensStage(client);
                allergensStage.initOwner(parent);
                allergensStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void menuItemLocationsPressed(ActionEvent evt) {
        if(client != null) {
            try {
                LocationsStage locationsStage = new LocationsStage(client);
                locationsStage.initOwner(parent);
                locationsStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
