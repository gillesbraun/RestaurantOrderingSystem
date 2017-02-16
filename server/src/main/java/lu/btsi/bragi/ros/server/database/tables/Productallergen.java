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
import lu.btsi.bragi.ros.server.database.tables.records.ProductAllergenRecord;

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
public class ProductAllergen extends TableImpl<ProductAllergenRecord> {

    private static final long serialVersionUID = 817943785;

    /**
     * The reference instance of <code>ros.Product_Allergen</code>
     */
    public static final ProductAllergen PRODUCT_ALLERGEN = new ProductAllergen();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProductAllergenRecord> getRecordType() {
        return ProductAllergenRecord.class;
    }

    /**
     * The column <code>ros.Product_Allergen.allergen_id</code>.
     */
    public final TableField<ProductAllergenRecord, UInteger> ALLERGEN_ID = createField("allergen_id", org.jooq.impl.SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>ros.Product_Allergen.product_id</code>.
     */
    public final TableField<ProductAllergenRecord, UInteger> PRODUCT_ID = createField("product_id", org.jooq.impl.SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>ros.Product_Allergen.created_at</code>.
     */
    public final TableField<ProductAllergenRecord, Timestamp> CREATED_AT = createField("created_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>ros.Product_Allergen.updated_at</code>.
     */
    public final TableField<ProductAllergenRecord, Timestamp> UPDATED_AT = createField("updated_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * Create a <code>ros.Product_Allergen</code> table reference
     */
    public ProductAllergen() {
        this("Product_Allergen", null);
    }

    /**
     * Create an aliased <code>ros.Product_Allergen</code> table reference
     */
    public ProductAllergen(String alias) {
        this(alias, PRODUCT_ALLERGEN);
    }

    private ProductAllergen(String alias, Table<ProductAllergenRecord> aliased) {
        this(alias, aliased, null);
    }

    private ProductAllergen(String alias, Table<ProductAllergenRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<ProductAllergenRecord> getPrimaryKey() {
        return Keys.KEY_PRODUCT_ALLERGEN_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ProductAllergenRecord>> getKeys() {
        return Arrays.<UniqueKey<ProductAllergenRecord>>asList(Keys.KEY_PRODUCT_ALLERGEN_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<ProductAllergenRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ProductAllergenRecord, ?>>asList(Keys.FK_PRODUCTALLERGEN_ALLERGEN, Keys.FK_PRODUCTALLERGEN_PRODUCT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductAllergen as(String alias) {
        return new ProductAllergen(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ProductAllergen rename(String name) {
        return new ProductAllergen(name, null);
    }
}
