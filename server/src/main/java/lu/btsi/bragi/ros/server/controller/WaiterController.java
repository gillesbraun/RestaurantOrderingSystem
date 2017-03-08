package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.Waiter;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.WaiterRecord;

import java.util.List;

/**
 * Created by gillesbraun on 17/02/2017.
 */
public class WaiterController extends Controller<Waiter> {

    private static final Class<Waiter> pojo = Waiter.class;
    private static final lu.btsi.bragi.ros.server.database.tables.Waiter dbTable = Tables.WAITER;


    public WaiterController() {
        super(Waiter.class);
    }

    @Override
    protected List<Waiter> handleGet() {
        List<Waiter> list = context.fetch(dbTable).into(pojo);
        return list;
    }

    Waiter getWaiterForOrder(Order order) {
        return context.select()
                .from(dbTable)
                .where(dbTable.ID.eq(order.getWaiterId()))
                .fetchOne()
                .into(pojo);
    }

    @Override
    protected void handleUpdate(Waiter obj) {
        WaiterRecord waiterRecord = new WaiterRecord();
        waiterRecord.from(obj);
        waiterRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(waiterRecord);
    }

    @Override
    protected void handleCreate(Waiter obj) {
        WaiterRecord waiterRecord = new WaiterRecord();
        waiterRecord.from(obj);
        context.executeInsert(waiterRecord);
    }

    @Override
    protected void handleDelete(Waiter obj) {
        WaiterRecord waiterRecord = new WaiterRecord();
        waiterRecord.from(obj);
        context.executeDelete(waiterRecord);
    }
}
