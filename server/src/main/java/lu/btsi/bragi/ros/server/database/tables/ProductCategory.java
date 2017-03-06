/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.server.database.tables;


import lu.btsi.bragi.ros.server.database.Keys;
import lu.btsi.bragi.ros.server.database.Ros;
import lu.btsi.bragi.ros.server.database.tables.records.ProductCategoryRecord;
import org.jooq.*;
import org.jooq.Table;
import org.jooq.impl.TableImpl;
import org.jooq.types.UInteger;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


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
public class ProductCategory extends TableImpl<ProductCategoryRecord> {

    private static final long serialVersionUID = 1720495175;

    /**
     * The reference instance of <code>ros.Product_Category</code>
     */
    public static final ProductCategory PRODUCT_CATEGORY = new ProductCategory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProductCategoryRecord> getRecordType() {
        return ProductCategoryRecord.class;
    }

    /**
     * The column <code>ros.Product_Category.id</code>.
     */
    public final TableField<ProductCategoryRecord, UInteger> ID = createField("id", org.jooq.impl.SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>ros.Product_Category.image_url</code>.
     */
    public final TableField<ProductCategoryRecord, String> IMAGE_URL = createField("image_url", org.jooq.impl.SQLDataType.VARCHAR.length(500), this, "");

    /**
     * The column <code>ros.Product_Category.location_id</code>.
     */
    public final TableField<ProductCategoryRecord, UInteger> LOCATION_ID = createField("location_id", org.jooq.impl.SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>ros.Product_Category.created_at</code>.
     */
    public final TableField<ProductCategoryRecord, Timestamp> CREATED_AT = createField("created_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>ros.Product_Category.updated_at</code>.
     */
    public final TableField<ProductCategoryRecord, Timestamp> UPDATED_AT = createField("updated_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * Create a <code>ros.Product_Category</code> table reference
     */
    public ProductCategory() {
        this("Product_Category", null);
    }

    /**
     * Create an aliased <code>ros.Product_Category</code> table reference
     */
    public ProductCategory(String alias) {
        this(alias, PRODUCT_CATEGORY);
    }

    private ProductCategory(String alias, Table<ProductCategoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private ProductCategory(String alias, Table<ProductCategoryRecord> aliased, Field<?>[] parameters) {
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
    public Identity<ProductCategoryRecord, UInteger> getIdentity() {
        return Keys.IDENTITY_PRODUCT_CATEGORY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<ProductCategoryRecord> getPrimaryKey() {
        return Keys.KEY_PRODUCT_CATEGORY_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ProductCategoryRecord>> getKeys() {
        return Arrays.<UniqueKey<ProductCategoryRecord>>asList(Keys.KEY_PRODUCT_CATEGORY_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<ProductCategoryRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ProductCategoryRecord, ?>>asList(Keys.FK_PRODUCTCATEGORY_LOCATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductCategory as(String alias) {
        return new ProductCategory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ProductCategory rename(String name) {
        return new ProductCategory(name, null);
    }
}
