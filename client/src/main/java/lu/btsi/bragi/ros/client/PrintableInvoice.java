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
        String datetime = Config.getInstance().getDateTimeFormatter().format(LocalDateTime.now());
        labelDateTime.setText(datetime);
        labelTable.setText(getTable() + "");
        Config config = Config.getInstance();
        getProductListInvoice(Config.getInstance().generalSettings.getLanguage()).forEach(invoiceEntry -> {
            containerProductName.getChildren().add(new Label(invoiceEntry.quantity + "x" + invoiceEntry.productName));
            containerProductA.getChildren().add(new Label(Character.toString((char) 0x00e0)));
            containerProductPrice.getChildren().add(new Label(config.formatCurrency(invoiceEntry.productPrice)));
            containerProductPriceTotal.getChildren().add(new Label(config.formatCurrency(invoiceEntry.productPriceTotal)));
        });
        labelTotalPrice.setText(config.formatCurrency(getTotalPrice().doubleValue()));

        labelWaiter.setText(getWaiters());
        labelTaxNumber.setText(invoiceSettings.getTaxNumber());
        labelTelephone.setText(invoiceSettings.getTelephone());
        labelEmail.setText(invoiceSettings.getEmail());
    }
}
