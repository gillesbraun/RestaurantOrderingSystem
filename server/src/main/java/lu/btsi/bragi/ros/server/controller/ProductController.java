package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.Product;
import lu.btsi.bragi.ros.models.pojos.ProductPriceForOrder;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.ProductRecord;

import java.util.List;

/**
 * Created by gillesbraun on 01/03/2017.
 */
public class ProductController extends Controller<Product> {

    private static final Class<Product> pojo = Product.class;
    private static final lu.btsi.bragi.ros.server.database.tables.Product dbTable = Tables.PRODUCT;

    ProductController() {
        super(pojo);
    }

    @Override
    protected List<Product> handleGet() throws Exception {
        return context.fetch(dbTable).into(pojo);
    }

    @Override
    protected void handleUpdate(Product obj) throws Exception {
        ProductRecord productRecord = new ProductRecord();
        productRecord.from(obj);
        productRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(productRecord);
    }

    @Override
    protected void handleCreate(Product obj) throws Exception {
        ProductRecord productRecord = new ProductRecord();
        productRecord.from(obj);
        context.executeInsert(productRecord);
    }

    @Override
    protected void handleDelete(Product obj) throws Exception {
        ProductRecord productRecord = new ProductRecord();
        productRecord.from(obj);
        context.executeDelete(productRecord);
    }

    Product getProduct(ProductPriceForOrder productPriceForOrders) {
        Product product = context.select()
                .from(dbTable)
                .where(dbTable.ID.eq(productPriceForOrders.getProductId()))
                .fetchOne()
                .into(pojo);
        product.setProductLocalized(getController(ProductLocalizedController.class).getProductsLocalized(product));
        return product;
    }
}
