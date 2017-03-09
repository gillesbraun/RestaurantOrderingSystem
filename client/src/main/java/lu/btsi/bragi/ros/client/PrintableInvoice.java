package lu.btsi.bragi.ros.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lu.btsi.bragi.ros.models.pojos.Invoice;
import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.ProductPriceForOrder;
import lu.btsi.bragi.ros.models.pojos.Waiter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

class PrintableInvoice extends Invoice {
    private static final String TAX_NUM = "123123";
    private static final String EMAIL = "info@ltam.lu";
    private static final String TELEPHONE = "+352 123456789";
    private String language;

    private String restaurantName = "Restaurant chez BTSi",
        address = "19, rue Guillaume Schneider",
        address2 = "L-2522 Luxembourg";
    @FXML private Label labelTitle, labelAddress1, labelAddress2, labelInvoice, labelDateTime, labelTable, labelPriceTotal,
            labelWaiter, labelTaxNumber, labelTelephone, labelEmail;
    @FXML private VBox containerProductName, containerProductA, containerProductPrice, containerProductPriceTotal;

    PrintableInvoice(Invoice value, String language) {
        super(value);
        this.language = language;
    }

    VBox getNode() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PrintInvoice.fxml"));
        loader.setController(this);
        VBox printable = loader.load();
        printable.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
        //assignComponents();

        return printable;
    }

    private void assignComponents() {
        labelTitle.setText(restaurantName);
        labelAddress1.setText(address);
        labelAddress2.setText(address2);

        labelInvoice.setText("getId().toString()");
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd / HH:mm"));
        labelDateTime.setText(datetime);
        getOrders().stream()
                .map(Order::getTableId)
                .findFirst()
                .ifPresent(tableID -> {
            labelTable.setText(tableID + "");
        });
        List<ProductPriceForOrder> productPriceForOrders = getOrders().stream().flatMap(
                order -> order.getProductPriceForOrder().stream()).collect(Collectors.toList());

        getOrders().stream().flatMap(
                order -> order.getProductPriceForOrder().stream())
                .map(ProductPriceForOrder::getProduct)
                .flatMap(
                        product -> product.getProductLocalized().stream()
                        .filter(productLocalized -> productLocalized.getLanguageCode().equals(language))
                ).forEach(productLocalized -> {


            containerProductA.getChildren().add(new Label(Character.toString((char) 0x00e1)));

            productPriceForOrders.stream()
                .filter(ppfo -> ppfo.getProductId().equals(productLocalized.getProductId()))
                .findFirst()
                .ifPresent(productPriceForOrder -> {

                    containerProductName.getChildren().add(new Label(
                            productPriceForOrder.getQuantity() + "x " +productLocalized.getLabel()
                    ));
                    containerProductPrice.getChildren().add(new Label(
                            new BigDecimal(productPriceForOrder.getPricePerProduct()).toString() + " €"
                    ));
                    containerProductPriceTotal.getChildren().add(new Label(
                            new BigDecimal(productPriceForOrder.getPricePerProduct()).multiply(
                                    new BigDecimal(productPriceForOrder.getQuantity().toBigInteger())).toString() + " €"));
            });

            String waiters = getOrders().stream().map(Order::getWaiter).map(Waiter::getName).collect(Collectors.joining(", "));
            labelWaiter.setText(waiters);

            labelTaxNumber.setText(TAX_NUM);
            labelTelephone.setText(TELEPHONE);
            labelEmail.setText(EMAIL);
        });
    }
}
