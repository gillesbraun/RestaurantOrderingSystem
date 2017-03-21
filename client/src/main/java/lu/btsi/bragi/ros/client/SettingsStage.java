package lu.btsi.bragi.ros.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
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

/**
 * Created by gillesbraun on 20/03/2017.
 */
public class SettingsStage extends Stage implements ConnectionCallback {
    @FXML private Button buttonSave, buttonCancel;
    @FXML private TextField
            textFieldCurrency, textFieldInvoiceTitle, textFieldInvoiceAddress1, textFieldInvoiceAddress2, textFieldInvoiceTax,
            textFieldInvoiceTelephone, textFieldInvoiceEmail, textFieldHostAddress;
    @FXML private ChoiceBox<Language> choiceBoxLanguage;
    @FXML private CheckBox checkBoxAutoDisover;
    private ObservableList<Language> listLanguages = FXCollections.observableArrayList();

    SettingsStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SettingsStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();

        updateSettings();
        setTitle("Settings");
        setScene(new Scene(root));
        choiceBoxLanguage.setDisable(true);
        choiceBoxLanguage.setItems(listLanguages);
        ConnectionManager.getInstance().addConnectionCallback(this);
    }

    private void updateSettings() {
        Config config = Config.getInstance();
        textFieldCurrency.setText(config.generalSettings.getCurrency());
        textFieldInvoiceTitle.setText(config.invoiceSettings.getTitle());
        textFieldInvoiceAddress1.setText(config.invoiceSettings.getAddress());
        textFieldInvoiceAddress2.setText(config.invoiceSettings.getAddress2());
        textFieldInvoiceTax.setText(config.invoiceSettings.getTaxNumber());
        textFieldInvoiceTelephone.setText(config.invoiceSettings.getTelephone());
        textFieldInvoiceEmail.setText(config.invoiceSettings.getEmail());

        textFieldHostAddress.setText(config.connectionSettings.getHostAddress());
        checkBoxAutoDisover.setSelected(config.connectionSettings.isAutoDiscovery());
    }

    private void saveConfig() {
        Config config = Config.getInstance();
        config.generalSettings.setCurrency(textFieldCurrency.getText());
        config.invoiceSettings.setTitle(textFieldInvoiceTitle.getText());
        config.invoiceSettings.setAddress(textFieldInvoiceAddress1.getText());
        config.invoiceSettings.setAddress2(textFieldInvoiceAddress2.getText());
        config.invoiceSettings.setTaxNumber(textFieldInvoiceTax.getText());
        config.invoiceSettings.setTelephone(textFieldInvoiceTelephone.getText());
        config.invoiceSettings.setEmail(textFieldInvoiceEmail.getText());
        config.connectionSettings.setAutoDiscovery(checkBoxAutoDisover.isSelected());
        if(choiceBoxLanguage.getValue() != null)
            config.generalSettings.setLanguage(choiceBoxLanguage.getValue());
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
