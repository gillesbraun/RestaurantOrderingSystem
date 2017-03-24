package lu.btsi.bragi.ros.models.message;

import java8.util.Optional;
import java8.util.stream.StreamSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gillesbraun on 20/03/2017.
 */
public class Query {
    private final QueryType queryType;
    private final List<QueryParam> queryParams;

    public Query(QueryType queryType, QueryParam... queryParams) {
        this.queryType = queryType;
        this.queryParams = new ArrayList<>(Arrays.asList(queryParams));
    }

    public Query(QueryType queryType) {
        this.queryType = queryType;
        this.queryParams = null;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public List<QueryParam> getQueryParams() {
        return queryParams;
    }

    public <T> T getParam(String name, Class<T> clazz) {
        Optional<QueryParam> value = StreamSupport.stream(queryParams)
                .filter(qp -> qp.getName().equals(name))
                .findFirst();
        if(value.isPresent())
            return clazz.cast(value.get());
        else
            throw new QueryParamNotFoundException("Could not find query parameter " + name);
    }
}