package lu.btsi.bragi.ros.models.message;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Message Class used providing standardised communication between server and client. It provides multiple constructors
 * for different use cases.
 * Created by gillesbraun on 15/02/2017.
 */
public class Message<T> {
    private final MessageType type;
    private final Class clazz;
    private final List<T> payload;

    private static final String SEPARATOR = ";;";

    /**
     * Decodes the encodedMessage and assigns the content to the new instance. It is required to define the Type of
     * the elements that are stored in the message, to be type-safe. Sou if you want to decode a Message that should
     * have the "Waiter" Type, you would call: <code>Message&lt;Waiter&gt; decoded = new Message&lt;&gt;(rawMessage);</code>
     * @param encodedMessage The raw string that is sent between client and server
     */
    @SuppressWarnings("unchecked")
    public Message(String encodedMessage) {
        try {
            isMessageValid(encodedMessage);
            String[] split = encodedMessage.split(SEPARATOR);
            this.type = MessageType.get(split[0]);
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

    /**
     * Creates a new Message with a list of elements of the Type
     * @param type The MessageType (Get, Delete, ...)
     * @param content A list of elements to encapsulate
     * @param clazz Class of the element you want to encapsulate
     */
    public Message(MessageType type, List<T> content, Class<T> clazz) {
        if(content == null) {
            throw new IllegalArgumentException("List must not be null.");
        }
        this.type = type;
        this.payload = content;
        this.clazz = clazz;
    }

    /**
     * Creates a Message with a single element as payload
     * @param type The MessageType (Get, Delete, ...)
     * @param item The element you want to encapsulate
     */
    public Message(MessageType type, T item) {
        this.type = type;
        this.payload = new ArrayList<T>(Arrays.asList(item));
        this.clazz = item.getClass();
    }

    /**
     * Creates a Message, which does not contain any data as payload. This is used for the <code>MessageType.Get</code>,
     * as it does not need to have content to be sent
     * @param type The <code>MessageType</code>, which probably should be Get
     * @param clazz The class of the Object you want to retrieve
     */
    protected Message(MessageType type, Class<T> clazz) {
        this.type = type;
        this.payload = null;
        this.clazz = clazz;
    }

    /**
     * Returns the class of the provided Message
     * @param encoded The raw message as it is sent between client and server
     * @return The class of the object encapsulated in the message
     * @throws ClassNotFoundException
     */
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
            return type.getName() + SEPARATOR + clazz.getCanonicalName() + SEPARATOR + json;
        } else {
            return type.getName() + SEPARATOR + clazz.getCanonicalName();
        }
    }

    public MessageType getType() {
        return type;
    }

    public List<T> getPayload() {
        return payload;
    }
}
