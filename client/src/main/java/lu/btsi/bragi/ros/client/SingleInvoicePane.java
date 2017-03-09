package lu.btsi.bragi.ros.client;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import lu.btsi.bragi.ros.models.pojos.*;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.jooq.types.UInteger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by gillesbraun on 09/03/2017.
 */
public class SingleInvoicePane extends VBox {

    @FXML private ListView<ProductLocalized> listViewProducts;
    @FXML private Label labelTable, lableInvoice, labelTotalPrice, labelPaid;
    @FXML private Button buttonPay;

    private ObservableList<ProductLocalized> listProducts = FXCollections.observableArrayList();

    private static final String LANGUAGE = "en";
    private List<ProductPriceForOrder> produtsPriceForOrder;
    private Table table;
    private List<Order> orders;
    private InvoicesContainerPane parent;

    SingleInvoicePane(Table table, List<Order> orders, InvoicesContainerPane invoicesContainerPane) throws IOException {
        this.table = table;
        this.orders = orders;
        parent = invoicesContainerPane;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SingleInvoicePanel.fxml"));
        loader.setController(this);
        VBox root = loader.load();
        root.setFillWidth(true);
        setFillWidth(true);
        getChildren().setAll(root);

        listViewProducts.setItems(listProducts);
        listViewProducts.setCellFactory(param -> new ListCell<ProductLocalized>(){
            @Override
            protected void updateItem(ProductLocalized item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null) {
                    setText(null);
                    return;
                }
                Optional<UInteger> quantity = produtsPriceForOrder.stream()
                        .filter(ppfo -> ppfo.getProductId().equals(item.getProductId()))
                        .map(ProductPriceForOrder::getQuantity)
                        .findFirst();
                Optional<Double> price = produtsPriceForOrder.stream()
                        .filter(ppfo -> ppfo.getProductId().equals(item.getProductId()))
                        .map(ProductPriceForOrder::getPricePerProduct)
                        .findFirst();
                quantity.ifPresent(qty -> setText(qty + " " + item.getLabel() + " " + price.orElse(0.0) + "€"));
            }
        });

        labelTable.setText(table.toString());
        fillList();
        setTotalPrice();
        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        labelPaid.setGraphic(fa.create(FontAwesome.Glyph.CIRCLE_THIN));
        labelPaid.setText("");
    }

    private void setTotalPrice() {
        Optional<BigDecimal> sumOptional = orders.stream()
                .flatMap(order -> order.getProductPriceForOrder().stream())
                .map(value -> BigDecimal.valueOf(value.getPricePerProduct()).multiply(new BigDecimal(value.getQuantity().toBigInteger())))
                .reduce(BigDecimal::add);
        sumOptional.ifPresent(sum -> labelTotalPrice.setText(sum + " €"));
    }

    private void fillList() {
        produtsPriceForOrder = orders.stream().flatMap(o -> o.getProductPriceForOrder().stream()).collect(toList());
        listProducts.setAll(
                orders.stream()
                        .flatMap(o -> o.getProductPriceForOrder().stream())
                        .map(ProductPriceForOrder::getProduct)
                .flatMap(product ->
                                product.getProductLocalized().stream()
                                .filter(pL -> pL.getLanguageCode().equals(LANGUAGE))
                )
                .collect(toList())
        );

        listViewProducts.prefHeightProperty().bind(Bindings.size(listProducts).multiply(28));
    }

    public void buttonPayPressed(ActionEvent event) {
        Invoice invoice = new Invoice();
        invoice.setPaid((byte)1);
        invoice.setOrders(orders);
        System.out.println(invoice.getOrders().size());
        try {
            Printer printer = Printer.getDefaultPrinter();
            PrinterJob printerJob = PrinterJob.createPrinterJob(printer);
            printerJob.showPrintDialog(null);
            VBox node = new PrintableInvoice(invoice, LANGUAGE).getNode();
            node.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
            printerJob.printPage(node);
            printerJob.endJob();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //parent.send(new Message<>(MessageType.Create, invoice, Invoice.class));
        //parent.loadInvoices();
    }
}
