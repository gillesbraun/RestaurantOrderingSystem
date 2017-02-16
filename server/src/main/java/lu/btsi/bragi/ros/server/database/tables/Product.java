/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.server.database.tables;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import lu.btsi.bragi.ros.server.database.Keys;
import lu.btsi.bragi.ros.server.database.Ros;
import lu.btsi.bragi.ros.server.database.tables.records.ProductRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
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
public class Product extends TableImpl<ProductRecord> {

    private static final long serialVersionUID = -578785048;

    /**
     * The reference instance of <code>ros.Product</code>
     */
    public static final Product PRODUCT = new Product();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProductRecord> getRecordType() {
        return ProductRecord.class;
    }

    /**
     * The column <code>ros.Product.id</code>.
     */
    public final TableField<ProductRecord, UInteger> ID = createField("id", org.jooq.impl.SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>ros.Product.price</code>.
     */
    public final TableField<ProductRecord, BigDecimal> PRICE = createField("price", org.jooq.impl.SQLDataType.DECIMAL.precision(10, 2).nullable(false), this, "");

    /**
     * The column <code>ros.Product.product_category_id</code>.
     */
    public final TableField<ProductRecord, UInteger> PRODUCT_CATEGORY_ID = createField("product_category_id", org.jooq.impl.SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>ros.Product.created_at</code>.
     */
    public final TableField<ProductRecord, Timestamp> CREATED_AT = createField("created_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>ros.Product.updated_at</code>.
     */
    public final TableField<ProductRecord, Timestamp> UPDATED_AT = createField("updated_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * Create a <code>ros.Product</code> table reference
     */
    public Product() {
        this("Product", null);
    }

    /**
     * Create an aliased <code>ros.Product</code> table reference
     */
    public Product(String alias) {
        this(alias, PRODUCT);
    }

    private Product(String alias, Table<ProductRecord> aliased) {
        this(alias, aliased, null);
    }

    private Product(String alias, Table<ProductRecord> aliased, Field<?>[] parameters) {
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
    public Identity<ProductRecord, UInteger> getIdentity() {
        return Keys.IDENTITY_PRODUCT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<ProductRecord> getPrimaryKey() {
        return Keys.KEY_PRODUCT_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ProductRecord>> getKeys() {
        return Arrays.<UniqueKey<ProductRecord>>asList(Keys.KEY_PRODUCT_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<ProductRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ProductRecord, ?>>asList(Keys.FK_PRODUCT_PRODUCTCATEGORY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product as(String alias) {
        return new Product(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Product rename(String name) {
        return new Product(name, null);
    }
}
