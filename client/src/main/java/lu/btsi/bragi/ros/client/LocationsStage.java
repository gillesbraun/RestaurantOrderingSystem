package lu.btsi.bragi.ros.client;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.pojos.Location;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static lu.btsi.bragi.ros.models.message.MessageType.*;

/**
 * Created by gillesbraun on 06/03/2017.
 */
public class LocationsStage extends Stage {
    private final Client client;

    @FXML private Button buttonAdd, buttonDelete, buttonRefresh, buttonSave;
    @FXML private ListView<Location> listViewLocations;
    @FXML private VBox detailPane;
    @FXML private Label labelID, labelCreatedAt, labelUpdatedAt;
    @FXML private TextField textFieldDescription;

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy H:mm");

    private Location selectedLocation;
    private ObservableList<Location> locationsForList;

    public LocationsStage(Client client) throws IOException {
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LocationsStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        setTitle("Locations");
        setScene(new Scene(root));

        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        buttonAdd.setGraphic(fa.create(FontAwesome.Glyph.PLUS_CIRCLE));
        buttonDelete.setGraphic(fa.create(FontAwesome.Glyph.TRASH));
        buttonRefresh.setGraphic(fa.create(FontAwesome.Glyph.REFRESH));

        detailPane.setDisable(true);

        loadData();
        listViewLocations.getSelectionModel().selectedItemProperty().addListener(locationChangeListener);
    }

    private void loadData() {
        client.sendWithAction(new MessageGet<>(Location.class), message -> {
            try {
                Location previous = selectedLocation;
                List<Location> payload = new Message<Location>(message).getPayload();
                locationsForList = FXCollections.observableList(payload);
                listViewLocations.setItems(locationsForList);
                if(previous != null) {
                    Optional<Location> newPrev = locationsForList.stream()
                            .filter(l -> l.getId().equals(previous.getId()))
                            .findFirst();
                    if(newPrev.isPresent()) {
                        listViewLocations.getSelectionModel().select(newPrev.get());
                    }
                }
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }

    private final ChangeListener<? super Location> locationChangeListener = (observable, oldValue, selectedLocation) -> {
        this.selectedLocation = selectedLocation;
        detailPane.setDisable(selectedLocation == null);
        labelID.setText(selectedLocation == null ? "-" : selectedLocation.getId()+"");
        textFieldDescription.setText(selectedLocation == null ? "" : selectedLocation.getDescription());
        buttonDelete.setDisable(selectedLocation == null);
        labelCreatedAt.setText(selectedLocation == null ? "-" : selectedLocation.getCreatedAt().toLocalDateTime().format(dateFormat));
        labelUpdatedAt.setText(selectedLocation == null ? "-" : selectedLocation.getUpdatedAt().toLocalDateTime().format(dateFormat));
    };

    public void buttonRefreshPressed(ActionEvent evt) {
        loadData();
    }

    public void buttonAddPressed(ActionEvent evt) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText("Create a new Location");
        inputDialog.setContentText("Enter a short description for this Location. For example: Kitchen, Bar, ...");
        Optional<String> s = inputDialog.showAndWait();
        if(s.isPresent() && s.get().trim().length() > 0) {
            Location location = new Location();
            location.setDescription(s.get());
            client.send(new Message<>(Create, location, Location.class));
            loadData();
        }
    }

    public void buttonDeletePressed(ActionEvent evt) {
        if(selectedLocation != null) {
            client.send(new Message<>(Delete, selectedLocation, Location.class));
            loadData();
        }
    }

    public void buttonSavePressed(ActionEvent evt) {
        if(selectedLocation != null && textFieldDescription.getText().trim().length() > 0) {
            selectedLocation.setDescription(textFieldDescription.getText());
            client.send(new Message<>(Update, selectedLocation, Location.class));
            loadData();
        }
    }
}
