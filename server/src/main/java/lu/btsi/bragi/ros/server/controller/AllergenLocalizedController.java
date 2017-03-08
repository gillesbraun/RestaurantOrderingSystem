package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.Allergen;
import lu.btsi.bragi.ros.models.pojos.AllergenLocalized;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.AllergenLocalizedRecord;

import java.util.List;

/**
 * Created by gillesbraun on 01/03/2017.
 */
public class AllergenLocalizedController extends Controller<AllergenLocalized> {

    private static Class<AllergenLocalized> pojo = AllergenLocalized.class;

    private lu.btsi.bragi.ros.server.database.tables.AllergenLocalized dbTable = Tables.ALLERGEN_LOCALIZED;


    public AllergenLocalizedController() {
        super(pojo);
    }

    @Override
    protected List<AllergenLocalized> handleGet() throws Exception {
        List<AllergenLocalized> list = context.fetch(dbTable).into(pojo);
        return list;
    }

    @Override
    protected void handleUpdate(AllergenLocalized obj) throws Exception {
        AllergenLocalizedRecord allergenLocalizedRecord = new AllergenLocalizedRecord();
        allergenLocalizedRecord.from(obj);
        allergenLocalizedRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(allergenLocalizedRecord);
    }

    @Override
    protected void handleCreate(AllergenLocalized obj) throws Exception {
        AllergenLocalizedRecord allergenLocalizedRecord = new AllergenLocalizedRecord();
        allergenLocalizedRecord.from(obj);
        context.executeInsert(allergenLocalizedRecord);
    }

    @Override
    protected void handleDelete(AllergenLocalized obj) throws Exception {
        AllergenLocalizedRecord allergenLocalizedRecord = new AllergenLocalizedRecord();
        allergenLocalizedRecord.from(obj);
        context.executeDelete(allergenLocalizedRecord);
    }

    List<AllergenLocalized> getAllergenLocalized(Allergen allergen) {
        return context.select()
                .from(dbTable)
                .where(dbTable.ALLERGEN_ID.eq(allergen.getId()))
                .fetch()
                .into(pojo);
    }
}
