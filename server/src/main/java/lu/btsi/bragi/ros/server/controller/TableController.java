package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.Table;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.TableRecord;

import java.util.List;


/**
 * Created by gillesbraun on 15/02/2017.
 */
public class TableController extends Controller<Table> {

    private static final Class<Table> pojo = Table.class;
    private static final lu.btsi.bragi.ros.server.database.tables.Table dbTable = Tables.TABLE;

    public TableController() {
        super(pojo);
    }

    @Override
    protected List<Table> handleGet() {
        List<Table> list = context.fetch(dbTable).into(pojo);
        return list;
    }

    Table getTableForOrder(Order order) {
        return context.select()
                .from(dbTable)
                .where(dbTable.ID.equal(order.getTableId()))
                .fetchOne()
                .into(pojo);
    }

    @Override
    protected void handleUpdate(Table obj) {
        TableRecord tableRecord = new TableRecord();
        tableRecord.from(obj);
        tableRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(tableRecord);
    }

    @Override
    protected void handleCreate(Table obj) {
        TableRecord tableRecord = context.newRecord(dbTable.TABLE);
        tableRecord.from(obj);
        context.executeInsert(tableRecord);
    }

    @Override
    protected void handleDelete(Table obj) {
        TableRecord tableRecord = new TableRecord();
        tableRecord.from(obj);
        context.executeDelete(tableRecord);
    }
}
