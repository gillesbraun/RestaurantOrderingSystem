package lu.btsi.bragi.ros.client.settings;

import lu.btsi.bragi.ros.models.pojos.Language;

/**
 * Created by gillesbraun on 20/03/2017.
 */
public class GeneralSettings {

    private String currency = "\u20ac"; // euro sign
    private Language language = new Language("en", "English", null, null);

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
