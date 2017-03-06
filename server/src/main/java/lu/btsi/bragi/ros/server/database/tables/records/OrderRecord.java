/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.server.database.tables.records;


import lu.btsi.bragi.ros.server.database.tables.Order;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.types.UInteger;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OrderRecord extends UpdatableRecordImpl<OrderRecord> implements Record8<UInteger, Byte, Byte, UInteger, UInteger, UInteger, Timestamp, Timestamp> {

    private static final long serialVersionUID = 847294972;

    /**
     * Setter for <code>ros.Order.id</code>.
     */
    public void setId(UInteger value) {
        set(0, value);
    }

    /**
     * Getter for <code>ros.Order.id</code>.
     */
    public UInteger getId() {
        return (UInteger) get(0);
    }

    /**
     * Setter for <code>ros.Order.processing</code>.
     */
    public void setProcessing(Byte value) {
        set(1, value);
    }

    /**
     * Getter for <code>ros.Order.processing</code>.
     */
    public Byte getProcessing() {
        return (Byte) get(1);
    }

    /**
     * Setter for <code>ros.Order.processing_done</code>.
     */
    public void setProcessingDone(Byte value) {
        set(2, value);
    }

    /**
     * Getter for <code>ros.Order.processing_done</code>.
     */
    public Byte getProcessingDone() {
        return (Byte) get(2);
    }

    /**
     * Setter for <code>ros.Order.table_id</code>.
     */
    public void setTableId(UInteger value) {
        set(3, value);
    }

    /**
     * Getter for <code>ros.Order.table_id</code>.
     */
    public UInteger getTableId() {
        return (UInteger) get(3);
    }

    /**
     * Setter for <code>ros.Order.waiter_id</code>.
     */
    public void setWaiterId(UInteger value) {
        set(4, value);
    }

    /**
     * Getter for <code>ros.Order.waiter_id</code>.
     */
    public UInteger getWaiterId() {
        return (UInteger) get(4);
    }

    /**
     * Setter for <code>ros.Order.invoice_id</code>.
     */
    public void setInvoiceId(UInteger value) {
        set(5, value);
    }

    /**
     * Getter for <code>ros.Order.invoice_id</code>.
     */
    public UInteger getInvoiceId() {
        return (UInteger) get(5);
    }

    /**
     * Setter for <code>ros.Order.created_at</code>.
     */
    public void setCreatedAt(Timestamp value) {
        set(6, value);
    }

    /**
     * Getter for <code>ros.Order.created_at</code>.
     */
    public Timestamp getCreatedAt() {
        return (Timestamp) get(6);
    }

    /**
     * Setter for <code>ros.Order.updated_at</code>.
     */
    public void setUpdatedAt(Timestamp value) {
        set(7, value);
    }

    /**
     * Getter for <code>ros.Order.updated_at</code>.
     */
    public Timestamp getUpdatedAt() {
        return (Timestamp) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<UInteger> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<UInteger, Byte, Byte, UInteger, UInteger, UInteger, Timestamp, Timestamp> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<UInteger, Byte, Byte, UInteger, UInteger, UInteger, Timestamp, Timestamp> valuesRow() {
        return (Row8) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UInteger> field1() {
        return Order.ORDER.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field2() {
        return Order.ORDER.PROCESSING;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field3() {
        return Order.ORDER.PROCESSING_DONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UInteger> field4() {
        return Order.ORDER.TABLE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UInteger> field5() {
        return Order.ORDER.WAITER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UInteger> field6() {
        return Order.ORDER.INVOICE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field7() {
        return Order.ORDER.CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field8() {
        return Order.ORDER.UPDATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UInteger value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value2() {
        return getProcessing();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value3() {
        return getProcessingDone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UInteger value4() {
        return getTableId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UInteger value5() {
        return getWaiterId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UInteger value6() {
        return getInvoiceId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value7() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value8() {
        return getUpdatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value1(UInteger value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value2(Byte value) {
        setProcessing(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value3(Byte value) {
        setProcessingDone(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value4(UInteger value) {
        setTableId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value5(UInteger value) {
        setWaiterId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value6(UInteger value) {
        setInvoiceId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value7(Timestamp value) {
        setCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value8(Timestamp value) {
        setUpdatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord values(UInteger value1, Byte value2, Byte value3, UInteger value4, UInteger value5, UInteger value6, Timestamp value7, Timestamp value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached OrderRecord
     */
    public OrderRecord() {
        super(Order.ORDER);
    }

    /**
     * Create a detached, initialised OrderRecord
     */
    public OrderRecord(UInteger id, Byte processing, Byte processingDone, UInteger tableId, UInteger waiterId, UInteger invoiceId, Timestamp createdAt, Timestamp updatedAt) {
        super(Order.ORDER);

        set(0, id);
        set(1, processing);
        set(2, processingDone);
        set(3, tableId);
        set(4, waiterId);
        set(5, invoiceId);
        set(6, createdAt);
        set(7, updatedAt);
    }
}
