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
import lu.btsi.bragi.ros.client.connection.ConnectionManager;
import lu.btsi.bragi.ros.client.settings.Config;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageType;
import lu.btsi.bragi.ros.models.pojos.*;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by gillesbraun on 09/03/2017.
 */
public class SingleInvoicePane extends VBox {
    @FXML private ListView<ProductPriceForOrder> listViewProducts;
    @FXML private Label labelTable, labelTime, labelTotalPrice, labelPaid;
    @FXML private Button buttonPay;

    private ObservableList<ProductPriceForOrder> listProducts = FXCollections.observableArrayList();

    private List<ProductPriceForOrder> produtsPriceForOrder;
    private List<Order> orders;
    private InvoicesContainerPane parent;

    SingleInvoicePane(Table table, List<Order> orders, InvoicesContainerPane invoicesContainerPane) throws IOException {
        this.orders = orders;
        parent = invoicesContainerPane;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SingleInvoicePanel.fxml"));
        loader.setController(this);
        VBox root = loader.load();
        root.setFillWidth(true);
        setFillWidth(true);
        getChildren().setAll(root);

        listViewProducts.setItems(listProducts);
        listViewProducts.setCellFactory(param -> new ListCell<ProductPriceForOrder>(){
            @Override
            protected void updateItem(ProductPriceForOrder ppfo, boolean empty) {
                super.updateItem(ppfo, empty);
                if(ppfo == null) {
                    setText(null);
                    return;
                }
                ProductLocalized maybeLocalized = ppfo.getProductInLanguage(Config.getInstance().generalSettings.getLanguage());
                double pricePer = ppfo.getPricePerProduct().doubleValue();
                double priceTotal = ppfo.getTotalPriceOfProduct().doubleValue();
                long quantity = ppfo.getQuantity().longValue();
                String currency = Config.getInstance().generalSettings.getCurrency();
                setText(String.format("%s %s \u00e0 %.2f%s = %.2f%s", quantity, maybeLocalized.getLabel(), pricePer, currency, priceTotal, currency));
            }
        });

        labelTable.setText(table.toString());
        fillList();
        setTotalPrice();
        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("H:mm");
        orders.stream().map(Order::getCreatedAt).sorted().findFirst().ifPresent(
                timestamp -> labelTime.setText(dateFormat.format(timestamp.toLocalDateTime()))
        );
        labelPaid.setGraphic(fa.create(FontAwesome.Glyph.CIRCLE_THIN));
        labelPaid.setText("");
    }

    private void setTotalPrice() {
        Optional<BigDecimal> sumOptional = orders.stream()
                .map(Order::getTotalPriceOfOrder)
                .reduce(BigDecimal::add);
        sumOptional.ifPresent(sum -> labelTotalPrice.setText(String.format("%.2f%s", sum.doubleValue(), Config.getInstance().generalSettings.getCurrency())));
    }

    private void fillList() {
        produtsPriceForOrder = orders.stream().flatMap(o -> o.getProductPriceForOrder().stream()).collect(toList());
        listProducts.setAll(
                Order.combineOrders(orders)
        );

        listViewProducts.prefHeightProperty().bind(Bindings.size(listProducts).multiply(28));
    }

    public void buttonPayPressed(ActionEvent event) {
        Invoice invoice = new Invoice();
        invoice.setPaid((byte)1);
        invoice.setOrders(orders);
        try {
            Printer printer = Printer.getDefaultPrinter();
            PrinterJob printerJob = PrinterJob.createPrinterJob(printer);
            printerJob.showPrintDialog(null);
            VBox node = new PrintableInvoice(invoice).getNode();
            node.setStyle("-fx-font-family: \"Arial\";");
            printerJob.printPage(node);
            printerJob.endJob();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConnectionManager.getInstance().send(new Message<>(MessageType.Create, invoice, Invoice.class));
        parent.refreshInvoices();
    }
}
