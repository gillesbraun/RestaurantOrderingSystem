/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.server.database;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import lu.btsi.bragi.ros.server.database.tables.Allergen;
import lu.btsi.bragi.ros.server.database.tables.Allergenlocalized;
import lu.btsi.bragi.ros.server.database.tables.Invoice;
import lu.btsi.bragi.ros.server.database.tables.Language;
import lu.btsi.bragi.ros.server.database.tables.Order;
import lu.btsi.bragi.ros.server.database.tables.Product;
import lu.btsi.bragi.ros.server.database.tables.Productallergen;
import lu.btsi.bragi.ros.server.database.tables.Productlocalized;
import lu.btsi.bragi.ros.server.database.tables.Productpricefororder;
import lu.btsi.bragi.ros.server.database.tables.Table;
import lu.btsi.bragi.ros.server.database.tables.Waiter;

import org.jooq.Catalog;
import org.jooq.impl.SchemaImpl;


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
public class Ros extends SchemaImpl {

    private static final long serialVersionUID = 148735419;

    /**
     * The reference instance of <code>ros</code>
     */
    public static final Ros ROS = new Ros();

    /**
     * The table <code>ros.Allergen</code>.
     */
    public final Allergen ALLERGEN = lu.btsi.bragi.ros.server.database.tables.Allergen.ALLERGEN;

    /**
     * The table <code>ros.AllergenLocalized</code>.
     */
    public final Allergenlocalized ALLERGENLOCALIZED = lu.btsi.bragi.ros.server.database.tables.Allergenlocalized.ALLERGENLOCALIZED;

    /**
     * The table <code>ros.Invoice</code>.
     */
    public final Invoice INVOICE = lu.btsi.bragi.ros.server.database.tables.Invoice.INVOICE;

    /**
     * The table <code>ros.Language</code>.
     */
    public final Language LANGUAGE = lu.btsi.bragi.ros.server.database.tables.Language.LANGUAGE;

    /**
     * The table <code>ros.Order</code>.
     */
    public final Order ORDER = lu.btsi.bragi.ros.server.database.tables.Order.ORDER;

    /**
     * The table <code>ros.Product</code>.
     */
    public final Product PRODUCT = lu.btsi.bragi.ros.server.database.tables.Product.PRODUCT;

    /**
     * The table <code>ros.ProductAllergen</code>.
     */
    public final Productallergen PRODUCTALLERGEN = lu.btsi.bragi.ros.server.database.tables.Productallergen.PRODUCTALLERGEN;

    /**
     * The table <code>ros.ProductLocalized</code>.
     */
    public final Productlocalized PRODUCTLOCALIZED = lu.btsi.bragi.ros.server.database.tables.Productlocalized.PRODUCTLOCALIZED;

    /**
     * The table <code>ros.ProductPriceForOrder</code>.
     */
    public final Productpricefororder PRODUCTPRICEFORORDER = lu.btsi.bragi.ros.server.database.tables.Productpricefororder.PRODUCTPRICEFORORDER;

    /**
     * The table <code>ros.Table</code>.
     */
    public final Table TABLE = lu.btsi.bragi.ros.server.database.tables.Table.TABLE;

    /**
     * The table <code>ros.Waiter</code>.
     */
    public final Waiter WAITER = lu.btsi.bragi.ros.server.database.tables.Waiter.WAITER;

    /**
     * No further instances allowed
     */
    private Ros() {
        super("ros", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<org.jooq.Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<org.jooq.Table<?>> getTables0() {
        return Arrays.<org.jooq.Table<?>>asList(
            Allergen.ALLERGEN,
            Allergenlocalized.ALLERGENLOCALIZED,
            Invoice.INVOICE,
            Language.LANGUAGE,
            Order.ORDER,
            Product.PRODUCT,
            Productallergen.PRODUCTALLERGEN,
            Productlocalized.PRODUCTLOCALIZED,
            Productpricefororder.PRODUCTPRICEFORORDER,
            Table.TABLE,
            Waiter.WAITER);
    }
}
