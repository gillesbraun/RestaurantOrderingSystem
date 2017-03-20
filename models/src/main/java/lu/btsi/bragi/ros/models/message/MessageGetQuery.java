package lu.btsi.bragi.ros.models.message;

/**
 * Created by Gilles Braun on 23.02.2017.
 */
public class MessageGetQuery<T> extends Message<T> {

    public MessageGetQuery(Class<T> clazz, Query query) {
        super(MessageType.GetQuery, clazz, query);
    }
}
