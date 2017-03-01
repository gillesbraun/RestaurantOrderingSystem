package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.ProductLocalized;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.ProductLocalizedRecord;

import java.util.List;

/**
 * Created by gillesbraun on 01/03/2017.
 */
public class ProductLocalizedController extends Controller<ProductLocalized> {

    private static final Class pojo = ProductLocalized.class;
    private static final lu.btsi.bragi.ros.server.database.tables.ProductLocalized dbTable = Tables.PRODUCT_LOCALIZED;

    public ProductLocalizedController() {
        super(pojo);
    }

    @Override
    protected List<ProductLocalized> handleGet() throws Exception {
        return context.fetch(dbTable).into(pojo);
    }

    @Override
    protected void handleUpdate(ProductLocalized obj) throws Exception {
        ProductLocalizedRecord productLocalizedRecord = new ProductLocalizedRecord();
        productLocalizedRecord.from(obj);
        productLocalizedRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(productLocalizedRecord);
    }

    @Override
    protected void handleCreate(ProductLocalized obj) throws Exception {
        ProductLocalizedRecord productLocalizedRecord = new ProductLocalizedRecord();
        productLocalizedRecord.from(obj);
        context.executeInsert(productLocalizedRecord);
    }

    @Override
    protected void handleDelete(ProductLocalized obj) throws Exception {
        ProductLocalizedRecord productLocalizedRecord = new ProductLocalizedRecord();
        productLocalizedRecord.from(obj);
        context.executeDelete(productLocalizedRecord);
    }
}
