package lu.btsi.bragi.ros.client.editViews;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import lu.btsi.bragi.ros.client.connection.ConnectionManager;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.message.MessageType;
import lu.btsi.bragi.ros.models.pojos.Language;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Created by gillesbraun on 03/03/2017.
 */
public class LanguagesStage extends Stage {
    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd MMM yyyy H:mm");

    @FXML private Label labelUpdatedAt, labelCreatedAt;
    @FXML private Button buttonRefreshLanguages, buttonAddLanguage, buttonDeleteLanguage, buttonSaveLanguage;
    @FXML private ListView<Language> listViewLanguages;
    @FXML private TextField textFieldLanguageName, textFieldLanguageCode;
    @FXML private VBox detailPane;

    private ObservableList<Language> languagesForList;

    public LanguagesStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LanguagesStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        setTitle("Languages");
        setScene(new Scene(root));

        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        buttonAddLanguage.setGraphic(fa.create(Glyph.PLUS_CIRCLE));
        buttonDeleteLanguage.setGraphic(fa.create(Glyph.TRASH));
        buttonRefreshLanguages.setGraphic(fa.create(Glyph.REFRESH));

        textFieldLanguageCode.textProperty().addListener((observable, oldValue, newText) -> {
            if(newText.length() >= 2)
                textFieldLanguageCode.setText(newText.substring(0, 2));
        });

        loadData();
        listViewLanguages.getSelectionModel().selectedItemProperty().addListener(languageSelectedListener);

    }

    private void loadData() {
        ConnectionManager.getInstance().sendWithAction(new MessageGet<>(Language.class), message -> {
            try {
                List<Language> languages = new Message<Language>(message).getPayload();
                languagesForList = FXCollections.observableList(languages);
                listViewLanguages.setItems(languagesForList);
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }
    private final ChangeListener<? super Language> languageSelectedListener = new ChangeListener<Language>() {
        @Override
        public void changed(ObservableValue<? extends Language> observable, Language oldValue, Language selectedLanguage) {
            buttonDeleteLanguage.setDisable(selectedLanguage == null);
            detailPane.setDisable(selectedLanguage == null);
            buttonSaveLanguage.setDisable(false);
            if(selectedLanguage == null) {
                textFieldLanguageCode.setText("");
                textFieldLanguageName.setText("");
                labelCreatedAt.setText("-");
                labelUpdatedAt.setText("-");
            } else {
                textFieldLanguageCode.setText(selectedLanguage.getCode());
                textFieldLanguageName.setText(selectedLanguage.getName());
                labelCreatedAt.setText(selectedLanguage.getCreatedAt().toLocalDateTime().format(dateTimeFormat));
                labelUpdatedAt.setText(selectedLanguage.getUpdatedAt().toLocalDateTime().format(dateTimeFormat));
            }
        }
    };

    public void buttonRefreshPressed(ActionEvent evt) {
        loadData();
    }

    public void buttonAddLanguagePressed(ActionEvent evt) {
        Dialog<Pair<String,String>> addLangDialog = new Dialog<>();
        addLangDialog.initOwner(this);
        addLangDialog.setTitle("Add Language");
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        addLangDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButtonType);

        Label labelCode = new Label("Language Code:");
        TextField textFieldCode = new TextField();
        HBox hBoxCode = new HBox(14, labelCode, textFieldCode);

        textFieldCode.textProperty().addListener((observable, oldValue, newText) -> {
            if(newText.length() >= 2)
                textFieldCode.setText(newText.substring(0, 2));
        });

        Platform.runLater(textFieldCode::requestFocus);

        Label labelName = new Label("Language Name: ");
        TextField textFieldName = new TextField("");
        HBox hBoxName = new HBox(14, labelName, textFieldName);

        hBoxCode.setAlignment(Pos.CENTER_LEFT);
        hBoxName.setAlignment(Pos.CENTER_LEFT);
        VBox root = new VBox(14, hBoxCode, hBoxName);
        addLangDialog.getDialogPane().setContent(root);

        addLangDialog.setResultConverter(param -> {
            if(param.equals(addButtonType) &&
                    textFieldCode.getText().trim().length() > 0 &&
                    textFieldName.getText().trim().length() > 0
                    ) {
                return new Pair<>(textFieldCode.getText(), textFieldName.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = addLangDialog.showAndWait();
        if(result.isPresent()) {
            Pair<String, String> codeAndName = result.get();
            Language language = new Language();
            language.setCode(codeAndName.getKey());
            language.setName(codeAndName.getValue());
            ConnectionManager.getInstance().sendWithAction(new Message<>(MessageType.Create, language, Language.class), message -> {
                try {
                    new Message<Language>(message);
                } catch (MessageException e) {
                    if(e.getLocalizedMessage().contains("Duplicate entry")) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText("Language Code '"+codeAndName.getKey()+"' already used.");
                        a.initOwner(this);
                        a.show();
                    } else {
                        ExceptionDialog exceptionDialog = new ExceptionDialog(e);
                        exceptionDialog.initOwner(this);
                        exceptionDialog.show();
                    }
                }
            });
            loadData();
        }
    }

    public void buttonDeleteLanguagePressed(ActionEvent evt) {
        Language selectedItem = listViewLanguages.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            ConnectionManager.getInstance().send(new Message<>(MessageType.Delete, selectedItem, Language.class));
            loadData();
        }
    }

    public void buttonSaveLanguagePressed(ActionEvent evt) {
        Language selectedItem = listViewLanguages.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            selectedItem.setCode(textFieldLanguageCode.getText());
            selectedItem.setName(textFieldLanguageName.getText());
            ConnectionManager.getInstance().send(new Message<>(MessageType.Update, selectedItem, Language.class));
            loadData();
        }
    }

    public void textFieldLanguageCodeKeyReleased(KeyEvent evt) {
        Language selectedItem = listViewLanguages.getSelectionModel().getSelectedItem();
        // check if ID already present, prevent save
        long count = languagesForList.stream()
                // Don't count itself
                .filter(l -> ! l.equals(selectedItem))
                // Filter where code is the same
                .filter(l -> l.getCode().equals(textFieldLanguageCode.getText().trim()))
                .count();
        buttonSaveLanguage.setDisable(count > 0);
    }
}
