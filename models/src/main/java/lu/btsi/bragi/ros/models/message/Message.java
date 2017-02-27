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

    @SuppressWarnings("unchecked")
    public Message(String encodedMessage, Class clazz) {
        isMessageValid(encodedMessage);
        String[] split = encodedMessage.split(SEPARATOR);
        this.action = MessageType.get(split[0]);
        this.clazz = clazz;
        if(split.length == 3) {
            Gson gson = new Gson();
            ListOfType listWithType = new ListOfType<>(clazz);
            this.payload = gson.fromJson(split[2], listWithType);
        } else {
            this.payload = null;
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
