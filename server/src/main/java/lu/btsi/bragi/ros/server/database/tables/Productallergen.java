/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.server.database.tables;


import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import lu.btsi.bragi.ros.server.database.Keys;
import lu.btsi.bragi.ros.server.database.Ros;
import lu.btsi.bragi.ros.server.database.tables.records.ProductallergenRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
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
public class Productallergen extends TableImpl<ProductallergenRecord> {

    private static final long serialVersionUID = -1188075099;

    /**
     * The reference instance of <code>ros.ProductAllergen</code>
     */
    public static final Productallergen PRODUCTALLERGEN = new Productallergen();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProductallergenRecord> getRecordType() {
        return ProductallergenRecord.class;
    }

    /**
     * The column <code>ros.ProductAllergen.allergen_id</code>.
     */
    public final TableField<ProductallergenRecord, UInteger> ALLERGEN_ID = createField("allergen_id", org.jooq.impl.SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>ros.ProductAllergen.product_id</code>.
     */
    public final TableField<ProductallergenRecord, UInteger> PRODUCT_ID = createField("product_id", org.jooq.impl.SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>ros.ProductAllergen.created_at</code>.
     */
    public final TableField<ProductallergenRecord, Timestamp> CREATED_AT = createField("created_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>ros.ProductAllergen.updated_at</code>.
     */
    public final TableField<ProductallergenRecord, Timestamp> UPDATED_AT = createField("updated_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * Create a <code>ros.ProductAllergen</code> table reference
     */
    public Productallergen() {
        this("ProductAllergen", null);
    }

    /**
     * Create an aliased <code>ros.ProductAllergen</code> table reference
     */
    public Productallergen(String alias) {
        this(alias, PRODUCTALLERGEN);
    }

    private Productallergen(String alias, Table<ProductallergenRecord> aliased) {
        this(alias, aliased, null);
    }

    private Productallergen(String alias, Table<ProductallergenRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<ProductallergenRecord> getPrimaryKey() {
        return Keys.KEY_PRODUCTALLERGEN_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ProductallergenRecord>> getKeys() {
        return Arrays.<UniqueKey<ProductallergenRecord>>asList(Keys.KEY_PRODUCTALLERGEN_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<ProductallergenRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ProductallergenRecord, ?>>asList(Keys.FK_PRODUCTALLERGEN_ALLERGEN, Keys.FK_PRODUCTALLERGEN_PRODUCT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Productallergen as(String alias) {
        return new Productallergen(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Productallergen rename(String name) {
        return new Productallergen(name, null);
    }
}
