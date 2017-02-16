package lu.btsi.bragi.ros.server.controller;

import com.google.inject.Inject;
import lu.btsi.bragi.ros.models.message.Answer;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.server.database.tables.Table;
import lu.btsi.bragi.ros.server.database.tables.records.TableRecord;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;

import java.util.List;


/**
 * Created by gillesbraun on 15/02/2017.
 */
public class TableController extends Controller<lu.btsi.bragi.ros.models.Table> {
    @Inject
    DSLContext context;

    private static Class mapTo = lu.btsi.bragi.ros.models.Table.class;

    Table dbTable = Table.TABLE;

    public TableController() {
        super(mapTo);
    }

    @Override
    protected Message handleGet() {
        List list = context.fetch(dbTable).into(mapTo);
        return new Answer(list, mapTo);
    }

    @Override
    protected void handleUpdate(lu.btsi.bragi.ros.models.Table obj) {
        TableRecord tableRecord = new TableRecord();
        tableRecord.from(obj);
        UpdatableRecord u = tableRecord;
        context.attach(u);
        u.reset(Table.TABLE.UPDATED_AT);
        u.update();
    }

    @Override
    protected void handleCreate(lu.btsi.bragi.ros.models.Table obj) {
        TableRecord tableRecord = context.newRecord(Table.TABLE);
        tableRecord.from(obj);
        context.attach(tableRecord);
        tableRecord.store();
    }

    @Override
    protected void handleDelete(lu.btsi.bragi.ros.models.Table obj) {
        TableRecord tableRecord = new TableRecord();
        tableRecord.from(obj);
        context.executeDelete(tableRecord);
    }
}
