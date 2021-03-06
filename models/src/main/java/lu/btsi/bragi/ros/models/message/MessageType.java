package lu.btsi.bragi.ros.models.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gillesbraun on 15/02/2017.
 */
public enum MessageType {
    Get("get"), Update("update"), Delete("delete"), Create("create"), Answer("answer"), Error("error"), GetQuery("getquery", true), Broadcast("broadcast");

    private final String name;
    private final boolean hasParams;
    private static final Map<String, MessageType> lookup = new HashMap<>();

    static {
        for(MessageType mt : MessageType.values()) {
            lookup.put(mt.getName(), mt);
        }
    }

    MessageType(String name) {
        this.name = name;
        hasParams = false;
    }

    MessageType(String name, boolean hasParams) {
        this.name = name;
        this.hasParams = hasParams;
    }

    public String getName() {
        return name;
    }

    public static MessageType get(Object key) {
        return lookup.get(key);
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean hasParams() {
        return hasParams;
    }
}
