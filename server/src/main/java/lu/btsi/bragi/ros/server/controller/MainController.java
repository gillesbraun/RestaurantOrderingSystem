package lu.btsi.bragi.ros.server.controller;

import com.google.inject.Inject;
import lu.btsi.bragi.ros.models.message.Message;

import java.util.Optional;

/**
 * Created by gillesbraun on 15/02/2017.
 */
public class MainController {
    @Inject
    TableController tableController;

    @Inject
    WaiterController waiterController;

    public Optional<Message> sendToRightController(Message message) {
        return Controller.sendToController(message);
    }
}
