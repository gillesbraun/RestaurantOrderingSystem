import lu.btsi.bragi.ros.models.message.Message;

/**
 * Created by gillesbraun on 17/02/2017.
 */
public interface MessageCallback {
    void handleAnswer(Message m);
}
