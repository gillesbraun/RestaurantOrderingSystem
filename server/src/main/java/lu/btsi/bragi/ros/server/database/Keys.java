/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.server.database;


import javax.annotation.Generated;

import lu.btsi.bragi.ros.server.database.tables.Allergen;
import lu.btsi.bragi.ros.server.database.tables.AllergenLocalized;
import lu.btsi.bragi.ros.server.database.tables.Invoice;
import lu.btsi.bragi.ros.server.database.tables.Language;
import lu.btsi.bragi.ros.server.database.tables.Order;
import lu.btsi.bragi.ros.server.database.tables.Product;
import lu.btsi.bragi.ros.server.database.tables.ProductAllergen;
import lu.btsi.bragi.ros.server.database.tables.ProductCategory;
import lu.btsi.bragi.ros.server.database.tables.ProductCategoryLocalized;
import lu.btsi.bragi.ros.server.database.tables.ProductLocalized;
import lu.btsi.bragi.ros.server.database.tables.ProductPriceForOrder;
import lu.btsi.bragi.ros.server.database.tables.Table;
import lu.btsi.bragi.ros.server.database.tables.Waiter;
import lu.btsi.bragi.ros.server.database.tables.records.AllergenLocalizedRecord;
import lu.btsi.bragi.ros.server.database.tables.records.AllergenRecord;
import lu.btsi.bragi.ros.server.database.tables.records.InvoiceRecord;
import lu.btsi.bragi.ros.server.database.tables.records.LanguageRecord;
import lu.btsi.bragi.ros.server.database.tables.records.OrderRecord;
import lu.btsi.bragi.ros.server.database.tables.records.ProductAllergenRecord;
import lu.btsi.bragi.ros.server.database.tables.records.ProductCategoryLocalizedRecord;
import lu.btsi.bragi.ros.server.database.tables.records.ProductCategoryRecord;
import lu.btsi.bragi.ros.server.database.tables.records.ProductLocalizedRecord;
import lu.btsi.bragi.ros.server.database.tables.records.ProductPriceForOrderRecord;
import lu.btsi.bragi.ros.server.database.tables.records.ProductRecord;
import lu.btsi.bragi.ros.server.database.tables.records.TableRecord;
import lu.btsi.bragi.ros.server.database.tables.records.WaiterRecord;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;
import org.jooq.types.UInteger;


/**
 * A class modelling foreign key relationships between tables of the <code>ros</code> 
 * schema
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<AllergenRecord, UInteger> IDENTITY_ALLERGEN = Identities0.IDENTITY_ALLERGEN;
    public static final Identity<InvoiceRecord, UInteger> IDENTITY_INVOICE = Identities0.IDENTITY_INVOICE;
    public static final Identity<OrderRecord, UInteger> IDENTITY_ORDER = Identities0.IDENTITY_ORDER;
    public static final Identity<ProductRecord, UInteger> IDENTITY_PRODUCT = Identities0.IDENTITY_PRODUCT;
    public static final Identity<TableRecord, UInteger> IDENTITY_TABLE = Identities0.IDENTITY_TABLE;
    public static final Identity<WaiterRecord, UInteger> IDENTITY_WAITER = Identities0.IDENTITY_WAITER;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AllergenRecord> KEY_ALLERGEN_PRIMARY = UniqueKeys0.KEY_ALLERGEN_PRIMARY;
    public static final UniqueKey<AllergenLocalizedRecord> KEY_ALLERGEN_LOCALIZED_PRIMARY = UniqueKeys0.KEY_ALLERGEN_LOCALIZED_PRIMARY;
    public static final UniqueKey<InvoiceRecord> KEY_INVOICE_PRIMARY = UniqueKeys0.KEY_INVOICE_PRIMARY;
    public static final UniqueKey<LanguageRecord> KEY_LANGUAGE_PRIMARY = UniqueKeys0.KEY_LANGUAGE_PRIMARY;
    public static final UniqueKey<OrderRecord> KEY_ORDER_PRIMARY = UniqueKeys0.KEY_ORDER_PRIMARY;
    public static final UniqueKey<ProductRecord> KEY_PRODUCT_PRIMARY = UniqueKeys0.KEY_PRODUCT_PRIMARY;
    public static final UniqueKey<ProductAllergenRecord> KEY_PRODUCT_ALLERGEN_PRIMARY = UniqueKeys0.KEY_PRODUCT_ALLERGEN_PRIMARY;
    public static final UniqueKey<ProductCategoryRecord> KEY_PRODUCT_CATEGORY_PRIMARY = UniqueKeys0.KEY_PRODUCT_CATEGORY_PRIMARY;
    public static final UniqueKey<ProductCategoryLocalizedRecord> KEY_PRODUCT_CATEGORY_LOCALIZED_PRIMARY = UniqueKeys0.KEY_PRODUCT_CATEGORY_LOCALIZED_PRIMARY;
    public static final UniqueKey<ProductLocalizedRecord> KEY_PRODUCT_LOCALIZED_PRIMARY = UniqueKeys0.KEY_PRODUCT_LOCALIZED_PRIMARY;
    public static final UniqueKey<ProductPriceForOrderRecord> KEY_PRODUCT_PRICE_FOR_ORDER_PRIMARY = UniqueKeys0.KEY_PRODUCT_PRICE_FOR_ORDER_PRIMARY;
    public static final UniqueKey<TableRecord> KEY_TABLE_PRIMARY = UniqueKeys0.KEY_TABLE_PRIMARY;
    public static final UniqueKey<WaiterRecord> KEY_WAITER_PRIMARY = UniqueKeys0.KEY_WAITER_PRIMARY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<AllergenLocalizedRecord, AllergenRecord> FK_ALLERGENLOCALIZED_ALLERGEN = ForeignKeys0.FK_ALLERGENLOCALIZED_ALLERGEN;
    public static final ForeignKey<AllergenLocalizedRecord, LanguageRecord> FK_ALLERGENLOCALIZED_LANGUAGE = ForeignKeys0.FK_ALLERGENLOCALIZED_LANGUAGE;
    public static final ForeignKey<OrderRecord, TableRecord> FK_ORDER_TABLE = ForeignKeys0.FK_ORDER_TABLE;
    public static final ForeignKey<OrderRecord, WaiterRecord> FK_ORDER_WAITER = ForeignKeys0.FK_ORDER_WAITER;
    public static final ForeignKey<OrderRecord, InvoiceRecord> FK_ORDER_INVOICE = ForeignKeys0.FK_ORDER_INVOICE;
    public static final ForeignKey<ProductRecord, ProductCategoryRecord> FK_PRODUCT_PRODUCTCATEGORY = ForeignKeys0.FK_PRODUCT_PRODUCTCATEGORY;
    public static final ForeignKey<ProductAllergenRecord, AllergenRecord> FK_PRODUCTALLERGEN_ALLERGEN = ForeignKeys0.FK_PRODUCTALLERGEN_ALLERGEN;
    public static final ForeignKey<ProductAllergenRecord, ProductRecord> FK_PRODUCTALLERGEN_PRODUCT = ForeignKeys0.FK_PRODUCTALLERGEN_PRODUCT;
    public static final ForeignKey<ProductCategoryLocalizedRecord, ProductCategoryRecord> FK_PRODUCTCATEGORYLOCALIZED_PRODUCTCATEGORY = ForeignKeys0.FK_PRODUCTCATEGORYLOCALIZED_PRODUCTCATEGORY;
    public static final ForeignKey<ProductCategoryLocalizedRecord, LanguageRecord> FK_PRODUCTCATEGORYLOCALIZED_LANGUAGE = ForeignKeys0.FK_PRODUCTCATEGORYLOCALIZED_LANGUAGE;
    public static final ForeignKey<ProductLocalizedRecord, ProductRecord> FK_PRODUCTLOCALIZED_PRODUCT = ForeignKeys0.FK_PRODUCTLOCALIZED_PRODUCT;
    public static final ForeignKey<ProductLocalizedRecord, LanguageRecord> FK_PRODUCTLOCALIZED_LANGUAGE = ForeignKeys0.FK_PRODUCTLOCALIZED_LANGUAGE;
    public static final ForeignKey<ProductPriceForOrderRecord, ProductRecord> FK_PRODUCTPRICEFORORDER_PRODUCT = ForeignKeys0.FK_PRODUCTPRICEFORORDER_PRODUCT;
    public static final ForeignKey<ProductPriceForOrderRecord, OrderRecord> FK_PRODUCTPRICEFORORDER_ORDER = ForeignKeys0.FK_PRODUCTPRICEFORORDER_ORDER;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 extends AbstractKeys {
        public static Identity<AllergenRecord, UInteger> IDENTITY_ALLERGEN = createIdentity(Allergen.ALLERGEN, Allergen.ALLERGEN.ID);
        public static Identity<InvoiceRecord, UInteger> IDENTITY_INVOICE = createIdentity(Invoice.INVOICE, Invoice.INVOICE.ID);
        public static Identity<OrderRecord, UInteger> IDENTITY_ORDER = createIdentity(Order.ORDER, Order.ORDER.ID);
        public static Identity<ProductRecord, UInteger> IDENTITY_PRODUCT = createIdentity(Product.PRODUCT, Product.PRODUCT.ID);
        public static Identity<TableRecord, UInteger> IDENTITY_TABLE = createIdentity(Table.TABLE, Table.TABLE.ID);
        public static Identity<WaiterRecord, UInteger> IDENTITY_WAITER = createIdentity(Waiter.WAITER, Waiter.WAITER.ID);
    }

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<AllergenRecord> KEY_ALLERGEN_PRIMARY = createUniqueKey(Allergen.ALLERGEN, "KEY_Allergen_PRIMARY", Allergen.ALLERGEN.ID);
        public static final UniqueKey<AllergenLocalizedRecord> KEY_ALLERGEN_LOCALIZED_PRIMARY = createUniqueKey(AllergenLocalized.ALLERGEN_LOCALIZED, "KEY_Allergen_Localized_PRIMARY", AllergenLocalized.ALLERGEN_LOCALIZED.ALLERGEN_ID, AllergenLocalized.ALLERGEN_LOCALIZED.LANGUAGE_CODE);
        public static final UniqueKey<InvoiceRecord> KEY_INVOICE_PRIMARY = createUniqueKey(Invoice.INVOICE, "KEY_Invoice_PRIMARY", Invoice.INVOICE.ID);
        public static final UniqueKey<LanguageRecord> KEY_LANGUAGE_PRIMARY = createUniqueKey(Language.LANGUAGE, "KEY_Language_PRIMARY", Language.LANGUAGE.CODE);
        public static final UniqueKey<OrderRecord> KEY_ORDER_PRIMARY = createUniqueKey(Order.ORDER, "KEY_Order_PRIMARY", Order.ORDER.ID);
        public static final UniqueKey<ProductRecord> KEY_PRODUCT_PRIMARY = createUniqueKey(Product.PRODUCT, "KEY_Product_PRIMARY", Product.PRODUCT.ID);
        public static final UniqueKey<ProductAllergenRecord> KEY_PRODUCT_ALLERGEN_PRIMARY = createUniqueKey(ProductAllergen.PRODUCT_ALLERGEN, "KEY_Product_Allergen_PRIMARY", ProductAllergen.PRODUCT_ALLERGEN.ALLERGEN_ID, ProductAllergen.PRODUCT_ALLERGEN.PRODUCT_ID);
        public static final UniqueKey<ProductCategoryRecord> KEY_PRODUCT_CATEGORY_PRIMARY = createUniqueKey(ProductCategory.PRODUCT_CATEGORY, "KEY_Product_Category_PRIMARY", ProductCategory.PRODUCT_CATEGORY.ID);
        public static final UniqueKey<ProductCategoryLocalizedRecord> KEY_PRODUCT_CATEGORY_LOCALIZED_PRIMARY = createUniqueKey(ProductCategoryLocalized.PRODUCT_CATEGORY_LOCALIZED, "KEY_Product_Category_Localized_PRIMARY", ProductCategoryLocalized.PRODUCT_CATEGORY_LOCALIZED.PRODUCT_CATEGORY_ID, ProductCategoryLocalized.PRODUCT_CATEGORY_LOCALIZED.LANGUAGE_CODE);
        public static final UniqueKey<ProductLocalizedRecord> KEY_PRODUCT_LOCALIZED_PRIMARY = createUniqueKey(ProductLocalized.PRODUCT_LOCALIZED, "KEY_Product_Localized_PRIMARY", ProductLocalized.PRODUCT_LOCALIZED.PRODUCT_ID, ProductLocalized.PRODUCT_LOCALIZED.LANGUAGE_CODE);
        public static final UniqueKey<ProductPriceForOrderRecord> KEY_PRODUCT_PRICE_FOR_ORDER_PRIMARY = createUniqueKey(ProductPriceForOrder.PRODUCT_PRICE_FOR_ORDER, "KEY_Product_Price_For_Order_PRIMARY", ProductPriceForOrder.PRODUCT_PRICE_FOR_ORDER.PRODUCT_ID, ProductPriceForOrder.PRODUCT_PRICE_FOR_ORDER.ORDER_ID);
        public static final UniqueKey<TableRecord> KEY_TABLE_PRIMARY = createUniqueKey(Table.TABLE, "KEY_Table_PRIMARY", Table.TABLE.ID);
        public static final UniqueKey<WaiterRecord> KEY_WAITER_PRIMARY = createUniqueKey(Waiter.WAITER, "KEY_Waiter_PRIMARY", Waiter.WAITER.ID);
    }

    private static class ForeignKeys0 extends AbstractKeys {
        public static final ForeignKey<AllergenLocalizedRecord, AllergenRecord> FK_ALLERGENLOCALIZED_ALLERGEN = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_ALLERGEN_PRIMARY, AllergenLocalized.ALLERGEN_LOCALIZED, "fk_AllergenLocalized_Allergen", AllergenLocalized.ALLERGEN_LOCALIZED.ALLERGEN_ID);
        public static final ForeignKey<AllergenLocalizedRecord, LanguageRecord> FK_ALLERGENLOCALIZED_LANGUAGE = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_LANGUAGE_PRIMARY, AllergenLocalized.ALLERGEN_LOCALIZED, "fk_AllergenLocalized_Language", AllergenLocalized.ALLERGEN_LOCALIZED.LANGUAGE_CODE);
        public static final ForeignKey<OrderRecord, TableRecord> FK_ORDER_TABLE = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_TABLE_PRIMARY, Order.ORDER, "fk_Order_Table", Order.ORDER.TABLE_ID);
        public static final ForeignKey<OrderRecord, WaiterRecord> FK_ORDER_WAITER = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_WAITER_PRIMARY, Order.ORDER, "fk_Order_Waiter", Order.ORDER.WAITER_ID);
        public static final ForeignKey<OrderRecord, InvoiceRecord> FK_ORDER_INVOICE = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_INVOICE_PRIMARY, Order.ORDER, "fk_Order_Invoice", Order.ORDER.INVOICE_ID);
        public static final ForeignKey<ProductRecord, ProductCategoryRecord> FK_PRODUCT_PRODUCTCATEGORY = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_PRODUCT_CATEGORY_PRIMARY, Product.PRODUCT, "fk_Product_ProductCategory", Product.PRODUCT.PRODUCT_CATEGORY_ID);
        public static final ForeignKey<ProductAllergenRecord, AllergenRecord> FK_PRODUCTALLERGEN_ALLERGEN = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_ALLERGEN_PRIMARY, ProductAllergen.PRODUCT_ALLERGEN, "fk_ProductAllergen_Allergen", ProductAllergen.PRODUCT_ALLERGEN.ALLERGEN_ID);
        public static final ForeignKey<ProductAllergenRecord, ProductRecord> FK_PRODUCTALLERGEN_PRODUCT = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_PRODUCT_PRIMARY, ProductAllergen.PRODUCT_ALLERGEN, "fk_ProductAllergen_Product", ProductAllergen.PRODUCT_ALLERGEN.PRODUCT_ID);
        public static final ForeignKey<ProductCategoryLocalizedRecord, ProductCategoryRecord> FK_PRODUCTCATEGORYLOCALIZED_PRODUCTCATEGORY = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_PRODUCT_CATEGORY_PRIMARY, ProductCategoryLocalized.PRODUCT_CATEGORY_LOCALIZED, "fk_ProductCategoryLocalized_ProductCategory", ProductCategoryLocalized.PRODUCT_CATEGORY_LOCALIZED.PRODUCT_CATEGORY_ID);
        public static final ForeignKey<ProductCategoryLocalizedRecord, LanguageRecord> FK_PRODUCTCATEGORYLOCALIZED_LANGUAGE = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_LANGUAGE_PRIMARY, ProductCategoryLocalized.PRODUCT_CATEGORY_LOCALIZED, "fk_ProductCategoryLocalized_Language", ProductCategoryLocalized.PRODUCT_CATEGORY_LOCALIZED.LANGUAGE_CODE);
        public static final ForeignKey<ProductLocalizedRecord, ProductRecord> FK_PRODUCTLOCALIZED_PRODUCT = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_PRODUCT_PRIMARY, ProductLocalized.PRODUCT_LOCALIZED, "fk_ProductLocalized_Product", ProductLocalized.PRODUCT_LOCALIZED.PRODUCT_ID);
        public static final ForeignKey<ProductLocalizedRecord, LanguageRecord> FK_PRODUCTLOCALIZED_LANGUAGE = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_LANGUAGE_PRIMARY, ProductLocalized.PRODUCT_LOCALIZED, "fk_ProductLocalized_Language", ProductLocalized.PRODUCT_LOCALIZED.LANGUAGE_CODE);
        public static final ForeignKey<ProductPriceForOrderRecord, ProductRecord> FK_PRODUCTPRICEFORORDER_PRODUCT = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_PRODUCT_PRIMARY, ProductPriceForOrder.PRODUCT_PRICE_FOR_ORDER, "fk_ProductPriceForOrder_Product", ProductPriceForOrder.PRODUCT_PRICE_FOR_ORDER.PRODUCT_ID);
        public static final ForeignKey<ProductPriceForOrderRecord, OrderRecord> FK_PRODUCTPRICEFORORDER_ORDER = createForeignKey(lu.btsi.bragi.ros.server.database.Keys.KEY_ORDER_PRIMARY, ProductPriceForOrder.PRODUCT_PRICE_FOR_ORDER, "fk_ProductPriceForOrder_Order", ProductPriceForOrder.PRODUCT_PRICE_FOR_ORDER.ORDER_ID);
    }
}
