package lu.btsi.bragi.ros.server;

import com.google.gson.Gson;
import com.google.inject.Inject;
import lu.btsi.bragi.ros.server.database.tables.Table;
import lu.btsi.bragi.ros.server.database.tables.records.TableRecord;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.jooq.DSLContext;
import org.jooq.Result;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gillesbraun on 13/02/2017.
 */
public class Server extends WebSocketServer {
    @Inject
    private DSLContext create;

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
        System.out.println("in message: "+message);
        if(message.equals("/get/tables")) {
            Result<TableRecord> fetch = create.fetch(Table.TABLE);

            List<lu.btsi.bragi.ros.models.Table> into = fetch.into(lu.btsi.bragi.ros.models.Table.class);

            conn.send(new Gson().toJson(into));
        } else if(message.equals("ping")) {
            conn.send("pong");
        } else {
            conn.send("You said: "+message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println(ex);
    }
}
