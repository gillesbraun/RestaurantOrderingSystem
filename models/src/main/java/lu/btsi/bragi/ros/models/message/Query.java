package lu.btsi.bragi.ros.models.message;

import org.jooq.types.UInteger;

/**
 * Created by gillesbraun on 20/03/2017.
 */
public class Query {
    private final QueryType queryType;
    private final UInteger param;

    public Query(QueryType queryType, UInteger param) {
        this.queryType = queryType;
        this.param = param;
    }

    public Query(QueryType queryType) {
        this.queryType = queryType;
        this.param = null;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public UInteger getParam() {
        return param;
    }
}