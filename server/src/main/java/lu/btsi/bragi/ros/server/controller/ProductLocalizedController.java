package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.Product;
import lu.btsi.bragi.ros.models.pojos.ProductLocalized;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.ProductLocalizedRecord;
import org.jooq.Record1;

import java.util.List;

import static org.jooq.impl.DSL.count;

/**
 * Created by gillesbraun on 01/03/2017.
 */
public class ProductLocalizedController extends Controller<ProductLocalized> {

    private static final Class<ProductLocalized> pojo = ProductLocalized.class;
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

        Record1<Integer> count = context.select(count())
                .from(dbTable)
                .where(dbTable.PRODUCT_ID.equal(productLocalizedRecord.getProductId()))
                .and(dbTable.LANGUAGE_CODE.eq(productLocalizedRecord.getLanguageCode())).fetchOne();
        if(count.value1() == 0) {
            context.executeInsert(productLocalizedRecord);
        } else {
            productLocalizedRecord.reset(dbTable.UPDATED_AT);
            context.executeUpdate(productLocalizedRecord);
        }
    }

    @Override
    protected void handleDelete(ProductLocalized obj) throws Exception {
        ProductLocalizedRecord productLocalizedRecord = new ProductLocalizedRecord();
        productLocalizedRecord.from(obj);
        context.executeDelete(productLocalizedRecord);
    }

    List<ProductLocalized> getProductsLocalized(Product product) {
        return context.select()
                .from(dbTable)
                .where(dbTable.PRODUCT_ID.eq(product.getId()))
                .fetch()
                .into(pojo);
    }
}
