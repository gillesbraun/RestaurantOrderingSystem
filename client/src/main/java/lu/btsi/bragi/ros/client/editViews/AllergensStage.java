package lu.btsi.bragi.ros.client.editViews;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lu.btsi.bragi.ros.client.connection.ConnectionManager;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.pojos.Allergen;
import lu.btsi.bragi.ros.models.pojos.AllergenLocalized;
import lu.btsi.bragi.ros.models.pojos.Language;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static lu.btsi.bragi.ros.models.message.MessageType.Create;
import static lu.btsi.bragi.ros.models.message.MessageType.Delete;

/**
 * Created by gillesbraun on 03/03/2017.
 */
public class AllergensStage extends Stage {
    @FXML private Label labelID;
    @FXML private Button buttonRefreshAllergen, buttonAddAllergen, buttonDeleteAllergen, buttonAddTranslation,
                         buttonEditTranslation;
    @FXML private ListView<Allergen> listViewAllergens;
    @FXML private ListView<AllergenLocalized> listViewTranslations;
    @FXML private TextField textFieldTranslation;
    @FXML private ChoiceBox<Language> choiceBoxLanguage;

    private Allergen selectedAllergen = null;
    private AllergenLocalized selectedAllergenLocalized = null;

    private ObservableList<Allergen> allergensForList;
    private ObservableList<AllergenLocalized> allergensLocalizedForList;
    private ObservableList<Language> languagesForChoiceBox;

    public AllergensStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllergensStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();

        setTitle("Allergens");
        setScene(new Scene(root));

        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        buttonRefreshAllergen.setGraphic(fa.create(FontAwesome.Glyph.REFRESH));
        buttonAddAllergen.setGraphic(fa.create(FontAwesome.Glyph.PLUS_CIRCLE));
        buttonDeleteAllergen.setGraphic(fa.create(FontAwesome.Glyph.TRASH));

        buttonAddTranslation.setGraphic(fa.create(FontAwesome.Glyph.PLUS_CIRCLE));
        buttonEditTranslation.setGraphic(fa.create(FontAwesome.Glyph.ARROW_UP));

        buttonEditTranslation.setDisable(true);

        textFieldTranslation.setOnKeyReleased(textFieldTranslationKeyReleased);
        listViewAllergens.getSelectionModel().selectedItemProperty().addListener(allergenSelected);
        listViewTranslations.getSelectionModel().selectedItemProperty().addListener(translationSelected);
        listViewTranslations.setCellFactory(param -> new ListCell<AllergenLocalized>() {
            @Override
            protected void updateItem(AllergenLocalized item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? null : item.getLanguageCode() + ": " + item.getLabel());
            }
        });
        loadData();
    }

    private final ChangeListener<? super Allergen> allergenSelected = (observable, oldValue, selectedAllergen) -> {
        this.selectedAllergen = selectedAllergen;
        if(selectedAllergen != null) {
            labelID.setText(selectedAllergen.getId()+"");
            loadTranslations();
        } else {
            labelID.setText("-");
            allergensLocalizedForList.clear();
        }
    };

    private final ChangeListener<? super AllergenLocalized> translationSelected = (observable, oldValue, allergenLocalized) -> {
        this.selectedAllergenLocalized = allergenLocalized;
        if(allergenLocalized == null) {
            textFieldTranslation.clear();
            buttonEditTranslation.setDisable(true);
        } else {
            textFieldTranslation.setText(allergenLocalized.getLabel());
        }
    };

    private final EventHandler<? super KeyEvent> textFieldTranslationKeyReleased = event -> {
        buttonEditTranslation.setDisable(
                selectedAllergenLocalized == null ||
                selectedAllergenLocalized.getLabel().equals(textFieldTranslation.getText().trim())
        );
    };

    private void loadData() {
        // Get allergens
        ConnectionManager.getInstance().sendWithAction(new MessageGet<>(Allergen.class), message -> {
            try {
                List<Allergen> payload = new Message<Allergen>(message).getPayload();
                allergensForList = FXCollections.observableList(payload);
                listViewAllergens.setItems(allergensForList);
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadTranslations() {
        if(selectedAllergen == null)
            return;
        ConnectionManager.getInstance().sendWithAction(new MessageGet<>(AllergenLocalized.class), message -> {
            try {
                List<AllergenLocalized> payload = new Message<AllergenLocalized>(message).getPayload();
                allergensLocalizedForList = FXCollections.observableList(
                        payload.stream()
                        .filter(al -> al.getAllergenId().equals(selectedAllergen.getId()))
                        .collect(Collectors.toList())
                );
                listViewTranslations.setItems(allergensLocalizedForList);
                updateLanguageChoiceBox();
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateLanguageChoiceBox() {
        if(selectedAllergen == null)
            return;

        ConnectionManager.getInstance().sendWithAction(new MessageGet<>(Language.class), message -> {
            try {
                List<Language> payload = new Message<Language>(message).getPayload();
                // Gather already translated languages
                List<String> languageCodesTranslated = allergensLocalizedForList.stream()
                        .filter(al -> al.getAllergenId().equals(selectedAllergen.getId()))
                        .map(AllergenLocalized::getLanguageCode)
                        .collect(Collectors.toList());

                List<Language> languagesNotTranslated = payload.stream()
                        .filter(l -> !languageCodesTranslated.contains(l.getCode()))
                        .collect(Collectors.toList());

                languagesForChoiceBox = FXCollections.observableList(languagesNotTranslated);
                choiceBoxLanguage.setItems(languagesForChoiceBox);

                choiceBoxLanguage.setDisable(languagesNotTranslated.isEmpty());
                buttonAddTranslation.setDisable(languagesNotTranslated.isEmpty());
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }

    public void buttonRefreshAllergenPressed(ActionEvent evt) {
        loadData();
    }

    public void buttonAddAllergenPressed(ActionEvent evt) {
        Allergen allergen = new Allergen();
        allergen.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        ConnectionManager.getInstance().send(new Message<>(Create, allergen, Allergen.class));
        loadData();
    }

    public void buttonDeleteAllergenPressed(ActionEvent evt) {
        if(selectedAllergen != null) {
            ConnectionManager.getInstance().send(new Message<>(Delete, selectedAllergen, Allergen.class));
            loadData();
        }
    }

    public void buttonAddTranslationPressed(ActionEvent evt) {
        if(selectedAllergen == null || textFieldTranslation.getText().trim().length() == 0 || choiceBoxLanguage.getValue() == null)
            return;
        AllergenLocalized allergenLocalized = new AllergenLocalized();
        allergenLocalized.setAllergenId(selectedAllergen.getId());
        allergenLocalized.setLanguageCode(choiceBoxLanguage.getValue().getCode());
        allergenLocalized.setLabel(textFieldTranslation.getText());
        ConnectionManager.getInstance().send(new Message<>(Create, allergenLocalized, AllergenLocalized.class));
        textFieldTranslation.clear();
        loadTranslations();
    }

    public void buttonEditTranslationPressed(ActionEvent evt) {
        if(selectedAllergenLocalized == null || textFieldTranslation.getText().trim().length() == 0)
            return;
        selectedAllergenLocalized.setLabel(textFieldTranslation.getText());
    }
}
