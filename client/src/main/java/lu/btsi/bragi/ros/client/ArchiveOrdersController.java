package lu.btsi.bragi.ros.client;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.util.converter.LocalDateStringConverter;
import lu.btsi.bragi.ros.client.connection.ConnectionManager;
import lu.btsi.bragi.ros.client.settings.Config;
import lu.btsi.bragi.ros.models.message.*;
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
    public TableViewProducts tableViewProducts;
    public VBox vboxDetail;
    private ObservableList<ProductPriceForOrder> listPPFO;

    public void initialize() {
        DateTimeFormatter dateFormat = Config.getInstance().getDateFormatter();
        DateTimeFormatter dateTimeFormat = Config.getInstance().getDateTimeFormatter();

        datePickerFrom.setConverter(new LocalDateStringConverter(dateFormat, dateFormat));
        datePickerUntil.setConverter(new LocalDateStringConverter(dateFormat, dateFormat));
        datePickerFrom.setValue(LocalDate.now().minusDays(7));
        datePickerUntil.setValue(LocalDate.now());

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedOrder) -> {
            if(selectedOrder != null) {
                labelOrderID.setText(selectedOrder.getId().toString());
                listPPFO = FXCollections.observableList(selectedOrder.getProductPriceForOrder());
                tableViewProducts.setItems(listPPFO);
                vboxDetail.setDisable(false);
            } else {
                vboxDetail.setDisable(true);
                listPPFO.clear();
                labelOrderID.setText("");
            }
        });

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

        loadData();
    }

    private void loadData() {
        ConnectionManager conn = ConnectionManager.getInstance();
        conn.sendWithAction(new MessageGetQuery<>(Order.class,
                new Query(QueryType.Orders_Between_Dates,
                        new QueryParam("from", LocalDate.class, datePickerFrom.getValue()),
                        new QueryParam("until", LocalDate.class, datePickerUntil.getValue()))), t -> {
            try {
                List<Order> orders = new Message<Order>(t).getPayload();
                tableView.setItems(FXCollections.observableList(orders));
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
}
