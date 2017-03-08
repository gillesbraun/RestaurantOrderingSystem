package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.Allergen;
import lu.btsi.bragi.ros.server.database.tables.records.AllergenRecord;

import java.util.List;

/**
 * Created by gillesbraun on 01/03/2017.
 */
public class AllergenController extends Controller<Allergen> {

    private static Class<Allergen> pojo = Allergen.class;

    private lu.btsi.bragi.ros.server.database.tables.Allergen dbTable = lu.btsi.bragi.ros.server.database.tables.Allergen.ALLERGEN;


    public AllergenController() {
        super(pojo);
    }

    @Override
    protected List<Allergen> handleGet() throws Exception {
        List<Allergen> list = context.fetch(dbTable).into(pojo);
        return list;
    }

    @Override
    protected void handleUpdate(Allergen obj) throws Exception {
        AllergenRecord allergenRecord = new AllergenRecord();
        allergenRecord.from(obj);
        allergenRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(allergenRecord);
    }

    @Override
    protected void handleCreate(Allergen obj) throws Exception {
        AllergenRecord allergenRecord = new AllergenRecord();
        allergenRecord.from(obj);
        context.executeInsert(allergenRecord);
    }

    @Override
    protected void handleDelete(Allergen obj) throws Exception {
        AllergenRecord allergenRecord = new AllergenRecord();
        allergenRecord.from(obj);
        context.executeDelete(allergenRecord);
    }
}
