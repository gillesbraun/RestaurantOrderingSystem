package lu.btsi.bragi.ros.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lu.btsi.bragi.ros.models.pojos.Invoice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class PrintableInvoice extends Invoice {
    private String CURRENCY = "\u20ac";
    private static final String TAX_NUM = "123123";
    private static final String EMAIL = "info@ltam.lu";
    private static final String TELEPHONE = "+352 123456789";
    private String language;

    private String restaurantName = "Restaurant chez BTSi",
        address = "19, rue Guillaume Schneider",
        address2 = "L-2522 Luxembourg";
    @FXML private Label labelTitle, labelAddress1, labelAddress2, labelInvoice, labelDateTime, labelTable,
            labelWaiter, labelTaxNumber, labelTelephone, labelEmail, labelTotalPrice;
    @FXML private VBox containerProductName, containerProductA, containerProductPrice, containerProductPriceTotal;

    PrintableInvoice(Invoice value, String language) {
        super(value);
        setOrders(value.getOrders());
        setPaid(value.getPaid());
        this.language = language;
    }

    VBox getNode() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PrintInvoice.fxml"));
        loader.setController(this);
        VBox printable = loader.load();
        assignComponents();

        return printable;
    }

    private void assignComponents() {
        labelTitle.setText(restaurantName);
        labelAddress1.setText(address);
        labelAddress2.setText(address2);

        labelInvoice.setText("ID");
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy / HH:mm"));
        labelDateTime.setText(datetime);
        labelTable.setText(getTable() + "");
        getProductListInvoice(language, CURRENCY).forEach(invoiceEntry -> {
            containerProductName.getChildren().add(new Label(invoiceEntry.productName));
            containerProductA.getChildren().add(new Label(Character.toString((char) 0x00e1)));
            containerProductPrice.getChildren().add(new Label(invoiceEntry.productPrice));
            containerProductPriceTotal.getChildren().add(new Label(invoiceEntry.productPriceTotal));
        });
        labelTotalPrice.setText(getTotalPrice(CURRENCY));

        labelWaiter.setText(getWaiters());
        labelTaxNumber.setText(TAX_NUM);
        labelTelephone.setText(TELEPHONE);
        labelEmail.setText(EMAIL);
    }
}
