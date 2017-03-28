package lu.btsi.bragi.ros.client;

import javafx.application.Platform;
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
import javafx.scene.layout.VBox;
import lu.btsi.bragi.ros.client.connection.ConnectionManager;
import lu.btsi.bragi.ros.client.settings.Config;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageType;
import lu.btsi.bragi.ros.models.pojos.Invoice;
import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.ProductPriceForOrder;
import lu.btsi.bragi.ros.models.pojos.Table;
import org.controlsfx.dialog.ExceptionDialog;
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
    @FXML private TableViewProducts listViewProducts;
    @FXML private Label labelTable, labelTime, labelTotalPrice, labelPaid;
    @FXML private Button buttonPay;

    private ObservableList<ProductPriceForOrder> listProducts = FXCollections.observableArrayList();

    private List<ProductPriceForOrder> produtsPriceForOrder;
    private List<Order> orders;
    private ContainerPaneInvoices parent;

    SingleInvoicePane(Table table, List<Order> orders, ContainerPaneInvoices invoicesContainerPane) throws IOException {
        this.orders = orders;
        parent = invoicesContainerPane;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SingleInvoicePanel.fxml"));
        loader.setController(this);
        VBox root = loader.load();
        root.setFillWidth(true);
        setFillWidth(true);
        getChildren().setAll(root);

        listViewProducts.setItems(listProducts);

        labelTable.setText(table.toString());
        fillList();
        setTotalPrice();
        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        DateTimeFormatter dateFormat = Config.getInstance().getTimeFormatter();
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
        sumOptional.ifPresent(sum -> labelTotalPrice.setText(Config.getInstance().formatCurrency(sum.doubleValue())));
    }

    private void fillList() {
        produtsPriceForOrder = orders.stream().flatMap(o -> o.getProductPriceForOrder().stream()).collect(toList());
        listProducts.setAll(
                Order.combineOrders(orders)
        );

        listViewProducts.prefHeightProperty().bind(Bindings.size(listProducts).add(1).multiply(24));
    }

    public void buttonPayPressed(ActionEvent event) {
        Invoice invoiceToSend = new Invoice();
        invoiceToSend.setPaid((byte)1);
        invoiceToSend.setOrders(orders);

        ConnectionManager.getInstance().sendWithAction(new Message<>(MessageType.Create, invoiceToSend, Invoice.class), m -> {
            try {
                List<Invoice> payload = new Message<Invoice>(m).getPayload();
                if(payload.size() > 0) {
                    Invoice invoice = payload.get(0);
                    Printer printer = Printer.getDefaultPrinter();
                    PrinterJob printerJob = PrinterJob.createPrinterJob(printer);
                    printerJob.showPrintDialog(null);
                    VBox node = new PrintableInvoice(invoice).getNode();
                    node.setStyle("-fx-font-family: \"Arial\";");
                    printerJob.printPage(node);
                    printerJob.endJob();

                    parent.refreshInvoices();
                }
            } catch (IOException | MessageException e) {
                Platform.runLater(() -> {
                    new ExceptionDialog(e).show();
                });
            }
        });
    }
}
