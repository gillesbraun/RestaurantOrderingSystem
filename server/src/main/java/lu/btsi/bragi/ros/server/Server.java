package lu.btsi.bragi.ros.server;

import com.google.inject.Inject;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.server.controller.Controller;
import lu.btsi.bragi.ros.server.controller.MainController;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.jooq.DSLContext;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * Created by gillesbraun on 13/02/2017.
 */
public class Server extends WebSocketServer implements IMessageSender {
    @Inject
    private DSLContext create;

    @Inject
    private MainController mainController;

    private ArrayList<WebSocket> clients = new ArrayList<>();
    private Map<UUID, WebSocket> socketMap = new HashMap<>();

    public Server() {
        super(new InetSocketAddress(8887));
        Controller.setMessageSender(this);
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
        try {
            UUID uuid = Message.messageUUID(message);
            socketMap.put(uuid, conn);
            Optional<Message> answer = mainController.sendToRightController(message);
            if(answer.isPresent()) {
                conn.send(answer.get().toString());
            }
            socketMap.remove(uuid);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (MessageException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        if(ex.getClass().equals(java.net.BindException.class)) {
            System.err.println("Address already in use.");
            System.exit(1);
        }
        ex.printStackTrace();
    }

    public void broadcast(Message message) {
        clients.forEach(webSocket -> {
            webSocket.send(message.toString());
        });
    }

    @Override
    public void sendReply(Message newMessage) {
        WebSocket conn = socketMap.get(newMessage.getMessageID());
        if(conn != null) {
            conn.send(newMessage.toString());
        }
    }
}
