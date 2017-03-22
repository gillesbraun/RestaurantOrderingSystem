package lu.btsi.bragi.ros.client.settings;

import lu.btsi.bragi.ros.models.pojos.Language;

import java.util.Locale;

/**
 * Created by gillesbraun on 20/03/2017.
 */
public class GeneralSettings {
    private Language language = new Language("en", "English", null, null);
    private Locale locale = Locale.US;

    public GeneralSettings() {
        Locale.setDefault(locale);
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        Locale.setDefault(locale);
    }

    public Locale getLocale() {
        return locale;
    }
}
