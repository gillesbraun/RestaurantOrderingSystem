package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.pojos.Invoice;
import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.ProductPriceForOrder;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.OrderRecord;
import org.jooq.types.UInteger;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by gillesbraun on 06/03/2017.
 */
public class OrderController extends Controller<Order> {

    private static final Class<Order> pojo = Order.class;
    private static final lu.btsi.bragi.ros.server.database.tables.Order dbTable = Tables.ORDER;

    public OrderController() {
        super(pojo);
    }

    @Override
    protected List<Order> handleGet() throws Exception {
        List<Order> orders = context.fetch(dbTable).into(pojo);
        orders.stream().map(this::fetchReferences).collect(toList());
        return orders;
    }

    @Override
    protected void handleUpdate(Order obj) throws Exception {
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.from(obj);
        orderRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(orderRecord);
    }

    @Override
    protected void handleCreate(Order obj, Message<Order> originalMessage) throws Exception {
        OrderRecord record = new OrderRecord();
        record.from(obj);
        OrderRecord newId = context.insertInto(dbTable)
                .set(dbTable.TABLE_ID, obj.getTable().getId())
                .set(dbTable.WAITER_ID, obj.getWaiter().getId())
                .returning(dbTable.ID)
                .fetchOne();

        for (ProductPriceForOrder productPriceForOrder : obj.getProductPriceForOrder()) {
            productPriceForOrder.setOrderId(newId.getId());
            getController(ProductPriceForOrderController.class).handleCreate(productPriceForOrder);
        }
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
