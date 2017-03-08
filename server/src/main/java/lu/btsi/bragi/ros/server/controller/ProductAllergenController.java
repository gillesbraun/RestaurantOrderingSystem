package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.Product;
import lu.btsi.bragi.ros.models.pojos.ProductAllergen;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.ProductAllergenRecord;
import org.jooq.Record1;

import java.util.List;

import static org.jooq.impl.DSL.count;

/**
 * Created by gillesbraun on 02/03/2017.
 */
public class ProductAllergenController extends Controller<ProductAllergen> {

    private static final Class<ProductAllergen> pojo = ProductAllergen.class;
    private static final lu.btsi.bragi.ros.server.database.tables.ProductAllergen dbTable = Tables.PRODUCT_ALLERGEN;

    public ProductAllergenController() {
        super(pojo);
    }

    @Override
    protected List<ProductAllergen> handleGet() throws Exception {
        return context.fetch(dbTable).into(pojo);
    }

    @Override
    protected void handleUpdate(ProductAllergen obj) throws Exception {
        ProductAllergenRecord productAllergenRecord = new ProductAllergenRecord();
        productAllergenRecord.from(obj);
        productAllergenRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(productAllergenRecord);
    }

    @Override
    protected void handleCreate(ProductAllergen obj) throws Exception {
        ProductAllergenRecord productAllergenRecord = new ProductAllergenRecord();
        productAllergenRecord.from(obj);
        Record1<Integer> count = context.select(count())
                .from(dbTable)
                .where(dbTable.PRODUCT_ID.equal(productAllergenRecord.getProductId()))
                .and(dbTable.ALLERGEN_ID.equal(productAllergenRecord.getAllergenId()))
                .fetchOne();
        if(count.value1() > 0) {
            handleUpdate(obj);
            return;
        }
        context.executeInsert(productAllergenRecord);
    }

    @Override
    protected void handleDelete(ProductAllergen obj) throws Exception {
        ProductAllergenRecord productAllergenRecord = new ProductAllergenRecord();
        productAllergenRecord.from(obj);
        context.executeDelete(productAllergenRecord);
    }

    List<ProductAllergen> getProductAllergen(Product product) {
        List<ProductAllergen> productAllergens = context.select()
                .from(dbTable)
                .where(dbTable.PRODUCT_ID.eq(product.getId()))
                .fetch()
                .into(pojo);
        productAllergens.forEach(productAllergen -> {
            productAllergen.setAllergen(getController(AllergenController.class).getAllergen(productAllergen));
        });
        return productAllergens;
    }
}
