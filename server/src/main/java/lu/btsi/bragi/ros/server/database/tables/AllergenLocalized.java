/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.server.database.tables;


import lu.btsi.bragi.ros.server.database.Keys;
import lu.btsi.bragi.ros.server.database.Ros;
import lu.btsi.bragi.ros.server.database.tables.records.AllergenLocalizedRecord;
import org.jooq.*;
import org.jooq.Table;
import org.jooq.impl.TableImpl;
import org.jooq.types.UInteger;

import javax.annotation.Generated;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AllergenLocalized extends TableImpl<AllergenLocalizedRecord> {

    private static final long serialVersionUID = -1807722691;

    /**
     * The reference instance of <code>ros.Allergen_Localized</code>
     */
    public static final AllergenLocalized ALLERGEN_LOCALIZED = new AllergenLocalized();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AllergenLocalizedRecord> getRecordType() {
        return AllergenLocalizedRecord.class;
    }

    /**
     * The column <code>ros.Allergen_Localized.allergen_id</code>.
     */
    public final TableField<AllergenLocalizedRecord, UInteger> ALLERGEN_ID = createField("allergen_id", org.jooq.impl.SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>ros.Allergen_Localized.language_code</code>.
     */
    public final TableField<AllergenLocalizedRecord, String> LANGUAGE_CODE = createField("language_code", org.jooq.impl.SQLDataType.CHAR.length(2).nullable(false), this, "");

    /**
     * The column <code>ros.Allergen_Localized.label</code>.
     */
    public final TableField<AllergenLocalizedRecord, String> LABEL = createField("label", org.jooq.impl.SQLDataType.VARCHAR.length(500), this, "");

    /**
     * Create a <code>ros.Allergen_Localized</code> table reference
     */
    public AllergenLocalized() {
        this("Allergen_Localized", null);
    }

    /**
     * Create an aliased <code>ros.Allergen_Localized</code> table reference
     */
    public AllergenLocalized(String alias) {
        this(alias, ALLERGEN_LOCALIZED);
    }

    private AllergenLocalized(String alias, Table<AllergenLocalizedRecord> aliased) {
        this(alias, aliased, null);
    }

    private AllergenLocalized(String alias, Table<AllergenLocalizedRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Ros.ROS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<AllergenLocalizedRecord> getPrimaryKey() {
        return Keys.KEY_ALLERGEN_LOCALIZED_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<AllergenLocalizedRecord>> getKeys() {
        return Arrays.<UniqueKey<AllergenLocalizedRecord>>asList(Keys.KEY_ALLERGEN_LOCALIZED_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<AllergenLocalizedRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<AllergenLocalizedRecord, ?>>asList(Keys.FK_ALLERGENLOCALIZED_ALLERGEN, Keys.FK_ALLERGENLOCALIZED_LANGUAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AllergenLocalized as(String alias) {
        return new AllergenLocalized(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public AllergenLocalized rename(String name) {
        return new AllergenLocalized(name, null);
    }
}
