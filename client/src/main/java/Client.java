import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by gillesbraun on 13/02/2017.
 */
public class Client extends WebSocketClient {
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Client(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if(callback != null){
            callback.connectionOpened("Connection opened with: "+getURI());
        }
    }

    @Override
    public void onMessage(String message) {
        System.out.println("incoming message: "+message);
        if(callback != null){
            callback.handleCallback(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if(callback != null){
            callback.connectionClosed(reason);
        }
    }

    @Override
    public void onError(Exception ex) {
        if(!ex.getLocalizedMessage().contains("Connection refused")) {
            ex.printStackTrace();
        }
    }
}
