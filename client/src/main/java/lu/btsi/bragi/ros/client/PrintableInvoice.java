package lu.btsi.bragi.ros.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lu.btsi.bragi.ros.client.settings.Config;
import lu.btsi.bragi.ros.client.settings.InvoiceSettings;
import lu.btsi.bragi.ros.models.pojos.Invoice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class PrintableInvoice extends Invoice {
    @FXML private Label labelTitle, labelAddress1, labelAddress2, labelInvoice, labelDateTime, labelTable,
            labelWaiter, labelTaxNumber, labelTelephone, labelEmail, labelTotalPrice;
    @FXML private VBox containerProductName, containerProductA, containerProductPrice, containerProductPriceTotal;

    PrintableInvoice(Invoice value) {
        super(value);
        setOrders(value.getOrders());
        setPaid(value.getPaid());
    }

    VBox getNode() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PrintInvoice.fxml"));
        loader.setController(this);
        VBox printable = loader.load();
        assignComponents();

        return printable;
    }

    private void assignComponents() {
        InvoiceSettings invoiceSettings = Config.getInstance().invoiceSettings;
        labelTitle.setText(invoiceSettings.getTitle());
        labelAddress1.setText(invoiceSettings.getAddress());
        labelAddress2.setText(invoiceSettings.getAddress2());

        labelInvoice.setText("ID");
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy / HH:mm"));
        labelDateTime.setText(datetime);
        labelTable.setText(getTable() + "");
        String currency = Config.getInstance().generalSettings.getCurrency();
        getProductListInvoice(Config.getInstance().generalSettings.getLanguage(), Config.getInstance().generalSettings.getCurrency()).forEach(invoiceEntry -> {
            containerProductName.getChildren().add(new Label(invoiceEntry.quantity + "x" + invoiceEntry.productName));
            containerProductA.getChildren().add(new Label(Character.toString((char) 0x00e0)));
            containerProductPrice.getChildren().add(new Label(invoiceEntry.productPrice + currency));
            containerProductPriceTotal.getChildren().add(new Label(invoiceEntry.productPriceTotal + currency));
        });
        labelTotalPrice.setText(getTotalPrice(Config.getInstance().generalSettings.getCurrency()));

        labelWaiter.setText(getWaiters());
        labelTaxNumber.setText(invoiceSettings.getTaxNumber());
        labelTelephone.setText(invoiceSettings.getTelephone());
        labelEmail.setText(invoiceSettings.getEmail());
    }
}
