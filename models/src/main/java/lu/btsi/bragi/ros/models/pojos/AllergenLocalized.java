/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.models.pojos;


import java.io.Serializable;

import javax.annotation.Generated;

import org.jooq.types.UInteger;


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
public class AllergenLocalized implements Serializable {

    private static final long serialVersionUID = 2131948069;

    private UInteger allergenId;
    private String   languageCode;

    public AllergenLocalized() {}

    public AllergenLocalized(AllergenLocalized value) {
        this.allergenId = value.allergenId;
        this.languageCode = value.languageCode;
    }

    public AllergenLocalized(
        UInteger allergenId,
        String   languageCode
    ) {
        this.allergenId = allergenId;
        this.languageCode = languageCode;
    }

    public UInteger getAllergenId() {
        return this.allergenId;
    }

    public void setAllergenId(UInteger allergenId) {
        this.allergenId = allergenId;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AllergenLocalized (");

        sb.append(allergenId);
        sb.append(", ").append(languageCode);

        sb.append(")");
        return sb.toString();
    }
}