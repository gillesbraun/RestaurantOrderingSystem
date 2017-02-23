package lu.btsi.bragi.ros.models.message;

/**
 * Created by Gilles Braun on 23.02.2017.
 */
public class MessageGet<T> extends Message<T> {
    public MessageGet(Class<T> clazz) {
        super(MessageType.Get, clazz);
    }
}
