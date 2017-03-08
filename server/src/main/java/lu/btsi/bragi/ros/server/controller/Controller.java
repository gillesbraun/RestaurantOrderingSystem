package lu.btsi.bragi.ros.server.controller;

import com.google.inject.Inject;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageType;
import lu.btsi.bragi.ros.server.IMessageSender;
import org.jooq.DSLContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by gillesbraun on 15/02/2017.
 */
public abstract class Controller<T> {

    @Inject
    protected DSLContext context;

    private static Map<Class<?>, Controller> registeredControllers = new HashMap<>();
    static IMessageSender messageSender;

    public Controller(Class<T> type) {
        registeredControllers.put(type, this);
    }

    static Optional<Message> sendToController(String message) throws ControllerNotFoundException, ClassNotFoundException, MessageException {
        Class clazz = Message.messageClass(message);
        Controller<?> controller = registeredControllers.get(clazz);
        if(controller != null) {
            return controller.handle(message);
        }
        String controllers = registeredControllers.keySet().stream().map(Class::getName).collect(Collectors.joining(", "));
        throw new ControllerNotFoundException("No Controller found for class "+clazz +". Available controllers: "+ controllers);
    }

    protected abstract List<T> handleGet() throws Exception;


    protected void handleCreate(T obj, Message<T> originalMessage) throws Exception {
        handleCreate(obj);
    }

    protected void handleDelete(T obj, Message<T> originalMessage) throws Exception {
        handleDelete(obj);
    }

    protected void handleUpdate(T obj, Message<T> originalMessage) throws Exception {
        handleUpdate(obj);
    }

    protected abstract void handleUpdate(T obj) throws Exception;

    protected abstract void handleCreate(T obj) throws Exception;

    protected abstract void handleDelete(T obj) throws Exception;

    private Optional<Message> handle(String text) throws MessageException {
        Message<T> message = new Message<>(text);
        List<T> payload = message.getPayload();
        try {
            if (message.getType() == MessageType.Get) {
                List<T> newPayload = handleGet();
                Message<T> answer = message.createAnswer(newPayload);
                return Optional.of(answer);
            }
            for (T pojo : payload) {
                if (message.getType() == MessageType.Update) {
                    handleUpdate(pojo, message);
                } else if (message.getType() == MessageType.Delete) {
                    handleDelete(pojo, message);
                } else if (message.getType() == MessageType.Create) {
                    handleCreate(pojo, message);
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(message.createAnswerException(e, e.getClass()));
        }
    }

    public static void setMessageSender(IMessageSender messageSender) {
        Controller.messageSender = messageSender;
    }

    static <T extends Controller> T getController(Class<T> controllerClass) {
        Optional<Controller> first = registeredControllers.values().stream()
                .filter(controller -> controller.getClass().equals(controllerClass))
                .findFirst();
        if (first.isPresent()) {
            return controllerClass.cast(first.get());
        } else {
            throw new ControllerNotFoundException("Controller '" + controllerClass.getSimpleName() + "' is not loaded");
        }
    }
}
