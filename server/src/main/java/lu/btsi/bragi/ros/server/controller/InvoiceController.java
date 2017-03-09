package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.Invoice;
import lu.btsi.bragi.ros.server.database.tables.records.InvoiceRecord;

import java.util.List;

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
    }

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
    protected void handleCreate(Invoice obj) throws Exception {
        InvoiceRecord invoiceRecord = new InvoiceRecord();
        invoiceRecord.from(obj);

        InvoiceRecord afterInsert = context.insertInto(dbTable)
                .set(dbTable.PAID, obj.getPaid())
                .returning(dbTable.ID)
                .fetchOne();

        obj.getOrders().forEach(order -> {
            getController(OrderController.class).updateInvoice(order, afterInsert.getId());
        });
    }

    @Override
    protected void handleDelete(Invoice obj) throws Exception {
        InvoiceRecord invoiceRecord = new InvoiceRecord();
        invoiceRecord.from(obj);
        context.executeDelete(invoiceRecord);
    }
}
