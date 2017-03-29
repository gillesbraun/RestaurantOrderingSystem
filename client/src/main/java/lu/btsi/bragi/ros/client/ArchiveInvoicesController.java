package lu.btsi.bragi.ros.client;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.converter.LocalDateStringConverter;
import lu.btsi.bragi.ros.client.connection.ConnectionManager;
import lu.btsi.bragi.ros.client.settings.Config;
import lu.btsi.bragi.ros.models.message.*;
import lu.btsi.bragi.ros.models.pojos.Invoice;
import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.ProductPriceForOrder;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Created by gillesbraun on 24/03/2017.
 */
public class ArchiveInvoicesController {

    public DatePicker datePickerFrom, datePickerUntil;
    public TableColumn<Invoice, String> tableColumnPaid, tableColumnPrice, tableColumnID, tableColumnTable, tableColumnDateTime;
    public TableView<Invoice> tableViewInvoice;
    public TableViewProducts tableViewProducts;
    public Label labelWaiter, labelPaid, labelInvoiceID, labelTableID;
    public VBox vboxDetail;
    private ObservableList<ProductPriceForOrder> listPPFO = FXCollections.observableArrayList();

    private Window owner;

    public void initialize() {
        DateTimeFormatter dateFormat = Config.getInstance().getDateFormatter();
        DateTimeFormatter dateTimeFormat = Config.getInstance().getDateTimeFormatter();

        datePickerFrom.setConverter(new LocalDateStringConverter(dateFormat, dateFormat));
        datePickerUntil.setConverter(new LocalDateStringConverter(dateFormat, dateFormat));
        datePickerFrom.setValue(LocalDate.now().minusDays(7));
        datePickerUntil.setValue(LocalDate.now());

        tableColumnID.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue().getId().toString())
        );
        tableColumnTable.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue().getTable().toString())
        );
        tableColumnPrice.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(Config.getInstance().formatCurrency(param.getValue().getTotalPrice().doubleValue()))
        );
        tableColumnPaid.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue().getPaid() == 0 ? "No" : "Yes")
        );
        tableColumnDateTime.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue().getCreatedAt().toLocalDateTime().format(dateTimeFormat))
        );

        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        labelPaid.setGraphic(fa.create(FontAwesome.Glyph.CIRCLE_THIN));

        tableViewProducts.setItems(listPPFO);
        tableViewProducts.setCategoryVisibile(true);

        tableViewInvoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedInvoice) -> {
            if(selectedInvoice == null) {
                labelInvoiceID.setText("-");
                labelTableID.setText("-");
                labelWaiter.setText("-");
                labelPaid.setGraphic(fa.create(FontAwesome.Glyph.CIRCLE_THIN));
                listPPFO.clear();
                vboxDetail.setDisable(true);
            } else {
                if(selectedInvoice.getPaid() == 1)
                    labelPaid.setGraphic(fa.create(FontAwesome.Glyph.CHECK_CIRCLE));
                else
                    labelPaid.setGraphic(fa.create(FontAwesome.Glyph.CIRCLE_THIN));
                labelInvoiceID.setText(selectedInvoice.getId().toString());
                labelTableID.setText(selectedInvoice.getTable().toString());
                labelWaiter.setText(selectedInvoice.getWaiters().stream().collect(Collectors.joining(", ")));
                listPPFO.setAll(
                        Order.combineOrders(
                                selectedInvoice.getOrders()
                        )
                );
                vboxDetail.setDisable(false);
            }
        });

        loadData();
    }

    private void loadData() {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        MessageGetQuery<Invoice> message = new MessageGetQuery<>(Invoice.class, new Query(
                QueryType.Invoices_Between_Dates,
                new QueryParam("from", LocalDate.class, datePickerFrom.getValue()),
                new QueryParam("until", LocalDate.class, datePickerUntil.getValue())
        ));
        connectionManager.sendWithAction(message, text -> {
            try {
                tableViewInvoice.setItems(FXCollections.observableList(new Message<Invoice>(text).getPayload()));
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }

    public void datePickerFromAction() {
        LocalDate from = datePickerFrom.getValue();
        if(datePickerUntil.getValue().minusDays(7).isAfter(from)) {
            datePickerUntil.setValue(from.plusDays(7));
            loadData();
        }
    }

    public void datePickerUntilAction() {
        LocalDate until = datePickerUntil.getValue();
        LocalDate from = datePickerFrom.getValue();
        if(from.isAfter(until) || from.plusDays(7).isBefore(until)) {
            datePickerFrom.setValue(until.minusDays(7));
            loadData();
        }
    }

    public void buttonPrintInvoiceAction() {
        Invoice invoice = tableViewInvoice.getSelectionModel().getSelectedItem();
        if(invoice != null) {
            try {
                PrintHelper.printInvoice(invoice, owner);
            } catch (IOException e) {
                ExceptionDialog exceptionDialog = new ExceptionDialog(e);
                exceptionDialog.initOwner(owner);
                exceptionDialog.show();
            }
        }
    }

    public void setOwner(Window owner) {
        this.owner = owner;
    }
}
