package lu.btsi.bragi.ros.client;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by gillesbraun on 27/02/2017.
 */
public class ConnectionManager implements ServiceListener, ConnectionCallback {

    private final UICallback UICallback;
    private Client client;

    public ConnectionManager(UICallback UICallback) {
        this.UICallback = UICallback;
    }

    public void newClient() {
        try {
            JmDNS jmDNS = JmDNS.create(InetAddress.getLocalHost());
            jmDNS.addServiceListener("_ws._tcp.local.", this);
            UICallback.displayMessage("Looking for ROS Server...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionClosed(String reason) {
        UICallback.displayMessage("Connection closed. Retrying in 2 seconds...");
        new Thread(new javafx.concurrent.Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                newClient();
                return null;
            }
        }).start();
    }

    @Override
    public void connectionOpened(String message) {
        UICallback.connectionOpened(message, client);
    }

    @Override
    public void serviceAdded(ServiceEvent event) {
        if(event.getInfo().getSubtype().equals("roswebsocket")){
            try {
                client = new Client(new URI("ws://" + event.getDNS().getName() + ":8887"));
                client.setConnectionCallback(this);
                client.connect();
            } catch (URISyntaxException e) {
            }
        } else {
            UICallback.displayMessage("Found other webservice ("+event.getInfo().getSubtype()+"), ignoring");
        }
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {

    }

    @Override
    public void serviceResolved(ServiceEvent event) {

    }
}
