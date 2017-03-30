package lu.btsi.bragi.ros.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by gillesbraun on 21/03/2017.
 */
public class AboutStage extends Stage {
    private HyperlinkOpener hyperlinkOpener;
    @FXML private Label linkJmdns, linkControlsFX, linkApacheHTTPClient, linkZxing, linkThumbnailator,
            linkGson, linkJooq, linkStreamSupport, linkMysqlJdbc, linkGuice, linkWebSocket;

    public AboutStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AboutStage.fxml"));
        loader.setController(this);
        Parent root =  loader.load();
        setTitle("About ROS");

        setScene(new Scene(root));

        linkApacheHTTPClient.setOnMouseClicked(event -> {
            hyperlinkOpener.openLink("https://hc.apache.org/");
        });

        linkJmdns.setOnMouseClicked(event -> {
            hyperlinkOpener.openLink("https://github.com/jmdns/jmdns");
        });

        linkControlsFX.setOnMouseClicked(event -> {
            hyperlinkOpener.openLink("http://fxexperience.com/controlsfx/");
        });

        linkZxing.setOnMouseClicked(event -> {
            hyperlinkOpener.openLink("https://zxing.github.io/zxing/");
        });

        linkThumbnailator.setOnMouseClicked(event -> {
            hyperlinkOpener.openLink("https://github.com/coobird/thumbnailator");
        });

        linkGson.setOnMouseClicked(event -> {
            hyperlinkOpener.openLink("https://github.com/google/gson");
        });

        linkJooq.setOnMouseClicked(event -> {
            hyperlinkOpener.openLink("https://www.jooq.org/");
        });

        linkStreamSupport.setOnMouseClicked(event -> {
            hyperlinkOpener.openLink("https://github.com/streamsupport/streamsupport");
        });

        linkMysqlJdbc.setOnMouseClicked(event -> {
            hyperlinkOpener.openLink("https://dev.mysql.com/downloads/connector/j/5.1.html");
        });

        linkGuice.setOnMouseClicked(event -> {
            hyperlinkOpener.openLink("https://github.com/google/guice");
        });

        linkWebSocket.setOnMouseClicked(event -> {
            hyperlinkOpener.openLink("https://github.com/TooTallNate/Java-WebSocket");
        });
    }

    public void setHyperlinkOpener(HyperlinkOpener hyperlinkOpener) {
        this.hyperlinkOpener = hyperlinkOpener;
    }
}
