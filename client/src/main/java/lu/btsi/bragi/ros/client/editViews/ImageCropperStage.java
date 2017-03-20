package lu.btsi.bragi.ros.client.editViews;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lu.btsi.bragi.ros.client.connection.Client;
import org.controlsfx.control.SnapshotView;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by gillesbraun on 20/03/2017.
 */
public class ImageCropperStage extends Stage {
    private Client client;
    private VBox vBox = new VBox(7);
    private HBox hBox = new HBox(7);
    private Button
            buttonSave = new Button("Save"),
            buttonCancel = new Button("Cancel");
    private ImageView imageView;
    private SnapshotView snapshowView;

    private WritableImage image;

    ImageCropperStage(Client client, File file) throws IOException {
        super();
        this.client = client;
        imageView = new ImageView(SwingFXUtils.toFXImage(ImageIO.read(file), null));
        snapshowView = new SnapshotView(imageView);
        snapshowView.setFixedSelectionRatio(1);
        snapshowView.setSelectionRatioFixed(true);
        imageView.fitWidthProperty().bind(vBox.widthProperty());
        imageView.fitHeightProperty().bind(vBox.heightProperty());
        imageView.setPreserveRatio(true);

        buttonCancel.setOnAction(buttonCancelPressed);
        buttonSave.setOnAction(buttonSavePressed);
        hBox.getChildren().addAll(buttonSave, buttonCancel);
        hBox.setPadding(new Insets(7));
        vBox.getChildren().addAll(hBox, snapshowView);

        Scene scene = new Scene(vBox);
        setScene(scene);
    }

    private final EventHandler<ActionEvent> buttonSavePressed = event -> {
        image = snapshowView.createSnapshot();
        close();
    };

    private final EventHandler<ActionEvent> buttonCancelPressed = event -> {
        close();
    };

    public WritableImage getImage() {
        return image;
    }
}
