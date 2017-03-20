package lu.btsi.bragi.ros.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lu.btsi.bragi.ros.client.settings.Config;
import lu.btsi.bragi.ros.models.pojos.Language;
import org.controlsfx.dialog.ExceptionDialog;

import java.io.IOException;

/**
 * Created by gillesbraun on 20/03/2017.
 */
public class SettingsStage extends Stage {
    @FXML private Button buttonSave, buttonCancel;
    @FXML private TextField
            textFieldCurrency, textFieldInvoiceTitle, textFieldInvoiceAddress1, textFieldInvoiceAddress2, textFieldInvoiceTax,
            textFieldInvoiceTelephone, textFieldInvoiceEmail;
    @FXML private ChoiceBox<Language> choiceBoxLanguage;

    SettingsStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SettingsStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();

        updateSettings();
        setTitle("Settings");
        setScene(new Scene(root));
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
    }

    public void buttonSavePressed(ActionEvent evt) {
        Config config = Config.getInstance();
        config.generalSettings.setCurrency(textFieldCurrency.getText());
        config.invoiceSettings.setTitle(textFieldInvoiceTitle.getText());
        config.invoiceSettings.setAddress(textFieldInvoiceAddress1.getText());
        config.invoiceSettings.setAddress2(textFieldInvoiceAddress2.getText());
        config.invoiceSettings.setTaxNumber(textFieldInvoiceTax.getText());
        config.invoiceSettings.setTelephone(textFieldInvoiceTelephone.getText());
        config.invoiceSettings.setEmail(textFieldInvoiceEmail.getText());
        try {
            Config.save();
        } catch (IOException e) {
            new ExceptionDialog(e).show();
        }
        close();
    }

    public void buttonCancelPressed(ActionEvent evt) {
        close();
    }
}
