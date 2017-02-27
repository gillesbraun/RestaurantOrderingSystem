package lu.btsi.bragi.ros.client.connection;

import lu.btsi.bragi.ros.client.MessageCallback;
import lu.btsi.bragi.ros.models.message.Message;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by gillesbraun on 13/02/2017.
 */
public class Client extends WebSocketClient {
    private MessageCallback latestCallback;
    private ConnectionCallback connectionCallback;

    public Client(URI serverURI) {
        super(serverURI);
    }

    public void sendWithAction(Message message, MessageCallback messageCallback) {
        String messageString = message.toString();
        send(messageString);
        this.latestCallback = messageCallback;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if(connectionCallback != null){
            connectionCallback.connectionOpened("Connection opened with: "+getURI());
        }
    }

    @Override
    public void onMessage(String message) {
        if(latestCallback != null) {
            latestCallback.handleAnswer(message);
            latestCallback = null;
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if(connectionCallback != null){
            connectionCallback.connectionClosed(reason);
        }
    }

    @Override
    public void onError(Exception ex) {
        if(!ex.getLocalizedMessage().contains("Connection refused")) {
            ex.printStackTrace();
        }
    }

    public void setConnectionCallback(ConnectionCallback connectionCallback) {
        this.connectionCallback = connectionCallback;
    }
}
