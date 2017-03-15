package lu.btsi.bragi.ros.models.message;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;

/**
 * Message Class used providing standardised communication between server and client. It provides multiple constructors
 * for different use cases.
 * Created by gillesbraun on 15/02/2017.
 */
public class Message<T> {
    protected final MessageType type;
    protected final Class<? extends T> clazz;
    protected final UUID messageID;
    protected final List<T> payload;

    private static final String SEPARATOR = ";;";
    private static final int POS_MESSAGETYPE = 0;
    private static final int POS_CLASS = 1;
    private static final int POS_UUID = 2;
    private static final int POS_PAYLOAD = 3;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Timestamp.class, new TimestampSerializer())
            .registerTypeAdapter(Timestamp.class, new TimestampDeserializer())
            .create();

    /**
     * Decodes the encodedMessage and assigns the content to the new instance. It is required to define the Type of
     * the elements that are stored in the message, to be type-safe. Sou if you want to decode a Message that should
     * have the "Waiter" Type, you would call: <code>Message&lt;Waiter&gt; decoded = new Message&lt;&gt;(rawMessage);</code>
     * @param encodedMessage The raw string that is sent between client and server
     */
    @SuppressWarnings("unchecked")
    public Message(String encodedMessage) throws MessageException {
        try {
            isMessageValid(encodedMessage);
            String[] split = encodedMessage.split(SEPARATOR);
            this.type = MessageType.get(split[POS_MESSAGETYPE]);
            this.clazz = (Class<T>) Class.forName(split[POS_CLASS]);
            this.messageID = UUID.fromString(split[POS_UUID]);
            if(split.length == 4) {
                ListOfType listWithType = new ListOfType<>(clazz);
                this.payload = gson.fromJson(split[POS_PAYLOAD], listWithType);
            } else {
                this.payload = null;
            }
            if(Exception.class.isAssignableFrom(clazz)) {
                List<Throwable> throwables = (ArrayList<Throwable>)payload;
                if(throwables.size() > 0) {
                    throw new MessageException(throwables.get(0));
                } else {
                    throw new MessageException("Incoming message is exception. No specific data available.");
                }
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
    public Message(MessageType type, List<T> content, Class<? extends T> clazz) {
        if(content == null) {
            throw new IllegalArgumentException("List must not be null.");
        }
        this.type = type;
        this.payload = content;
        this.clazz = clazz;
        this.messageID = UUID.randomUUID();
    }

    /**
     * Creates a Message with a single element as payload
     * @param type The MessageType (Get, Delete, ...)
     * @param item The element you want to encapsulate
     */
    public Message(MessageType type, T item, Class<? extends T> clazz) {
        this.type = type;
        this.payload = new ArrayList<T>(Arrays.asList(item));
        this.clazz = clazz;
        this.messageID = UUID.randomUUID();
    }

    /**
     * Creates a Message, which does not contain any data as payload. This is used for the <code>MessageType.Get</code>,
     * as it does not need to have content to be sent
     * @param type The <code>MessageType</code>, which probably should be Get
     * @param clazz The class of the Object you want to retrieve
     */
    protected Message(MessageType type, Class<? extends T> clazz) {
        this.type = type;
        this.payload = null;
        this.clazz = clazz;
        this.messageID = UUID.randomUUID();
    }

    /**
     * Constructor used internally for specifying every aspect of the message.
     * @param type <code>MessageType</code> of the message
     * @param clazz The class representing the type of each element in the payload
     * @param messageID <code>UUID</code> which is used to identify messages and responses
     * @param payload List of elements to be sent
     */
    private Message(MessageType type, Class<? extends T> clazz, UUID messageID, List<T> payload) {
        this.type = type;
        this.clazz = clazz;
        this.messageID = messageID;
        this.payload = payload;
    }

    /**
     * Creates a Message of <code>MessageType.Answer</code> used to send answers to clients. This method also assigns
     * the answer the same UUID as the message you are creating it from, so the client knows to which request this
     * answer refers to.
     * @param payload The items to send
     * @return <code>Message</code> which contains the response and UUID for the request, and is of type
     * <code>MessageType.Answer</code>
     */
    public Message<T> createAnswer(List<T> payload) {
        return new Message<>(MessageType.Answer, clazz, messageID, payload);
    }

    /**
     * Creates a Message of <code>MessageType.Error</code> used to send exceptions to clients. The receiving party
     * can then handle the error in a meaningful way. This method also assigns the answer the same UUID as the message
     * you are creating it from, so the client knows which request produced the error.
     * @param throwable The <code>Exception</code> to send to the client.
     * @param clazz The Class
     * @return <code>Message</code> which contains an exception and is of type: <code>MessageType.Error</code>
     */
    public Message createAnswerException(Exception throwable, Class<? extends Exception> clazz) {
        ArrayList<Exception> al = new ArrayList<>(Collections.singletonList(throwable));
        return new Message<>(MessageType.Error, clazz, this.messageID, al);
    }

    /**
     * Returns the class of the provided Message from String
     * @param encoded The raw message as it is sent between client and server
     * @return The class of the object encapsulated in the message
     * @throws ClassNotFoundException When the class is not unknown
     */
    public static Class messageClass(String encoded) throws ClassNotFoundException {
        isMessageValid(encoded);
        String[] split = encoded.split(SEPARATOR);
        return Class.forName(split[POS_CLASS]);
    }

    /**
     * Returns the <code>UUID</code> for the provided Message
     * @param encoded The raw message as it is sent between client and server
     * @return The identifier for this message
     */
    public static UUID messageUUID(String encoded) {
        isMessageValid(encoded);
        String[] split = encoded.split(SEPARATOR);
        return UUID.fromString(split[POS_UUID]);
    }

    /**
     * Returns the <code>MessageType</code> for the provided message.
     * @param encoded The raw message string as it is sent
     * @return The <code>MessageType</code> of that Message
     */
    public static MessageType messageType(String encoded) {
        isMessageValid(encoded);
        String[] split = encoded.split(SEPARATOR);
        return MessageType.get(split[POS_MESSAGETYPE]);
    }

    private static void isMessageValid(String str) throws MessageMalformedException {
        String[] split = str.split(SEPARATOR);
        if(split.length != 3 && split.length != 4) {
            throw new MessageMalformedException("Message must consist of 2 or 3 parts. Action"+SEPARATOR+"Class["+SEPARATOR+"Json]");
        }

        MessageType messageType = MessageType.get(split[POS_MESSAGETYPE]);
        if(messageType == null) {
            throw new MessageMalformedException("Message Type '"+split[POS_MESSAGETYPE]+"' does not exist.");
        }
    }

    @Override
    public String toString() {
        if(payload != null) {
            String json = gson.toJson(payload, new ListOfType<>(clazz));
            return type.getName() + SEPARATOR + clazz.getCanonicalName() + SEPARATOR + messageID + SEPARATOR + json;
        } else {
            return type.getName() + SEPARATOR + clazz.getCanonicalName() + SEPARATOR + messageID;
        }
    }

    public UUID getMessageID() {
        return messageID;
    }

    public MessageType getType() {
        return type;
    }

    public List<T> getPayload() {
        return payload;
    }

    private class TimestampSerializer implements JsonSerializer<Timestamp> {
        @Override
        public JsonElement serialize(Timestamp src, Type typeOfSrc, JsonSerializationContext context) {
            return src == null ? null : new JsonPrimitive(src.getTime());
        }
    }

    private class TimestampDeserializer implements JsonDeserializer<Timestamp>{
        @Override
        public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Timestamp(json.getAsLong());
        }
    }
}
