package lu.btsi.bragi.ros.server;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by gillesbraun on 14/02/2017.
 */
public class Main {

    public static void main(String[] args) {
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
