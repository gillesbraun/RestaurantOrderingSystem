package lu.btsi.bragi.ros.server.controller;

import lu.btsi.bragi.ros.models.pojos.Language;
import lu.btsi.bragi.ros.server.database.Tables;
import lu.btsi.bragi.ros.server.database.tables.records.LanguageRecord;

import java.util.List;

/**
 * Created by gillesbraun on 01/03/2017.
 */
public class LanguageController extends Controller<Language> {

    private static final Class mapTo = Language.class;

    private static final lu.btsi.bragi.ros.server.database.tables.Language dbTable = Tables.LANGUAGE;

    LanguageController() {
        super(mapTo);
    }

    @Override
    protected List<Language> handleGet() throws Exception {
        return context.fetch(dbTable).into(mapTo);
    }

    @Override
    protected void handleUpdate(Language obj) throws Exception {
        LanguageRecord languageRecord = new LanguageRecord();
        languageRecord.from(obj);
        languageRecord.reset(dbTable.UPDATED_AT);
        context.executeUpdate(languageRecord);
    }

    @Override
    protected void handleCreate(Language obj) throws Exception {
        LanguageRecord languageRecord = new LanguageRecord();
        languageRecord.from(obj);
        context.executeInsert(languageRecord);
    }

    @Override
    protected void handleDelete(Language obj) throws Exception {
        LanguageRecord languageRecord = new LanguageRecord();
        languageRecord.from(obj);
        context.executeDelete(languageRecord);
    }
}
