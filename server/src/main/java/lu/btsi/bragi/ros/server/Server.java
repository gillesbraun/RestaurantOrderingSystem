package lu.btsi.bragi.ros.server;

import com.google.inject.Inject;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.server.controller.MainController;
import lu.btsi.bragi.ros.server.controller.TableController;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.jooq.DSLContext;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by gillesbraun on 13/02/2017.
 */
public class Server extends WebSocketServer {
    @Inject
    private DSLContext create;

    @Inject
    private MainController mainController;

    ArrayList<WebSocket> clients = new ArrayList<>();

    public Server() {
        super(new InetSocketAddress(8887));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println(message);
        try {
            Message in = Message.fromString(message);
            Optional<Message> answer = mainController.sendToRightController(in);
            if(answer.isPresent()) {
                conn.send(answer.get().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            conn.send("Message could not be decoded: "+ e.getClass().getSimpleName() +" " + e.getMessage());
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println(ex);
    }
}
