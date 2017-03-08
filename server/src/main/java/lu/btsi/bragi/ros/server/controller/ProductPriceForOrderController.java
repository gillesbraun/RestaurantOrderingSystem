package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.ProductPriceForOrder;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.ProductPriceForOrderRecord;

import java.util.List;

/**
 * Created by gillesbraun on 06/03/2017.
 */
public class ProductPriceForOrderController extends Controller<ProductPriceForOrder> {

    private static final Class<ProductPriceForOrder> pojo = ProductPriceForOrder.class;
    private static final lu.btsi.bragi.ros.server.database.tables.ProductPriceForOrder dbTable = Tables.PRODUCT_PRICE_FOR_ORDER;

    public ProductPriceForOrderController() {
        super(pojo);
    }

    @Override
    protected List<ProductPriceForOrder> handleGet() throws Exception {
        return context.fetch(dbTable).into(pojo);
    }

    List<ProductPriceForOrder> getProductPriceForOrderForOrder(Order order) {
        List<ProductPriceForOrder> productPriceForOrder = context.select()
                .from(dbTable)
                .where(dbTable.ORDER_ID.eq(order.getId()))
                .fetch()
                .into(pojo);
        productPriceForOrder.forEach(ppfo -> {
            ppfo.setProduct(getController(ProductController.class).getProduct(ppfo));
        });
        return productPriceForOrder;
    }

    @Override
    protected void handleUpdate(ProductPriceForOrder obj) throws Exception {
        ProductPriceForOrderRecord productPriceForOrderRecord = new ProductPriceForOrderRecord();
        productPriceForOrderRecord.from(obj);
        productPriceForOrderRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(productPriceForOrderRecord);
    }

    @Override
    protected void handleCreate(ProductPriceForOrder obj) throws Exception {
        ProductPriceForOrderRecord productPriceForOrderRecord = new ProductPriceForOrderRecord();
        productPriceForOrderRecord.from(obj);
        context.executeInsert(productPriceForOrderRecord);
    }

    @Override
    protected void handleDelete(ProductPriceForOrder obj) throws Exception {
        ProductPriceForOrderRecord productPriceForOrderRecord = new ProductPriceForOrderRecord();
        productPriceForOrderRecord.from(obj);
        context.executeDelete(productPriceForOrderRecord);
    }
}
