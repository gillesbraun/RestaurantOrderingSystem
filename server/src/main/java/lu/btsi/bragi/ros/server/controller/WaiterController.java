package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.Waiter;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.WaiterRecord;

import java.util.List;

/**
 * Created by gillesbraun on 17/02/2017.
 */
public class WaiterController extends Controller<Waiter> {

    private static Class mapTo = Waiter.class;

    lu.btsi.bragi.ros.server.database.tables.Waiter dbTable = Tables.WAITER;


    public WaiterController() {
        super(Waiter.class);
    }

    @Override
    protected List<Waiter> handleGet() {
        List<Waiter> list = context.fetch(dbTable).into(mapTo);
        return list;
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
