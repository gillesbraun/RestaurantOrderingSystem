package lu.btsi.bragi.ros.server;

import lu.btsi.bragi.ros.models.message.Message;

/**
 * Created by gillesbraun on 06/03/2017.
 */
public interface MessageSender {
    void broadcast(Message m);

    void reply(Message newMessage);
}
