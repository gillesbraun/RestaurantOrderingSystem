package lu.btsi.bragi.ros.server.controller;

import com.google.inject.Inject;
import lu.btsi.bragi.ros.models.message.*;
import lu.btsi.bragi.ros.server.MessageSender;
import org.jooq.DSLContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by gillesbraun on 15/02/2017.
 */
public abstract class Controller<T> {
    @Inject protected DSLContext context;

    static MessageSender messageSender;

    /**
     * Stores the instances of all available controllers, where the key is the class of the object that each controller
     * can handle.
     */
    private static Map<Class<?>, Controller> registeredControllers = new HashMap<>();

    /**
     * For a given controller, maps all available QueryTypes to a function that is executed when such a Type is received.
     */
    private Map<QueryType, Function<Query, List<T>>> queryHandlers = new HashMap<>();

    /**
     * Registers a controller to handle the Type of object specified.
     * @param type The Type of object to handle
     */
    public Controller(Class<T> type) {
        registeredControllers.put(type, this);
    }

    /**
     * Registers a function to be run when a specified <code>QueryType</code> is requested.
     * @param queryType <code>QueryType</code> to handle
     * @param handler Function that returns a list of objects that match the query
     */
    void registerCustomQueryHandler(QueryType queryType, Function<Query, List<T>> handler) {
        queryHandlers.put(queryType, handler);
    }

    /**
     * Handles the creation of the specified object <code>obj</code>, with access to the original Message which
     * can be used for a reply Message. If not overridden, this calls the handleCreate(T) method.
     * @param obj Object to create
     * @param originalMessage Message from the client
     * @throws Exception Catches any exceptions and forwards them to the client
     */
    protected void handleCreate(T obj, Message<T> originalMessage) throws Exception {
        handleCreate(obj);
    }

    /**
     * Handles the deletion of the specified object <code>obj</code>, with access to the original Message which
     * can be used for a reply Message. If not overridden, this calls the handleDelete(T) method.
     * @param obj Object to delete
     * @param originalMessage Message from the client
     * @throws Exception Catches any exceptions and forwards them to the client
     */
    protected void handleDelete(T obj, Message<T> originalMessage) throws Exception {
        handleDelete(obj);
    }

    /**
     * Handles the updating of the specified object <code>obj</code>, with access to the original Message which
     * can be used for a reply Message. If not overridden, this calls the handleUpdate(T) method.
     * @param obj Object to update
     * @param originalMessage Message from the client
     * @throws Exception Catches any exceptions and forwards them to the client
     */
    protected void handleUpdate(T obj, Message<T> originalMessage) throws Exception {
        handleUpdate(obj);
    }

    /**
     * Returns a List of Objects.
     * @return The Objects
     * @throws Exception Catches any exceptions and forwards them to the client
     */
    protected abstract List<T> handleGet() throws Exception;

    /**
     * Handles the updating of a single object. The object should first be queried from the Server so it has the
     * identifying properties used for updating.
     * @param obj The new object
     * @throws Exception Catches any exceptions and forwards them to the client
     */
    protected abstract void handleUpdate(T obj) throws Exception;

    /**
     * Handles the creating of a single object.
     * @param obj The new object
     * @throws Exception Catches any exceptions and forwards them to the client
     */
    protected abstract void handleCreate(T obj) throws Exception;

    /**
     * Handles the deletion of a single object. The object only needs to have the identifying properties set in order
     * to delete.
     * @param obj The new object
     * @throws Exception Catches any exceptions and forwards them to the client
     */
    protected abstract void handleDelete(T obj) throws Exception;

    /**
     * Decodes the Message, decodes any Query Parameters and forwards it to the appropriate methods.
     * @param text The raw Message String from the client
     * @return Optional Message, because some methods don't need to send a response
     * @throws MessageException Thrown when the message is not properly formatted.
     */
    private Optional<Message> handle(String text) throws MessageException {
        Message<T> message = new Message<>(text);
        if(message.getType().equals(MessageType.GetQuery)) {
            return Optional.of(handleCustomQuery(message));
        }
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

    /**
     * Returns an answer Message containing the results of the Query.
     * @param message Message from the client
     * @return Answer to the Message from the client
     */
    private Message handleCustomQuery(Message<T> message) {
        Query query = message.getQuery();
        if(queryHandlers.containsKey(query.getQueryType())) {
            Function<Query, List<T>> listFunction = queryHandlers.get(query.getQueryType());
            List<T> list = listFunction.apply(query);
            return message.createAnswer(list);
        } else {
            throw new ControllerNotFoundException(
                    "Controller "+ getClass().getSimpleName() +" has not registered the custom query type: "+query.getQueryType());
        }
    }

    /**
     * Sends the message to the appropriate controller, and returns an Optional Message containing the answer.
     * @param message The raw Message from the client
     * @return Optional Message containing the answer
     * @throws ControllerNotFoundException Thrown when no controller is registered for the Type of object in the requested Message
     * @throws ClassNotFoundException Thrown when the requested Object doesn't exist
     * @throws MessageException Thrown when the incoming Message is not properly formatted
     */
    static Optional<Message> sendToController(String message) throws ControllerNotFoundException, ClassNotFoundException, MessageException {
        Class clazz = Message.messageClass(message);
        Controller<?> controller = registeredControllers.get(clazz);
        if(controller != null) {
            return controller.handle(message);
        }
        String controllers = registeredControllers.keySet().stream().map(Class::getName).collect(Collectors.joining(", "));
        throw new ControllerNotFoundException("No Controller found for class "+clazz +". Available controllers: "+ controllers);
    }

    /**
     * Gets the instance of a controller with the proper type.
     * You can use it as follows:
     * <code>
     *     OrderController orderController = getController(OrderController.class);
     * </code>
     * @param controllerClass The Class of the controller you want to use
     * @return The instance of the requested controller
     * @throws ControllerNotFoundException Thrown when the requested controller is not loaded
     */
    static <T extends Controller> T getController(Class<T> controllerClass) throws ControllerNotFoundException {
        Optional<Controller> first = registeredControllers.values().stream()
                .filter(controller -> controller.getClass().equals(controllerClass))
                .findFirst();
        if (first.isPresent()) {
            return controllerClass.cast(first.get());
        } else {
            throw new ControllerNotFoundException("Controller '" + controllerClass.getSimpleName() + "' is not loaded");
        }
    }

    public static void setMessageSender(MessageSender messageSender) {
        Controller.messageSender = messageSender;
    }
}
