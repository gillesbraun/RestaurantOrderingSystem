/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.server.database.tables.records;


import java.sql.Timestamp;

import javax.annotation.Generated;

import lu.btsi.bragi.ros.server.database.tables.Productpricefororder;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.types.UInteger;


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
public class ProductpricefororderRecord extends UpdatableRecordImpl<ProductpricefororderRecord> implements Record5<UInteger, UInteger, Double, Timestamp, Timestamp> {

    private static final long serialVersionUID = -1211496815;

    /**
     * Setter for <code>ros.ProductPriceForOrder.product_id</code>.
     */
    public void setProductId(UInteger value) {
        set(0, value);
    }

    /**
     * Getter for <code>ros.ProductPriceForOrder.product_id</code>.
     */
    public UInteger getProductId() {
        return (UInteger) get(0);
    }

    /**
     * Setter for <code>ros.ProductPriceForOrder.order_id</code>.
     */
    public void setOrderId(UInteger value) {
        set(1, value);
    }

    /**
     * Getter for <code>ros.ProductPriceForOrder.order_id</code>.
     */
    public UInteger getOrderId() {
        return (UInteger) get(1);
    }

    /**
     * Setter for <code>ros.ProductPriceForOrder.price</code>.
     */
    public void setPrice(Double value) {
        set(2, value);
    }

    /**
     * Getter for <code>ros.ProductPriceForOrder.price</code>.
     */
    public Double getPrice() {
        return (Double) get(2);
    }

    /**
     * Setter for <code>ros.ProductPriceForOrder.created_at</code>.
     */
    public void setCreatedAt(Timestamp value) {
        set(3, value);
    }

    /**
     * Getter for <code>ros.ProductPriceForOrder.created_at</code>.
     */
    public Timestamp getCreatedAt() {
        return (Timestamp) get(3);
    }

    /**
     * Setter for <code>ros.ProductPriceForOrder.updated_at</code>.
     */
    public void setUpdatedAt(Timestamp value) {
        set(4, value);
    }

    /**
     * Getter for <code>ros.ProductPriceForOrder.updated_at</code>.
     */
    public Timestamp getUpdatedAt() {
        return (Timestamp) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record2<UInteger, UInteger> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<UInteger, UInteger, Double, Timestamp, Timestamp> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<UInteger, UInteger, Double, Timestamp, Timestamp> valuesRow() {
        return (Row5) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UInteger> field1() {
        return Productpricefororder.PRODUCTPRICEFORORDER.PRODUCT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UInteger> field2() {
        return Productpricefororder.PRODUCTPRICEFORORDER.ORDER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Double> field3() {
        return Productpricefororder.PRODUCTPRICEFORORDER.PRICE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field4() {
        return Productpricefororder.PRODUCTPRICEFORORDER.CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return Productpricefororder.PRODUCTPRICEFORORDER.UPDATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UInteger value1() {
        return getProductId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UInteger value2() {
        return getOrderId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double value3() {
        return getPrice();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value4() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value5() {
        return getUpdatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductpricefororderRecord value1(UInteger value) {
        setProductId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductpricefororderRecord value2(UInteger value) {
        setOrderId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductpricefororderRecord value3(Double value) {
        setPrice(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductpricefororderRecord value4(Timestamp value) {
        setCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductpricefororderRecord value5(Timestamp value) {
        setUpdatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductpricefororderRecord values(UInteger value1, UInteger value2, Double value3, Timestamp value4, Timestamp value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ProductpricefororderRecord
     */
    public ProductpricefororderRecord() {
        super(Productpricefororder.PRODUCTPRICEFORORDER);
    }

    /**
     * Create a detached, initialised ProductpricefororderRecord
     */
    public ProductpricefororderRecord(UInteger productId, UInteger orderId, Double price, Timestamp createdAt, Timestamp updatedAt) {
        super(Productpricefororder.PRODUCTPRICEFORORDER);

        set(0, productId);
        set(1, orderId);
        set(2, price);
        set(3, createdAt);
        set(4, updatedAt);
    }
}
