package lu.btsi.bragi.ros.server.controller;

/**
 * Created by gillesbraun on 16/02/2017.
 */
public class ControllerNotFoundException extends RuntimeException {
    public ControllerNotFoundException() {
    }

    public ControllerNotFoundException(String message) {
        super(message);
    }

    public ControllerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerNotFoundException(Throwable cause) {
        super(cause);
    }

    public ControllerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
