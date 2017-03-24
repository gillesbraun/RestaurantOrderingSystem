package lu.btsi.bragi.ros.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lu.btsi.bragi.ros.client.connection.ConnectionCallback;
import lu.btsi.bragi.ros.client.connection.ConnectionManager;
import lu.btsi.bragi.ros.client.settings.Config;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.pojos.Language;
import org.controlsfx.dialog.ExceptionDialog;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

/**
 * Created by gillesbraun on 20/03/2017.
 */
public class SettingsStage extends Stage implements ConnectionCallback {
    @FXML private Label labelCurrency;
    @FXML private Button buttonSave, buttonCancel;
    @FXML private TextField
            textFieldInvoiceTitle, textFieldInvoiceAddress1, textFieldInvoiceAddress2, textFieldInvoiceTax,
            textFieldInvoiceTelephone, textFieldInvoiceEmail, textFieldHostAddress;
    @FXML private ChoiceBox<Language> choiceBoxLanguage;
    @FXML private ChoiceBox<Locale> choiceBoxLocale;
    @FXML private CheckBox checkBoxAutoDisover;
    private final ObservableList<Locale> listLocales;
    private ObservableList<Language> listLanguages = FXCollections.observableArrayList();

    SettingsStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SettingsStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();

        setTitle("Settings");
        setScene(new Scene(root));
        choiceBoxLanguage.setDisable(true);
        choiceBoxLanguage.setItems(listLanguages);

        listLocales = FXCollections.observableList(Arrays.stream(Locale.getAvailableLocales())
                .filter(l -> l.getCountry().length() == 2)
                .sorted(
                Comparator.comparing(Locale::toString)
        ).collect(toList()));

        choiceBoxLocale.setItems(listLocales);
        choiceBoxLocale.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedLocale) -> {
            if(selectedLocale != null)
                labelCurrency.setText(Currency.getInstance(selectedLocale).getSymbol(selectedLocale));
        });

        updateSettings();
        ConnectionManager.getInstance().addConnectionCallback(this);
    }

    private void updateSettings() {
        Config config = Config.getInstance();
        textFieldInvoiceTitle.setText(config.invoiceSettings.getTitle());
        textFieldInvoiceAddress1.setText(config.invoiceSettings.getAddress());
        textFieldInvoiceAddress2.setText(config.invoiceSettings.getAddress2());
        textFieldInvoiceTax.setText(config.invoiceSettings.getTaxNumber());
        textFieldInvoiceTelephone.setText(config.invoiceSettings.getTelephone());
        textFieldInvoiceEmail.setText(config.invoiceSettings.getEmail());

        textFieldHostAddress.setText(config.connectionSettings.getHostAddress());
        checkBoxAutoDisover.setSelected(config.connectionSettings.isAutoDiscovery());

        listLocales.stream()
                .filter(l -> l.equals(config.generalSettings.getLocale()))
                .findFirst()
                .ifPresent(l -> choiceBoxLocale.getSelectionModel().select(l));
    }

    private void saveConfig() {
        Config config = Config.getInstance();
        config.invoiceSettings.setTitle(textFieldInvoiceTitle.getText());
        config.invoiceSettings.setAddress(textFieldInvoiceAddress1.getText());
        config.invoiceSettings.setAddress2(textFieldInvoiceAddress2.getText());
        config.invoiceSettings.setTaxNumber(textFieldInvoiceTax.getText());
        config.invoiceSettings.setTelephone(textFieldInvoiceTelephone.getText());
        config.invoiceSettings.setEmail(textFieldInvoiceEmail.getText());
        config.connectionSettings.setAutoDiscovery(checkBoxAutoDisover.isSelected());
        if(choiceBoxLanguage.getValue() != null)
            config.generalSettings.setLanguage(choiceBoxLanguage.getValue());
        if(choiceBoxLocale.getValue() != null) {
            config.generalSettings.setLocale(choiceBoxLocale.getValue());
        }
        try {
            Config.save();
        } catch (IOException e) {
            new ExceptionDialog(e).show();
        }
    }

    public void buttonSavePressed(ActionEvent evt) {
        saveConfig();
        close();
    }

    public void buttonCancelPressed(ActionEvent evt) {
        close();
    }

    public void buttonReconnectPressed(ActionEvent evt) {
        ConnectionManager.getInstance().newClient();
    }

    public void buttonApplyPressed(ActionEvent evt) {
        saveConfig();
    }

    @Override
    public void connectionClosed(String reason) {
    }

    @Override
    public void connectionOpened(String message) {
        ConnectionManager.getInstance().sendWithAction(new MessageGet<>(Language.class), t -> {
            try {
                listLanguages.setAll(new Message<Language>(t).getPayload());
                choiceBoxLanguage.setDisable(false);
                String needle = Config.getInstance().generalSettings.getLanguage().getCode();
                listLanguages.stream()
                        .filter(l -> l.getCode().equals(needle))
                        .findFirst()
                        .ifPresent(l -> choiceBoxLanguage.getSelectionModel().select(l));
            } catch (MessageException e) {
                ExceptionDialog exceptionDialog = new ExceptionDialog(e);
                exceptionDialog.initOwner(this);
                exceptionDialog.show();
            }
        });
    }
}
