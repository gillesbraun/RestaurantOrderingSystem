package lu.btsi.bragi.ros.client.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by Gilles Braun on 18.03.2017.
 */
public class Config {
    private static Config ourInstance;

    public InvoiceSettings invoiceSettings;
    public GeneralSettings generalSettings;
    public ConnectionSettings connectionSettings;

    public static Config getInstance() {
        return ourInstance;
    }

    private Config() {
    }

    public static void init() {
        try {
            if(new File("settings.json").exists()) {
                ourInstance = new Gson().fromJson(new BufferedReader(new FileReader("settings.json")), Config.class);
            } else {
                ourInstance = new Config();
                ourInstance.invoiceSettings = new InvoiceSettings();
                ourInstance.generalSettings = new GeneralSettings();
                ourInstance.connectionSettings = new ConnectionSettings();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void save() throws IOException {
        String settings = new GsonBuilder().setPrettyPrinting().create().toJson(ourInstance, Config.class);
        try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("settings.json"), StandardCharsets.UTF_8), true)) {
            pw.print(settings);
        }
    }
}
