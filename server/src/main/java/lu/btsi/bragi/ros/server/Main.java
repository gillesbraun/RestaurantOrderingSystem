package lu.btsi.bragi.ros.server;

import com.google.common.io.Files;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by gillesbraun on 14/02/2017.
 */
public class Main {

    public static void main(String[] args) {
        File configFile = new File("ros.conf");
        if(!configFile.exists())
            try {
                URL reference = Main.class.getResource("/reference.conf");
                Files.copy(new File(reference.toURI()), configFile);
                System.err.println("Please fill out the configuration file that has been created: " + configFile.getAbsolutePath());
                System.exit(0);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        Injector injector = Guice.createInjector(new DatabaseModule());

        Server main = injector.getInstance(Server.class);
        main.start();

        try {
            JmDNS jmDNS = JmDNS.create(InetAddress.getLocalHost());
            ServiceInfo serviceInfo = ServiceInfo.create("_ws._tcp.local.", "RosWebsocket", "RosWebsocket", 8887, "");
            jmDNS.registerService(serviceInfo);
            System.out.println("Registered service to ip: "+ serviceInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("listening on port " + main.getPort());

        new WebServerImages(8888);
    }
}
