package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageType;
import lu.btsi.bragi.ros.models.message.Query;
import lu.btsi.bragi.ros.models.message.QueryType;
import lu.btsi.bragi.ros.models.pojos.Invoice;
import lu.btsi.bragi.ros.server.database.tables.records.InvoiceRecord;
import org.jooq.impl.DSL;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Created by gillesbraun on 09/03/2017.
 */
public class InvoiceController extends Controller<Invoice> {

    private static final Class<Invoice> pojo = Invoice.class;
    private static final lu.btsi.bragi.ros.server.database.tables.Invoice dbTable =
            lu.btsi.bragi.ros.server.database.tables.Invoice.INVOICE;

    public InvoiceController() {
        super(pojo);
        registerCustomQueryHandler(QueryType.Invoices_Between_Dates, getInvoicesBetweenDates);
    }

    private final Function<Query, List<Invoice>> getInvoicesBetweenDates = query -> {
        Date from = Date.valueOf(query.getParam("from", LocalDate.class));
        Date until = Date.valueOf(query.getParam("until", LocalDate.class));
        List<Invoice> invoices = context.select().from(dbTable)
                .where(DSL.date(dbTable.CREATED_AT).between(from, until))
                .fetchInto(pojo);
        return invoices.stream().map(this::fetchReferences).collect(toList());
    };

    private Invoice fetchReferences(Invoice invoice) {
        invoice.setOrders(getController(OrderController.class).getOrders(invoice));
        return invoice;
    }

    @Override
    protected List<Invoice> handleGet() throws Exception {
        List<Invoice> invoices = context.fetch(dbTable).into(pojo);
        invoices = invoices.stream().map(this::fetchReferences).collect(toList());
        return invoices;
    }

    @Override
    protected void handleUpdate(Invoice obj) throws Exception {
        InvoiceRecord invoiceRecord = new InvoiceRecord();
        invoiceRecord.from(obj);
        invoiceRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(invoiceRecord);
    }

    @Override
    protected void handleCreate(Invoice obj, Message<Invoice> originalMessage) throws Exception {
        InvoiceRecord invoiceRecord = new InvoiceRecord();
        invoiceRecord.from(obj);

        InvoiceRecord afterInsert = context.insertInto(dbTable)
                .set(dbTable.PAID, obj.getPaid())
                .returning(dbTable.fields())
                .fetchOne();

        obj.getOrders().forEach(order -> {
            getController(OrderController.class).updateInvoice(order, afterInsert.getId());
        });
        Invoice after = fetchReferences(afterInsert.into(pojo));
        messageSender.sendReply(originalMessage.createAnswer(after));
        messageSender.broadcast(new Message<>(MessageType.Broadcast, Collections.emptyList(), pojo));
    }

    @Override
    protected void handleCreate(Invoice obj) throws Exception {
        // do nothing here
    }

    @Override
    protected void handleDelete(Invoice obj) throws Exception {
        InvoiceRecord invoiceRecord = new InvoiceRecord();
        invoiceRecord.from(obj);
        context.executeDelete(invoiceRecord);
    }
}
