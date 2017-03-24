package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.Product;
import lu.btsi.bragi.ros.models.pojos.ProductCategory;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.ProductCategoryRecord;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gillesbraun on 01/03/2017.
 */
public class ProductCategoryController extends Controller<ProductCategory> {

    private static final Class<ProductCategory> pojo = ProductCategory.class;
    private static final lu.btsi.bragi.ros.server.database.tables.ProductCategory dbTable = Tables.PRODUCT_CATEGORY;

    public ProductCategoryController() {
        super(pojo);
    }

    @Override
    protected List<ProductCategory> handleGet() throws Exception {
        List<ProductCategory> categories = context.fetch(dbTable).into(pojo);
        categories = categories.stream().map(this::fetchReferences).collect(Collectors.toList());
        return categories;
    }

    @Override
    protected void handleUpdate(ProductCategory obj) throws Exception {
        ProductCategoryRecord productCategoryRecord = new ProductCategoryRecord();
        productCategoryRecord.from(obj);
        productCategoryRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(productCategoryRecord);
    }

    @Override
    protected void handleCreate(ProductCategory obj) throws Exception {
        ProductCategoryRecord productCategoryRecord = new ProductCategoryRecord();
        productCategoryRecord.from(obj);
        context.executeInsert(productCategoryRecord);
    }

    @Override
    protected void handleDelete(ProductCategory obj) throws Exception {
        ProductCategoryRecord productCategoryRecord = new ProductCategoryRecord();
        productCategoryRecord.from(obj);
        context.executeDelete(productCategoryRecord);
    }

    private ProductCategory fetchReferences(ProductCategory productCategory) {
        productCategory.setProductCategoryLocalized(
                getController(ProductCategoryLocalizedController.class).getProductCategoryLocalized(productCategory)
        );
        productCategory.setLocation(getController(LocationController.class).getLocation(productCategory));
        return productCategory;
    }

    ProductCategory getProductCategory(Product product) {
        ProductCategory productCategory = context.select()
                .from(dbTable)
                .where(dbTable.ID.eq(product.getProductCategoryId()))
                .fetchOne()
                .into(pojo);
        productCategory = fetchReferences(productCategory);
        return productCategory;
    }
}
