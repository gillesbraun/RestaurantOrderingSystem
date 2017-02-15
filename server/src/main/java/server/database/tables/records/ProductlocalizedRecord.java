/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.server.database.tables.records;


import java.sql.Timestamp;

import javax.annotation.Generated;

import lu.btsi.bragi.ros.server.database.tables.Productlocalized;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.types.UInteger;


/**
 * This class is database by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.1"
    },
    comments = "This class is database by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ProductlocalizedRecord extends UpdatableRecordImpl<ProductlocalizedRecord> implements Record5<UInteger, String, String, Timestamp, Timestamp> {

    private static final long serialVersionUID = 1179621073;

    /**
     * Setter for <code>ros.ProductLocalized.product_id</code>.
     */
    public void setProductId(UInteger value) {
        set(0, value);
    }

    /**
     * Getter for <code>ros.ProductLocalized.product_id</code>.
     */
    public UInteger getProductId() {
        return (UInteger) get(0);
    }

    /**
     * Setter for <code>ros.ProductLocalized.language_code</code>.
     */
    public void setLanguageCode(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>ros.ProductLocalized.language_code</code>.
     */
    public String getLanguageCode() {
        return (String) get(1);
    }

    /**
     * Setter for <code>ros.ProductLocalized.label</code>.
     */
    public void setLabel(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>ros.ProductLocalized.label</code>.
     */
    public String getLabel() {
        return (String) get(2);
    }

    /**
     * Setter for <code>ros.ProductLocalized.created_at</code>.
     */
    public void setCreatedAt(Timestamp value) {
        set(3, value);
    }

    /**
     * Getter for <code>ros.ProductLocalized.created_at</code>.
     */
    public Timestamp getCreatedAt() {
        return (Timestamp) get(3);
    }

    /**
     * Setter for <code>ros.ProductLocalized.updated_at</code>.
     */
    public void setUpdatedAt(Timestamp value) {
        set(4, value);
    }

    /**
     * Getter for <code>ros.ProductLocalized.updated_at</code>.
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
    public Record2<UInteger, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<UInteger, String, String, Timestamp, Timestamp> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<UInteger, String, String, Timestamp, Timestamp> valuesRow() {
        return (Row5) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UInteger> field1() {
        return Productlocalized.PRODUCTLOCALIZED.PRODUCT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Productlocalized.PRODUCTLOCALIZED.LANGUAGE_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Productlocalized.PRODUCTLOCALIZED.LABEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field4() {
        return Productlocalized.PRODUCTLOCALIZED.CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return Productlocalized.PRODUCTLOCALIZED.UPDATED_AT;
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
    public String value2() {
        return getLanguageCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getLabel();
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
    public ProductlocalizedRecord value1(UInteger value) {
        setProductId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductlocalizedRecord value2(String value) {
        setLanguageCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductlocalizedRecord value3(String value) {
        setLabel(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductlocalizedRecord value4(Timestamp value) {
        setCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductlocalizedRecord value5(Timestamp value) {
        setUpdatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductlocalizedRecord values(UInteger value1, String value2, String value3, Timestamp value4, Timestamp value5) {
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
     * Create a detached ProductlocalizedRecord
     */
    public ProductlocalizedRecord() {
        super(Productlocalized.PRODUCTLOCALIZED);
    }

    /**
     * Create a detached, initialised ProductlocalizedRecord
     */
    public ProductlocalizedRecord(UInteger productId, String languageCode, String label, Timestamp createdAt, Timestamp updatedAt) {
        super(Productlocalized.PRODUCTLOCALIZED);

        set(0, productId);
        set(1, languageCode);
        set(2, label);
        set(3, createdAt);
        set(4, updatedAt);
    }
}
