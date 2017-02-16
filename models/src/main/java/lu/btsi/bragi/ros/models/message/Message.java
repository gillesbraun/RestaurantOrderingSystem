package lu.btsi.bragi.ros.models.message;

import com.google.gson.Gson;

/**
 * Created by gillesbraun on 15/02/2017.
 */
public class Message {
    private final MessageType action;
    private final Object object;
    private final Class clazz;

    public static final String SEPARATOR = ";;";


    public Message(MessageType action, Object object) {
        this.action = action;
        this.object = object;
        this.clazz = object.getClass();
    }

    public Message(MessageType action, Class clazz) {
        this.action = action;
        this.clazz = clazz;
        this.object = null;
    }

    public static Message fromString(String str) throws MessageMalformedException, ClassNotFoundException {
        String[] split = str.split(SEPARATOR);
        if(split.length != 2 && split.length != 3) {
            throw new MessageMalformedException("Message must consist of 2 or 3 parts. Action"+SEPARATOR+"Class["+SEPARATOR+"Json]");
        }

        MessageType messageType = MessageType.get(split[0]);
        if(messageType == null) {
            throw new MessageMalformedException("Message Type '"+messageType+"' does not exist.");
        }
        Class clazz = Class.forName(split[1]);
        if(split.length == 2) {
            return new Message(messageType, clazz);
        } else {
            return new Message(messageType, clazz.cast(new Gson().fromJson(split[2], clazz)));
        }
    }

    @Override
    public String toString() {
        if(object != null) {
            String json = new Gson().toJson(object);
            return action.getName() + SEPARATOR + object.getClass().getCanonicalName() + SEPARATOR + json;
        } else {
            return action.getName() + SEPARATOR + clazz.getCanonicalName();
        }
    }

    public MessageType getAction() {
        return action;
    }

    public Object getObject() {
        return object;
    }

    public Class getClazz() {
        return clazz;
    }
}
