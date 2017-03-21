package lu.btsi.bragi.ros.client.connection;

import javafx.application.Platform;
import lu.btsi.bragi.ros.client.MessageCallback;
import lu.btsi.bragi.ros.client.settings.Config;
import lu.btsi.bragi.ros.client.settings.ConnectionSettings;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageType;
import org.controlsfx.dialog.ExceptionDialog;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by gillesbraun on 27/02/2017.
 */
public class ConnectionManager implements ServiceListener, ConnectionCallback, MessageCallback {
    private final List<ConnectionCallback> connectionCallbacks = new ArrayList<>();
    private final List<BroadcastCallback> broadcastCallbacks = new ArrayList<>();
    private final Map<UUID, MessageCallback> callbackMap = new HashMap<>();
    private final UICallback uiCallback;
    private Client client;
    private final Set<Message> unsentMessages = new HashSet<>();
    private boolean isConnected;
    private boolean queueRunning = false;

    public static void init(UICallback uiCallback) {
        ourInstance = new ConnectionManager(uiCallback);
    }

    private static ConnectionManager ourInstance;

    private ConnectionManager(UICallback uiCallback) {
        this.uiCallback = uiCallback;
        newClient();
    }

    public static ConnectionManager getInstance() {
        return ourInstance;
    }

    public void newClient() {
        ConnectionSettings connectionSettings = Config.getInstance().connectionSettings;
        if(!connectionSettings.isFirstConnection() && !connectionSettings.isAutoDiscovery()) {
            newClientIP(connectionSettings.getHostAddress());
            return;
        }
        if(connectionSettings.isAutoDiscovery()) {
            newClientAutoDiscover();
        } else {
            newClientIP(connectionSettings.getHostAddress());
        }
    }

    private void newClientAutoDiscover() {
        try {
            JmDNS jmDNS = JmDNS.create(InetAddress.getLocalHost());
            jmDNS.addServiceListener("_ws._tcp.local.", this);
            uiCallback.displayMessage("Looking for ROS Server...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newClientIP(String ip) {
        client = new Client(URI.create("ws://"+ip+":8887"));
        client.setConnectionCallback(this);
        client.setMessageCallback(this);
        client.connect();
    }

    public void send(Message message) {
        try {
            if(client != null && message != null && isConnected)
                client.send(message.toString());
        } catch (WebsocketNotConnectedException e) {
            queueMessageForLater(message);
        }
    }

    public void sendWithAction(Message message, MessageCallback messageCallback) {
        if(client != null && message != null && isConnected) {
            send(message);
            if(messageCallback != null) {
                UUID messageID = message.getMessageID();
                callbackMap.put(messageID, messageCallback);
            }
        }
    }

    private void queueMessageForLater(Message message) {
        unsentMessages.add(message);
        startQueueTimer();
    }

    private void startQueueTimer() {
        ScheduledExecutorService s = new ScheduledThreadPoolExecutor(1);
        s.scheduleWithFixedDelay(trySending, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    private Runnable trySending = () -> {
        synchronized (unsentMessages) {
            if (!queueRunning && unsentMessages.size() > 0) {
                queueRunning = true;
                System.out.println("Running queue");
                List<Message> sendNext = new ArrayList<>();
                for (Iterator<Message> it = unsentMessages.iterator(); it.hasNext(); ) {
                    Message message = it.next();
                    sendNext.add(message);
                    it.remove();
                }
                for (Message message : sendNext) {
                    send(message);
                }
                queueRunning = false;
            }
        }
    };

    @Override
    public void connectionClosed(String reason) {
        isConnected = false;
        uiCallback.displayMessage("Connection closed. Retrying in 2 seconds...");
        connectionCallbacks.forEach(c -> c.connectionClosed(reason));
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
        isConnected = true;
        uiCallback.connectionOpened(message, client);
        connectionCallbacks.forEach(c -> c.connectionOpened(message));
        ConnectionSettings connectionSettings = Config.getInstance().connectionSettings;
        connectionSettings.setFirstConnection(false);
        connectionSettings.setHostAddress(getRemoteIPAdress());
        try {
            Config.save();
        } catch (IOException e) {
            System.err.println("Error saving settings: "+e.getMessage());
        }
    }

    @Override
    public void serviceAdded(ServiceEvent event) {
        if(event.getInfo().getSubtype().equals("roswebsocket")){
            try {
                client = new Client(new URI("ws://" + event.getDNS().getName() + ":8887"));
                client.setConnectionCallback(this);
                client.setMessageCallback(this);
                client.connect();
            } catch (URISyntaxException e) {
            }
        } else {
            uiCallback.displayMessage("Found other webservice ("+event.getInfo().getSubtype()+"), ignoring");
        }
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {

    }

    @Override
    public void serviceResolved(ServiceEvent event) {

    }

    public String getRemoteIPAdress() {
        return client.getRemoteIPAdress();
    }

    public static boolean isConnected() {
        return ourInstance != null && ourInstance.isConnected;
    }

    public void close() {
        client.close();
    }

    public void addConnectionCallback(ConnectionCallback connectionCallback) {
        connectionCallbacks.add(connectionCallback);
        if(isConnected) {
            connectionCallback.connectionOpened("Connection was already open.");
        }
    }

    public void addBroadcastCallback(BroadcastCallback broadcastCallback) {
        broadcastCallbacks.add(broadcastCallback);
    }

    @Override
    public void handleAnswer(String message) {
        boolean isError = Message.messageType(message).equals(MessageType.Error);
        UUID messageUUID = Message.messageUUID(message);
        MessageCallback messageCallback = callbackMap.get(messageUUID);
        if(messageCallback != null) {
            Platform.runLater(() -> messageCallback.handleAnswer(message));
            callbackMap.remove(messageUUID);
        } else {
            if(!isError) {
                if(Message.messageType(message).equals(MessageType.Broadcast)) {
                    broadcastCallbacks.forEach(BroadcastCallback::handleBroadCast);
                }
            } else {
                try {
                    new Message(message);
                    // should not reach here
                } catch (MessageException e) {
                    Platform.runLater(() -> {
                        ExceptionDialog exceptionDialog = new ExceptionDialog(e);
                        exceptionDialog.show();
                    });
                }
            }
        }
    }
}
