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
    private ConnectionCallback connectionCallback;
    private MessageCallback messageCallback;

    public Client(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if(connectionCallback != null){
            connectionCallback.connectionOpened("Connection opened with: "+getURI());
        }
    }

    public void send(Message message) {
        send(message.toString());
    }

    @Override
    public void onMessage(String message) {
        messageCallback.handleAnswer(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if(connectionCallback != null){
            connectionCallback.connectionClosed(reason);
        }
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        if(ex != null && !ex.getLocalizedMessage().contains("Connection refused")) {
            ex.printStackTrace();
        }
    }

    void setMessageCallback(MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    public String getRemoteIPAdress() {
        return getConnection().getRemoteSocketAddress().getAddress().getHostAddress();
    }

    public void setConnectionCallback(ConnectionManager connectionCallback) {
        this.connectionCallback = connectionCallback;
    }
}
