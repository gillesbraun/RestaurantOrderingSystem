/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.server.database.tables.records;


import java.sql.Timestamp;

import javax.annotation.Generated;

import lu.btsi.bragi.ros.server.database.tables.Allergen;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
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
public class AllergenRecord extends UpdatableRecordImpl<AllergenRecord> implements Record3<UInteger, Timestamp, Timestamp> {

    private static final long serialVersionUID = -727730980;

    /**
     * Setter for <code>ros.Allergen.id</code>.
     */
    public void setId(UInteger value) {
        set(0, value);
    }

    /**
     * Getter for <code>ros.Allergen.id</code>.
     */
    public UInteger getId() {
        return (UInteger) get(0);
    }

    /**
     * Setter for <code>ros.Allergen.created_at</code>.
     */
    public void setCreatedAt(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>ros.Allergen.created_at</code>.
     */
    public Timestamp getCreatedAt() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>ros.Allergen.updated_at</code>.
     */
    public void setUpdatedAt(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>ros.Allergen.updated_at</code>.
     */
    public Timestamp getUpdatedAt() {
        return (Timestamp) get(2);
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
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<UInteger, Timestamp, Timestamp> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<UInteger, Timestamp, Timestamp> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UInteger> field1() {
        return Allergen.ALLERGEN.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field2() {
        return Allergen.ALLERGEN.CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field3() {
        return Allergen.ALLERGEN.UPDATED_AT;
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
    public Timestamp value2() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value3() {
        return getUpdatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AllergenRecord value1(UInteger value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AllergenRecord value2(Timestamp value) {
        setCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AllergenRecord value3(Timestamp value) {
        setUpdatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AllergenRecord values(UInteger value1, Timestamp value2, Timestamp value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AllergenRecord
     */
    public AllergenRecord() {
        super(Allergen.ALLERGEN);
    }

    /**
     * Create a detached, initialised AllergenRecord
     */
    public AllergenRecord(UInteger id, Timestamp createdAt, Timestamp updatedAt) {
        super(Allergen.ALLERGEN);

        set(0, id);
        set(1, createdAt);
        set(2, updatedAt);
    }
}
