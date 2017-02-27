package lu.btsi.bragi.ros.client;

/**
 * Created by gillesbraun on 14/02/2017.
 */
public interface UICallback {
    void connectionOpened(String message, Client client);

    void displayMessage(String text);
}
