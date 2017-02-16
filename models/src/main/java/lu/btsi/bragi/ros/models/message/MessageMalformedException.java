package lu.btsi.bragi.ros.models.message;

/**
 * Created by gillesbraun on 15/02/2017.
 */
public class MessageMalformedException extends RuntimeException {
    public MessageMalformedException() {
    }

    public MessageMalformedException(String s) {
        super(s);
    }

    public MessageMalformedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageMalformedException(Throwable cause) {
        super(cause);
    }
}
