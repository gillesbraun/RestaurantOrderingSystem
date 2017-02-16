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
import lu.btsi.bragi.ros.server.database.tables.records.ProductPriceForOrderRecord;

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
public class ProductPriceForOrder extends TableImpl<ProductPriceForOrderRecord> {

    private static final long serialVersionUID = -106925722;

    /**
     * The reference instance of <code>ros.Product_Price_For_Order</code>
     */
    public static final ProductPriceForOrder PRODUCT_PRICE_FOR_ORDER = new ProductPriceForOrder();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProductPriceForOrderRecord> getRecordType() {
        return ProductPriceForOrderRecord.class;
    }

    /**
     * The column <code>ros.Product_Price_For_Order.product_id</code>.
     */
    public final TableField<ProductPriceForOrderRecord, UInteger> PRODUCT_ID = createField("product_id", org.jooq.impl.SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>ros.Product_Price_For_Order.order_id</code>.
     */
    public final TableField<ProductPriceForOrderRecord, UInteger> ORDER_ID = createField("order_id", org.jooq.impl.SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>ros.Product_Price_For_Order.price</code>.
     */
    public final TableField<ProductPriceForOrderRecord, Double> PRICE = createField("price", org.jooq.impl.SQLDataType.DOUBLE.nullable(false), this, "");

    /**
     * The column <code>ros.Product_Price_For_Order.created_at</code>.
     */
    public final TableField<ProductPriceForOrderRecord, Timestamp> CREATED_AT = createField("created_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>ros.Product_Price_For_Order.updated_at</code>.
     */
    public final TableField<ProductPriceForOrderRecord, Timestamp> UPDATED_AT = createField("updated_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * Create a <code>ros.Product_Price_For_Order</code> table reference
     */
    public ProductPriceForOrder() {
        this("Product_Price_For_Order", null);
    }

    /**
     * Create an aliased <code>ros.Product_Price_For_Order</code> table reference
     */
    public ProductPriceForOrder(String alias) {
        this(alias, PRODUCT_PRICE_FOR_ORDER);
    }

    private ProductPriceForOrder(String alias, Table<ProductPriceForOrderRecord> aliased) {
        this(alias, aliased, null);
    }

    private ProductPriceForOrder(String alias, Table<ProductPriceForOrderRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<ProductPriceForOrderRecord> getPrimaryKey() {
        return Keys.KEY_PRODUCT_PRICE_FOR_ORDER_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ProductPriceForOrderRecord>> getKeys() {
        return Arrays.<UniqueKey<ProductPriceForOrderRecord>>asList(Keys.KEY_PRODUCT_PRICE_FOR_ORDER_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<ProductPriceForOrderRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ProductPriceForOrderRecord, ?>>asList(Keys.FK_PRODUCTPRICEFORORDER_PRODUCT, Keys.FK_PRODUCTPRICEFORORDER_ORDER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductPriceForOrder as(String alias) {
        return new ProductPriceForOrder(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ProductPriceForOrder rename(String name) {
        return new ProductPriceForOrder(name, null);
    }
}
