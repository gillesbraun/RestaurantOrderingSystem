package lu.btsi.bragi.ros.server.controller;

import com.google.inject.Inject;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageType;
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

    private static Map<Class, Controller> registeredControllers = new HashMap<>();

    public Controller(Class type) {
        registeredControllers.put(type, this);
    }

    protected static Optional<Message> sendToController(String message) throws ControllerNotFoundException, ClassNotFoundException {
        System.out.println("choosing controller");
        Class clazz = Message.messageClass(message);
        Controller controller = registeredControllers.get(clazz);
        if(controller != null) {
            return controller.handle(message, clazz);
        }
        String controllers = registeredControllers.keySet().stream().map(Class::getName).collect(Collectors.joining(", "));
        throw new ControllerNotFoundException("No Controller found for class "+clazz +". Available controllers: "+ controllers);
    }

    protected abstract Message handleGet();

    protected abstract void handleUpdate(T obj);

    protected abstract void handleCreate(T obj);

    protected abstract void handleDelete(T obj);

    public Optional<Message> handle(String text, Class clazz) {
        Message<T> message = new Message<T>(text, clazz);
        List<T> payload = message.getPayload();
        if(message.getAction().equals(MessageType.Get)) {
            return Optional.of(handleGet());
        }
        T obj = payload.get(0);
        switch (message.getAction()) {
            case Get:
                break;
            case Update:
                handleUpdate(obj);
                break;
            case Delete:
                handleDelete(obj);
                break;
            case Create:
                handleCreate(obj);
                break;
            case Answer:
                // do nothing
                break;
        }
        return Optional.empty();
    }
}
