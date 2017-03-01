package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.ProductCategoryLocalized;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.ProductCategoryLocalizedRecord;

import java.util.List;

/**
 * Created by gillesbraun on 01/03/2017.
 */
public class ProductCategoryLocalizedController extends Controller<ProductCategoryLocalized> {

    private static final Class pojo = ProductCategoryLocalized.class;
    private static final lu.btsi.bragi.ros.server.database.tables.ProductCategoryLocalized dbTable = Tables.PRODUCT_CATEGORY_LOCALIZED;

    public ProductCategoryLocalizedController() {
        super(pojo);
    }

    @Override
    protected List<ProductCategoryLocalized> handleGet() throws Exception {
        return context.fetch(dbTable).into(pojo);
    }

    @Override
    protected void handleUpdate(ProductCategoryLocalized obj) throws Exception {
        ProductCategoryLocalizedRecord productCategoryLocalizedRecord = new ProductCategoryLocalizedRecord();
        productCategoryLocalizedRecord.from(obj);
        productCategoryLocalizedRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(productCategoryLocalizedRecord);
    }

    @Override
    protected void handleCreate(ProductCategoryLocalized obj) throws Exception {
        ProductCategoryLocalizedRecord productCategoryLocalizedRecord = new ProductCategoryLocalizedRecord();
        productCategoryLocalizedRecord.from(obj);
        context.executeInsert(productCategoryLocalizedRecord);
    }

    @Override
    protected void handleDelete(ProductCategoryLocalized obj) throws Exception {
        ProductCategoryLocalizedRecord productCategoryLocalizedRecord = new ProductCategoryLocalizedRecord();
        productCategoryLocalizedRecord.from(obj);
        context.executeDelete(productCategoryLocalizedRecord);
    }
}
