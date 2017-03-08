package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.OrderRecord;

import java.util.Arrays;
import java.util.List;

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
        List<Order> into = context.fetch(dbTable).into(pojo);
        into.forEach(order -> {
            order.setWaiter(getController(WaiterController.class).getWaiterForOrder(order));
            order.setProductPriceForOrder(getController(ProductPriceForOrderController.class).getProductPriceForOrderForOrder(order));
            order.setTable(getController(TableController.class).getTableForOrder(order));
        });
        return into;
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
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.from(obj);
        OrderRecord id = context.insertInto(dbTable).set(orderRecord).returning(dbTable.ID).fetchOne();
        Order into = context.selectFrom(dbTable).where(dbTable.ID.equal(id.getId())).fetchOne().into(pojo);
        messageSender.sendReply(originalMessage.createAnswer(Arrays.asList(into)));
        //messageSender.messageSender(new Message<>(MessageType.Broadcast, into, Order.class));
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
}
