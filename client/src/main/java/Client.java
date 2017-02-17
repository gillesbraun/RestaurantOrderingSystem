import lu.btsi.bragi.ros.models.message.Message;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by gillesbraun on 13/02/2017.
 */
public class Client extends WebSocketClient {
    private Callback mainCallback;

    public void setMainCallback(Callback mainCallback) {
        this.mainCallback = mainCallback;
    }

    public Client(URI serverURI) {
        super(serverURI);
    }

    public void sendWithAction(Message message, MessageCallback messageCallback) {
        String messageString = message.toString();
        send(messageString);
        this.latestCallback = messageCallback;
    }
    private MessageCallback latestCallback;

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if(mainCallback != null){
            mainCallback.connectionOpened("Connection opened with: "+getURI());
        }
    }

    @Override
    public void onMessage(String message) {
        System.out.println("incoming message: "+message);

        if(latestCallback != null) {
            try {
                    Message decoded = Message.fromString(message);
                    latestCallback.handleAnswer(decoded);
                    latestCallback = null;
            } catch (ClassNotFoundException e) {}
        } else if(mainCallback != null){
            mainCallback.handleCallback(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if(mainCallback != null){
            mainCallback.connectionClosed(reason);
        }
    }

    @Override
    public void onError(Exception ex) {
        if(!ex.getLocalizedMessage().contains("Connection refused")) {
            ex.printStackTrace();
        }
    }
}
