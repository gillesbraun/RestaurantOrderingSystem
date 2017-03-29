package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageType;
import lu.btsi.bragi.ros.models.message.Query;
import lu.btsi.bragi.ros.models.message.QueryType;
import lu.btsi.bragi.ros.models.pojos.Invoice;
import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.ProductPriceForOrder;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.OrderRecord;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Created by gillesbraun on 06/03/2017.
 */
public class OrderController extends Controller<Order> {

    private static final Class<Order> pojo = Order.class;
    private static final lu.btsi.bragi.ros.server.database.tables.Order dbTable = Tables.ORDER;

    public OrderController() {
        super(pojo);
        registerCustomQueryHandler(QueryType.Open_Orders, handleOpenOrders);
        registerCustomQueryHandler(QueryType.Open_Orders_For_Location, handleOpenOrdersForLocation);
        registerCustomQueryHandler(QueryType.Open_Invoices, handleOpenInvoices);
        registerCustomQueryHandler(QueryType.Orders_Between_Dates, getOrdersBetweenDates);
    }

    Function<Query, List<Order>> handleOpenOrders = (ignore) -> {
        List<Order> orders = context.select()
                .from(dbTable)
                .where(dbTable.INVOICE_ID.isNull())
                .and(dbTable.PROCESSING_DONE.eq((byte)0))
                .and(DSL.date(dbTable.CREATED_AT).eq(DSL.currentDate()))
                .orderBy(dbTable.CREATED_AT.desc())
                .fetchInto(pojo);
        orders = orders.stream().map(this::fetchReferences).collect(toList());
        return orders;
    };

    Function<Query, List<Order>> handleOpenOrdersForLocation = (query) -> {
        UInteger locationID = query.getParam("location", UInteger.class);
        List<Order> orders = context.selectDistinct(dbTable.fields())
                .from(dbTable)
                .join(Tables.PRODUCT_PRICE_FOR_ORDER)
                    .on(dbTable.ID.eq(Tables.PRODUCT_PRICE_FOR_ORDER.ORDER_ID))
                .join(Tables.PRODUCT)
                    .on(Tables.PRODUCT.ID.eq(Tables.PRODUCT_PRICE_FOR_ORDER.PRODUCT_ID))
                .join(Tables.PRODUCT_CATEGORY)
                    .on(Tables.PRODUCT_CATEGORY.ID.eq(Tables.PRODUCT.PRODUCT_CATEGORY_ID))
                .where(
                        Tables.PRODUCT.LOCATION_ID.eq(locationID)
                        .and(Tables.PRODUCT_CATEGORY.LOCATION_ID.ne(locationID))
                        .or(
                                Tables.PRODUCT.LOCATION_ID.isNull().and(
                                        Tables.PRODUCT_CATEGORY.LOCATION_ID.eq(locationID))
                        )
                )
                .and(DSL.date(dbTable.CREATED_AT).eq(DSL.currentDate()))
                .and(dbTable.PROCESSING_DONE.eq((byte)0))
                .and(dbTable.INVOICE_ID.isNull())
                .orderBy(dbTable.CREATED_AT.desc())
                .fetchInto(pojo);
        orders = orders.stream().map(this::fetchReferences).collect(toList());
        return orders;
    };

    private final Function<Query, List<Order>> handleOpenInvoices = ignore -> {
        List<Order> orders = context.select()
                .from(dbTable)
                .where(dbTable.INVOICE_ID.isNull())
                .orderBy(dbTable.CREATED_AT.desc())
                .fetchInto(pojo);
        orders = orders.stream().map(this::fetchReferences).collect(toList());
        return orders;
    };

    private final Function<Query, List<Order>> getOrdersBetweenDates = query -> {
        Date from = Date.valueOf(query.getParam("from", LocalDate.class));
        Date until = Date.valueOf(query.getParam("until", LocalDate.class));
        List<Order> orders = context.select()
                .from(dbTable)
                .where(DSL.date(dbTable.CREATED_AT).between(from, until))
                .orderBy(dbTable.CREATED_AT.desc())
                .fetchInto(pojo);
        orders = orders.stream().map(this::fetchReferences).collect(toList());
        return orders;
    };

    @Override
    protected List<Order> handleGet() throws Exception {
        List<Order> orders = context.fetch(dbTable).into(pojo);
        orders = orders.stream().map(this::fetchReferences).collect(toList());
        return orders;
    }

    @Override
    protected void handleUpdate(Order obj) throws Exception {
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.from(obj);
        orderRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(orderRecord);
        messageSender.broadcast(new Message<>(MessageType.Broadcast, Collections.emptyList(), Order.class));
    }

    @Override
    protected void handleCreate(Order obj, Message<Order> originalMessage) throws Exception {
        // Grouping the added products by location ID
        Map<UInteger, List<ProductPriceForOrder>> perLocation = obj.getProductPriceForOrder().stream()
                .collect(groupingBy(
                        ppfo ->
                                ppfo.getProduct().getLocationId() == null
                                        ? ppfo.getProduct().getProductCategory().getLocationId()
                                        : ppfo.getProduct().getLocationId()
                ));
        // Create an order for each location
        for (Map.Entry<UInteger, List<ProductPriceForOrder>> entry : perLocation.entrySet()) {
            List<ProductPriceForOrder> productsForLocation = entry.getValue();
            OrderRecord record = new OrderRecord();
            record.from(obj);
            OrderRecord newId = context.insertInto(dbTable)
                    .set(dbTable.TABLE_ID, obj.getTable().getId())
                    .set(dbTable.WAITER_ID, obj.getWaiter().getId())
                    .returning(dbTable.ID)
                    .fetchOne();

            for (ProductPriceForOrder productPriceForOrder : productsForLocation) {
                productPriceForOrder.setOrderId(newId.getId());
                getController(ProductPriceForOrderController.class).handleCreate(productPriceForOrder);
            }
        }
        messageSender.broadcast(new Message<>(MessageType.Broadcast, Collections.emptyList(), Order.class));
    }

    @Override
    protected void handleCreate(Order obj) throws Exception {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void handleDelete(Order obj) throws Exception {
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.from(obj);
        context.executeDelete(orderRecord);
    }

    private Order fetchReferences(Order order) {
        order.setWaiter(getController(WaiterController.class).getWaiterForOrder(order));
        order.setProductPriceForOrder(getController(ProductPriceForOrderController.class).getProductPriceForOrderForOrder(order));
        order.setTable(getController(TableController.class).getTableForOrder(order));
        return order;
    }

    List<Order> getOrders(Invoice invoice) {
        List<Order> orders = context.select()
                .from(dbTable)
                .where(dbTable.INVOICE_ID.equal(invoice.getId()))
                .fetch()
                .into(pojo);
        orders = orders.stream().map(this::fetchReferences).collect(toList());
        return orders;
    }

    void updateInvoice(Order order, UInteger id) {
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.from(order);
        orderRecord.reset(dbTable.UPDATED_AT);
        orderRecord.setInvoiceId(id);
        context.executeUpdate(orderRecord);
    }
}
