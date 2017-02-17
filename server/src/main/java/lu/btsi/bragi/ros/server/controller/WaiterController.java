package lu.btsi.bragi.ros.server.controller;

import com.google.inject.Inject;
import lu.btsi.bragi.ros.models.message.Answer;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.pojos.Waiter;
import org.jooq.DSLContext;

import java.util.List;

/**
 * Created by gillesbraun on 17/02/2017.
 */
public class WaiterController extends Controller<Waiter> {

    private static Class mapTo = Waiter.class;

    lu.btsi.bragi.ros.server.database.tables.Waiter dbTable = lu.btsi.bragi.ros.server.database.tables.Waiter.WAITER;


    public WaiterController() {
        super(Waiter.class);
    }

    @Override
    protected Message handleGet() {
        List into = context.fetch(dbTable).into(mapTo);
        return new Answer(into, mapTo);
    }

    @Override
    protected void handleUpdate(Waiter obj) {

    }

    @Override
    protected void handleCreate(Waiter obj) {

    }

    @Override
    protected void handleDelete(Waiter obj) {

    }
}
