package lu.btsi.bragi.ros.models.message;

/**
 * Created by gillesbraun on 22/03/2017.
 */
public class QueryParam {
    private final String name;
    private final String clazz;
    private final Object value;

    public QueryParam(String name, Class<?> clazz, Object value) {
        if(!clazz.equals(value.getClass())) {
            throw new IllegalArgumentException("Object should be an instance of clazz.");
        }
        this.name = name;
        this.clazz = clazz.getCanonicalName();
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public static QueryParam from(String name, Class<?> clazz, Object value) {
        if(!value.getClass().equals(clazz))
            throw new ClassCastException("Not allowed");
        return new QueryParam(name, clazz, value);
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() throws ClassNotFoundException {
        return Class.forName(clazz);
    }

    public <T> T getValueAs(Class<T> clazz) {
        return clazz.cast(value);
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "QueryParam{" +
                "name='" + name + '\'' +
                ", clazz='" + clazz + '\'' +
                ", value=" + value +
                '}';
    }
}
