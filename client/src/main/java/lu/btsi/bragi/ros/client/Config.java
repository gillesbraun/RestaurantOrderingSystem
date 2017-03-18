package lu.btsi.bragi.ros.client;

import lu.btsi.bragi.ros.models.pojos.Language;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Gilles Braun on 18.03.2017.
 */
public class Config {
    private static Config ourInstance = new Config();
    private String currency = "\u20ac"; // euro sign

    public static Config getInstance() {
        return ourInstance;
    }

    private Language language = new Language("en", "English", Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()));

    private Config() {
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
