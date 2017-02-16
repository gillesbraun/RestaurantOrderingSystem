package lu.btsi.bragi.ros.models.message;

import com.google.gson.Gson;

/**
 * Created by gillesbraun on 15/02/2017.
 */
public class Answer extends Message {
    private final Object content;

    public Answer(Object content, Class clazz) {
        super(MessageType.Answer, clazz);
        this.content = content;
    }

    private String getObjectAsJSON() {
        return new Gson().toJson(content);
    }

    @Override
    public String toString() {
        return getAction().getName() + SEPARATOR + getClazz().getCanonicalName() + SEPARATOR + getObjectAsJSON();
    }
}
