/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.server.database;


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


/**
 * Convenience access to all tables in ros
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>ros.Allergen</code>.
     */
    public static final Allergen ALLERGEN = lu.btsi.bragi.ros.server.database.tables.Allergen.ALLERGEN;

    /**
     * The table <code>ros.AllergenLocalized</code>.
     */
    public static final Allergenlocalized ALLERGENLOCALIZED = lu.btsi.bragi.ros.server.database.tables.Allergenlocalized.ALLERGENLOCALIZED;

    /**
     * The table <code>ros.Invoice</code>.
     */
    public static final Invoice INVOICE = lu.btsi.bragi.ros.server.database.tables.Invoice.INVOICE;

    /**
     * The table <code>ros.Language</code>.
     */
    public static final Language LANGUAGE = lu.btsi.bragi.ros.server.database.tables.Language.LANGUAGE;

    /**
     * The table <code>ros.Order</code>.
     */
    public static final Order ORDER = lu.btsi.bragi.ros.server.database.tables.Order.ORDER;

    /**
     * The table <code>ros.Product</code>.
     */
    public static final Product PRODUCT = lu.btsi.bragi.ros.server.database.tables.Product.PRODUCT;

    /**
     * The table <code>ros.ProductAllergen</code>.
     */
    public static final Productallergen PRODUCTALLERGEN = lu.btsi.bragi.ros.server.database.tables.Productallergen.PRODUCTALLERGEN;

    /**
     * The table <code>ros.ProductLocalized</code>.
     */
    public static final Productlocalized PRODUCTLOCALIZED = lu.btsi.bragi.ros.server.database.tables.Productlocalized.PRODUCTLOCALIZED;

    /**
     * The table <code>ros.ProductPriceForOrder</code>.
     */
    public static final Productpricefororder PRODUCTPRICEFORORDER = lu.btsi.bragi.ros.server.database.tables.Productpricefororder.PRODUCTPRICEFORORDER;

    /**
     * The table <code>ros.Table</code>.
     */
    public static final Table TABLE = lu.btsi.bragi.ros.server.database.tables.Table.TABLE;

    /**
     * The table <code>ros.Waiter</code>.
     */
    public static final Waiter WAITER = lu.btsi.bragi.ros.server.database.tables.Waiter.WAITER;
}
