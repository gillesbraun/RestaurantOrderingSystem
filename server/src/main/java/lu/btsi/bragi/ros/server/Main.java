package lu.btsi.bragi.ros.server;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.Files;

/**
 * Created by gillesbraun on 14/02/2017.
 */
public class Main {

    public static void main(String[] args) {
        File configFile = new File("ros.conf");
        if(!configFile.exists())
            try {
                InputStream reference = Main.class.getResourceAsStream("/reference.conf");
                Files.copy(reference, configFile.toPath());
                System.err.println("Please fill out the configuration file that has been created: " + configFile.getAbsolutePath());
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        Injector injector = Guice.createInjector(new DatabaseModule());

        Server main = injector.getInstance(Server.class);
        main.start();

        new WebServerImages(8888);

        try {
            JmDNS jmDNS = JmDNS.create(InetAddress.getLocalHost());
            ServiceInfo serviceInfo = ServiceInfo.create("_ws._tcp.local.", "RosWebsocket", "RosWebsocket", 8887, "");
            jmDNS.registerService(serviceInfo);
            System.out.println("Registered service to ip: "+ serviceInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("listening on port " + main.getPort());
    }
}
