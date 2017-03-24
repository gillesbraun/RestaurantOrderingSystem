package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.Location;
import lu.btsi.bragi.ros.models.pojos.Product;
import lu.btsi.bragi.ros.models.pojos.ProductCategory;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.LocationRecord;

import java.util.List;

/**
 * Created by gillesbraun on 06/03/2017.
 */
public class LocationController extends Controller<Location> {

    private static final Class<Location> pojo = Location.class;
    private static final lu.btsi.bragi.ros.server.database.tables.Location dbTable = Tables.LOCATION;

    public LocationController() {
        super(pojo);
    }

    @Override
    protected List<Location> handleGet() throws Exception {
        return context.fetch(dbTable).into(pojo);
    }

    @Override
    protected void handleUpdate(Location obj) throws Exception {
        LocationRecord locationRecord = new LocationRecord();
        locationRecord.from(obj);
        locationRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(locationRecord);
    }

    @Override
    protected void handleCreate(Location obj) throws Exception {
        LocationRecord locationRecord = new LocationRecord();
        locationRecord.from(obj);
        context.executeInsert(locationRecord);
    }

    @Override
    protected void handleDelete(Location obj) throws Exception {
        LocationRecord locationRecord = new LocationRecord();
        locationRecord.from(obj);
        context.executeDelete(locationRecord);
    }

    Location getLocation(Product product) {
        if(product.getLocationId() == null)
            return null;
        Location location = context.select()
                .from(dbTable)
                .where(dbTable.ID.eq(product.getLocationId()))
                .fetchOne()
                .into(pojo);
        return location;
    }

    Location getLocation(ProductCategory productCategory) {
        Location location = context.select()
                .from(dbTable)
                .where(dbTable.ID.eq(productCategory.getLocationId()))
                .fetchOne()
                .into(pojo);
        return location;
    }
}
