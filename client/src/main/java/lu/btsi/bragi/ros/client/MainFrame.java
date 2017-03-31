package lu.btsi.bragi.ros.client;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Shadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import lu.btsi.bragi.ros.client.connection.*;
import lu.btsi.bragi.ros.client.editViews.*;
import lu.btsi.bragi.ros.client.settings.Config;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by gillesbraun on 14/02/2017.
 */
public class MainFrame extends Application implements UICallback, ConnectionCallback, BroadcastCallback, HyperlinkOpener {
    @FXML
    private TextArea statusTextArea;

    @FXML
    private Menu menuEdit, menuView;

    @FXML
    private VBox vboxOrdersContainer, vboxInvoicesContainer;

    @FXML
    private Label labelOrdersTitle;

    @FXML
    private MenuItem menuItemRefresh, menuItemQRCode, menuItemSettings, menuItemQuit;

    @FXML private SplitPane splitPaneContent, splitPaneMessages;

    private ContainerPaneOrders ordersContainerPane;
    private ContainerPaneInvoices invoicesContainerPane;

    private EventHandler<WindowEvent> onClose = event -> {
        ConnectionManager.getInstance().close();
        System.exit(0);
    };

    private Stage parent;

    private EventHandler<? super KeyEvent> keyListener = event -> {
        KeyCode code = event.getCode();
        if(code.equals(KeyCode.F5)) {
            loadContent();
        }
    };

    private boolean animationDisplayTextAreaPlayed = false;
    private Animation animationDisplayTextArea;

    private boolean animationHideTextAreaPlayed = false;
    private Animation animationHideTextArea;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Config.init();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainFrame.fxml"));
        loader.setController(this);
        Parent root = loader.load();

        primaryStage.setOnCloseRequest(onClose);
        primaryStage.setTitle("ROS Client");
        Scene scene = new Scene(root, 1000, 650);
        scene.setOnKeyPressed(keyListener);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.parent = primaryStage;

        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        menuItemRefresh.setGraphic(fa.create(FontAwesome.Glyph.REFRESH));
        menuItemRefresh.getGraphic().setEffect(new Shadow(1, Color.BLACK));
        menuItemQRCode.setGraphic(fa.create(FontAwesome.Glyph.ANDROID));
        menuItemQRCode.getGraphic().setEffect(new Shadow(1, Color.BLACK));
        menuItemSettings.setGraphic(fa.create(FontAwesome.Glyph.GEARS));
        menuItemSettings.getGraphic().setEffect(new Shadow(1, Color.BLACK));
        menuItemQuit.setGraphic(fa.create(FontAwesome.Glyph.SIGN_OUT));
        menuItemQuit.getGraphic().setEffect(new Shadow(1, Color.BLACK));

        ordersContainerPane = new ContainerPaneOrders();
        invoicesContainerPane = new ContainerPaneInvoices();
        invoicesContainerPane.setOwner(parent);
        vboxOrdersContainer.getChildren().setAll(ordersContainerPane);
        vboxInvoicesContainer.getChildren().setAll(invoicesContainerPane);

        animationHideTextArea = new SplitPaneAnimator(splitPaneMessages, 0.7, 1.0);
        animationDisplayTextArea = new SplitPaneAnimator(splitPaneMessages, 1.0, 0.7);
        animationHideTextArea.setDelay(Duration.millis(2000));

        setContentDisabled(true);
        ConnectionManager.init(this);
        ConnectionManager.getInstance().addBroadcastCallback(this);
        ConnectionManager.getInstance().addConnectionCallback(this);
    }

    public void showQR(ActionEvent evt) throws UnknownHostException, WriterException {
        if (!ConnectionManager.isConnected())
            return;

        String connectingTo = ConnectionManager.getInstance().getRemoteIPAdressForQR() + ":8887";
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix encode = qrCodeWriter.encode(connectingTo, BarcodeFormat.QR_CODE, 500, 500);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(encode);

        Stage stage = new Stage();
        ImageView image = new ImageView();
        image.setImage(SwingFXUtils.toFXImage(bufferedImage, null));

        Pane pane = new Pane();
        pane.getChildren().add(image);
        pane.setCenterShape(true);

        Scene qrScene = new Scene(pane);
        stage.setResizable(false);
        stage.setTitle("Connect an Android Client");
        stage.setScene(qrScene);
        stage.initOwner(parent);
        stage.show();
    }

    @Override
    public void connectionOpened(String message, Client client) {
        displayMessage(message);
        loadContent();
        setContentDisabled(false);
    }

    private void setContentDisabled(boolean en) {
        menuEdit.setDisable(en);
        menuView.setDisable(en);
        menuItemQRCode.setDisable(en);
        splitPaneContent.setDisable(en);
    }

    public void menuItemRefreshPressed(ActionEvent event) {
        loadContent();
    }

    void loadContent() {
        ordersContainerPane.refreshOrders();
        invoicesContainerPane.refreshInvoices();
    }

    @Override
    public void displayMessage(String text) {
        Platform.runLater(() -> {
            DateTimeFormatter dateTimeFormatter = Config.getInstance().getDateTimeFormatter();
            LocalDateTime now = LocalDateTime.now();
            statusTextArea.appendText(String.format("[%s] %s\n", now.format(dateTimeFormatter), text));
        });
    }

    public void menuItemQuitPressed(ActionEvent actionEvent) {
        onClose.handle(null);
    }

    public void menuItemWaitersPressed(ActionEvent actionEvent) {
        try {
            WaitersStage waitersStage = new WaitersStage();
            waitersStage.initOwner(parent);
            waitersStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuItemTablesPressed(ActionEvent actionEvent) {
        try {
            TablesStage tablesStage = new TablesStage();
            tablesStage.initOwner(parent);
            tablesStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuItemProductsPressed(ActionEvent event) {
        try {
            ProductsStage productsStage = new ProductsStage();
            productsStage.initOwner(parent);
            productsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuItemLanguagesPressed(ActionEvent evt) {
        try {
            LanguagesStage languagesStage = new LanguagesStage();
            languagesStage.initOwner(parent);
            languagesStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuItemProductCategoriesPressed(ActionEvent evt) {
        try {
            ProductCategoriesStage productCategoriesStage = new ProductCategoriesStage();
            productCategoriesStage.initOwner(parent);
            productCategoriesStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuItemAllergensPressed(ActionEvent evt) {
        try {
            AllergensStage allergensStage = new AllergensStage();
            allergensStage.initOwner(parent);
            allergensStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuItemLocationsPressed(ActionEvent evt) {
        try {
            LocationsStage locationsStage = new LocationsStage();
            locationsStage.initOwner(parent);
            locationsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuItemSettingsPressed(ActionEvent event) {
        try {
            SettingsStage settingsStage = new SettingsStage();
            settingsStage.initOwner(parent);
            settingsStage.setMainFrame(this);
            settingsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuItemAboutPressed(ActionEvent event) {
        try {
            AboutStage aboutStage = new AboutStage();
            aboutStage.initOwner(parent);
            aboutStage.setHyperlinkOpener(this);
            aboutStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuItemArchiveOrders(ActionEvent evt) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ArchiveOrdersStage.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(parent);
            stage.setTitle("Orders Archive");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuItemArchiveInvoices(ActionEvent evt) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ArchiveInvoicesStage.fxml"));
            Parent root = loader.load();
            ((ArchiveInvoicesController)loader.getController()).setOwner(parent);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(parent);
            stage.setTitle("Invoices Archive");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionClosed(String reason) {
        setContentDisabled(true);
        showStatusTextArea();
    }

    @Override
    public void connectionOpened(String message) {
        setContentDisabled(false);
        hideStatusTextArea();
    }

    public void hideStatusTextArea() {
        if(!animationHideTextAreaPlayed) {
            animationHideTextArea.play();
            animationHideTextAreaPlayed = true;
            animationDisplayTextAreaPlayed = false;
        }
    }

    public void showStatusTextArea() {
        if(!animationDisplayTextAreaPlayed) {
            animationDisplayTextArea.play();
            animationDisplayTextAreaPlayed = true;
            animationHideTextAreaPlayed = false;
        }
    }

    @Override
    public void handleBroadCast() {
        loadContent();
    }

    @Override
    public void openLink(String url) {
        getHostServices().showDocument(url);
    }
}
