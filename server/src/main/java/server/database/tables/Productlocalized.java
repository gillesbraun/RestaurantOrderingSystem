/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.server.database.tables;


import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.jooq.types.UInteger;

import lu.btsi.bragi.ros.server.database.Keys;
import lu.btsi.bragi.ros.server.database.Ros;
import lu.btsi.bragi.ros.server.database.tables.records.ProductlocalizedRecord;


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
public class Productlocalized extends TableImpl<ProductlocalizedRecord> {

    private static final long serialVersionUID = 624253291;

    /**
     * The reference instance of <code>ros.ProductLocalized</code>
     */
    public static final Productlocalized PRODUCTLOCALIZED = new Productlocalized();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProductlocalizedRecord> getRecordType() {
        return ProductlocalizedRecord.class;
    }

    /**
     * The column <code>ros.ProductLocalized.product_id</code>.
     */
    public final TableField<ProductlocalizedRecord, UInteger> PRODUCT_ID = createField("product_id", org.jooq.impl.SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>ros.ProductLocalized.language_code</code>.
     */
    public final TableField<ProductlocalizedRecord, String> LANGUAGE_CODE = createField("language_code", org.jooq.impl.SQLDataType.CHAR.length(2).nullable(false), this, "");

    /**
     * The column <code>ros.ProductLocalized.label</code>.
     */
    public final TableField<ProductlocalizedRecord, String> LABEL = createField("label", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

    /**
     * The column <code>ros.ProductLocalized.created_at</code>.
     */
    public final TableField<ProductlocalizedRecord, Timestamp> CREATED_AT = createField("created_at", org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>ros.ProductLocalized.updated_at</code>.
     */
    public final TableField<ProductlocalizedRecord, Timestamp> UPDATED_AT = createField("updated_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

    /**
     * Create a <code>ros.ProductLocalized</code> table reference
     */
    public Productlocalized() {
        this("ProductLocalized", null);
    }

    /**
     * Create an aliased <code>ros.ProductLocalized</code> table reference
     */
    public Productlocalized(String alias) {
        this(alias, PRODUCTLOCALIZED);
    }

    private Productlocalized(String alias, Table<ProductlocalizedRecord> aliased) {
        this(alias, aliased, null);
    }

    private Productlocalized(String alias, Table<ProductlocalizedRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Ros.ROS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<ProductlocalizedRecord> getPrimaryKey() {
        return Keys.KEY_PRODUCTLOCALIZED_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ProductlocalizedRecord>> getKeys() {
        return Arrays.<UniqueKey<ProductlocalizedRecord>>asList(Keys.KEY_PRODUCTLOCALIZED_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<ProductlocalizedRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ProductlocalizedRecord, ?>>asList(Keys.FK_PRODUCTLOCALIZED_PRODUCT, Keys.FK_PRODUCTLOCALIZED_LANGUAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Productlocalized as(String alias) {
        return new Productlocalized(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Productlocalized rename(String name) {
        return new Productlocalized(name, null);
    }
}
