package lu.btsi.bragi.ros.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Shadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.client.connection.ConnectionManager;
import lu.btsi.bragi.ros.client.connection.UICallback;
import lu.btsi.bragi.ros.client.editViews.*;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

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
    private Menu menuEdit;

    @FXML
    private VBox panelOrdersContainer, invoicesContainer;

    @FXML private Label labelOrdersTitle;

    @FXML private MenuItem menuItemRefresh;

    private OrdersPanel ordersPane;
    private InvoicesContainerPane invoicesContainerPane;

    private EventHandler<WindowEvent> onClose = event -> {
        if(client != null) {
            client.close();
        }
        System.exit(0);
    };
    private Stage parent;


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainFrame.fxml"));
        loader.setController(this);
        Parent root = loader.load();

        primaryStage.setOnCloseRequest(onClose);
        primaryStage.setTitle("ROS Client");
        Scene scene = new Scene(root, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.parent = primaryStage;

        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        menuItemRefresh.setGraphic(fa.create(FontAwesome.Glyph.REFRESH));
        menuItemRefresh.getGraphic().setEffect(new Shadow(1, Color.BLACK));

        connectionManager.newClient();
    }

    @Override
    public void connectionOpened(String message, Client client) {
        this.client = client;
        displayMessage(message);
        loadContent();
    }

    public void menuItemRefreshPressed(ActionEvent event) {
        loadContent();
    }

    private void loadContent() {
        ordersPane = new OrdersPanel(client);
        invoicesContainerPane = new InvoicesContainerPane(client);
        Platform.runLater(() -> {
            menuEdit.setDisable(false);
            panelOrdersContainer.getChildren().setAll(ordersPane);
            invoicesContainer.getChildren().setAll(invoicesContainerPane);
        });
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
