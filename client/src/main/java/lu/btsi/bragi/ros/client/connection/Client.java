package lu.btsi.bragi.ros.client.connection;

import lu.btsi.bragi.ros.client.MessageCallback;
import lu.btsi.bragi.ros.models.message.Message;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gillesbraun on 13/02/2017.
 */
public class Client extends WebSocketClient {
    private Map<UUID, MessageCallback> callbackMap = new HashMap<>();
    private ConnectionCallback connectionCallback;

    public Client(URI serverURI) {
        super(serverURI);
    }

    public void sendWithAction(Message message, MessageCallback messageCallback) {
        String messageString = message.toString();
        send(messageString);
        UUID messageID = message.getMessageID();
        callbackMap.put(messageID, messageCallback);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if(connectionCallback != null){
            connectionCallback.connectionOpened("Connection opened with: "+getURI());
        }
    }

    @Override
    public void onMessage(String message) {
        UUID messageUUID = Message.messageUUID(message);
        MessageCallback messageCallback = callbackMap.get(messageUUID);
        if(messageCallback != null) {
            messageCallback.handleAnswer(message);
            callbackMap.remove(messageUUID);
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
