package lu.btsi.bragi.ros.client;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.converter.LocalDateStringConverter;
import lu.btsi.bragi.ros.client.connection.ConnectionManager;
import lu.btsi.bragi.ros.client.settings.Config;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.pojos.Language;
import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.ProductPriceForOrder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by gillesbraun on 22/03/2017.
 */
public class ArchiveOrdersController {
    public TableView<Order> tableView;
    public TableColumn<Order, String> tableColumnID, tableColumnTable, tableColumnWaiter, tableColumnDateTime;
    public DatePicker datePickerUntil, datePickerFrom;
    public Label labelOrderID;
    public ListView<ProductPriceForOrder> listViewProducts;
    private ObservableList<ProductPriceForOrder> listPPFO;

    public void initialize() {
        DateTimeFormatter dateFormat = Config.getInstance().getDateFormatter();

        datePickerFrom.setConverter(new LocalDateStringConverter(dateFormat, dateFormat));
        datePickerUntil.setConverter(new LocalDateStringConverter(dateFormat, dateFormat));
        datePickerFrom.setValue(LocalDate.now().minusDays(7));
        datePickerUntil.setValue(LocalDate.now());

        listViewProducts.setCellFactory(param -> new ListCell<ProductPriceForOrder>(){
            @Override
            protected void updateItem(ProductPriceForOrder ppfo, boolean empty) {
                if(ppfo == null) {
                    setText(null);
                } else {
                    Language language = Config.getInstance().generalSettings.getLanguage();
                    String qty = ppfo.getQuantity().toString();
                    String product = ppfo.getProduct().getProductInLanguage(language).getLabel();
                    String pricePer = Config.getInstance().formatCurrency(ppfo.getPricePerProduct().doubleValue());
                    String priceTotal = Config.getInstance().formatCurrency(ppfo.getPricePerProduct().doubleValue());

                    setText(String.format("%s x %s  \u00e0 %s = %s", qty, product, pricePer, priceTotal));
                }
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedOrder) -> {
            if(selectedOrder != null) {
                labelOrderID.setText(selectedOrder.getId().toString());
                listPPFO = FXCollections.observableList(selectedOrder.getProductPriceForOrder());
                listViewProducts.setItems(listPPFO);
            } else {
                listPPFO.clear();
                labelOrderID.setText("");
            }
        });

        loadData();
    }

    private void loadData() {
        DateTimeFormatter dateTimeFormat = Config.getInstance().getDateTimeFormatter();
        ConnectionManager conn = ConnectionManager.getInstance();
        conn.sendWithAction(new MessageGet<>(Order.class), t -> {
            try {
                List<Order> orders = new Message<Order>(t).getPayload();
                tableView.setItems(FXCollections.observableList(orders));
                tableColumnID.setCellValueFactory(param ->
                        new ReadOnlyObjectWrapper<>(param.getValue().getId().toString())
                );
                tableColumnTable.setCellValueFactory(param ->
                        new ReadOnlyObjectWrapper<>(param.getValue().getTable().getId().toString())
                );
                tableColumnWaiter.setCellValueFactory(param ->
                        new ReadOnlyObjectWrapper<>(param.getValue().getWaiter().getName())
                );
                tableColumnDateTime.setCellValueFactory(param ->
                        new ReadOnlyObjectWrapper<>(param.getValue().getCreatedAt().toLocalDateTime().format(dateTimeFormat))
                );
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }

    public void datePickerFromAction() {
        LocalDate from = datePickerFrom.getValue();
        if(datePickerUntil.getValue().minusDays(7).isAfter(from)) {
            datePickerUntil.setValue(from.plusDays(7));
        }
    }

    public void datePickerUntilAction() {
        LocalDate until = datePickerUntil.getValue();
        LocalDate from = datePickerFrom.getValue();
        if(from.isAfter(until) || from.plusDays(7).isBefore(until)) {
            datePickerFrom.setValue(until.minusDays(7));
        }

    }
}
