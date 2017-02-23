package lu.btsi.bragi.ros.models.message;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Gilles Braun on 23.02.2017.
 */
class ListOfType<X> implements ParameterizedType {

    private Class<?> wrapped;

    public ListOfType(Class<X> wrapped) {
        this.wrapped = wrapped;
    }

    public Type[] getActualTypeArguments() {
        return new Type[] {wrapped};
    }

    public Type getRawType() {
        return List.class;
    }

    public Type getOwnerType() {
        return null;
    }

}