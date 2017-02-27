package lu.btsi.bragi.ros.client.connection;

/**
 * Created by gillesbraun on 27/02/2017.
 */
public interface ConnectionCallback {
    void connectionClosed(String reason);

    void connectionOpened(String message);
}
