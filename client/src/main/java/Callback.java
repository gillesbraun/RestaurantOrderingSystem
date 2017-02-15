/**
 * Created by gillesbraun on 14/02/2017.
 */
public interface Callback {
    void handleCallback(String message);

    void connectionClosed(String message);

    void connectionOpened(String message);
}
