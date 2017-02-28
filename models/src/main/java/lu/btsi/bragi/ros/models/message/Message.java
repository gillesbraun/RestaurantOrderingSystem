package lu.btsi.bragi.ros.models.message;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gillesbraun on 15/02/2017.
 */
public class Message<T> {
    private final MessageType action;
    private final Class clazz;
    private final List<T> payload;

    private static final String SEPARATOR = ";;";

    /**
     * Decodes the encodedMessage and assigns the content to the new instance. It is required to define the Type of
     * the elements that are stored in the message, to be type-safe. Sou if you want to decode a Message that should
     * have the "Waiter" Type, you would call: <code>Message&lt;Waiter&gt; decoded = new Message&lt;&gt;(rawMessage);</code>
     * @param encodedMessage
     */
    @SuppressWarnings("unchecked")
    public Message(String encodedMessage) {
        try {
            isMessageValid(encodedMessage);
            String[] split = encodedMessage.split(SEPARATOR);
            this.action = MessageType.get(split[0]);
            clazz = Class.forName(split[1]);
            if(split.length == 3) {
                Gson gson = new Gson();
                ListOfType listWithType = new ListOfType<>(clazz);
                this.payload = gson.fromJson(split[2], listWithType);
            } else {
                this.payload = null;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not read message.");
        }
    }

    public Message(MessageType action, List<T> content, Class<T> clazz) {
        this.action = action;
        this.payload = content;
        this.clazz = clazz;
    }

    public Message(MessageType action, T item) {
        this.action = action;
        this.payload = new ArrayList<T>(Arrays.asList(item));
        this.clazz = item.getClass();
    }

    protected Message(MessageType action, Class<T> clazz) {
        this.action = action;
        this.payload = null;
        this.clazz = clazz;
    }

    public static Class messageClass(String encoded) throws ClassNotFoundException {
        isMessageValid(encoded);
        String[] split = encoded.split(SEPARATOR);
        return Class.forName(split[1]);
    }

    private static void isMessageValid(String str) throws MessageMalformedException {
        String[] split = str.split(SEPARATOR);
        if(split.length != 2 && split.length != 3) {
            throw new MessageMalformedException("Message must consist of 2 or 3 parts. Action"+SEPARATOR+"Class["+SEPARATOR+"Json]");
        }

        MessageType messageType = MessageType.get(split[0]);
        if(messageType == null) {
            throw new MessageMalformedException("Message Type '"+split[0]+"' does not exist.");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        if(payload != null) {
            String json = new Gson().toJson(payload, new ListOfType<>(clazz));
            return action.getName() + SEPARATOR + clazz.getCanonicalName() + SEPARATOR + json;
        } else {
            return action.getName() + SEPARATOR + clazz.getCanonicalName();
        }
    }

    public MessageType getAction() {
        return action;
    }

    public List<T> getPayload() {
        return payload;
    }
}
