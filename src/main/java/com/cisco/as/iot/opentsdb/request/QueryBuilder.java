package com.cisco.as.iot.opentsdb.request;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkState;

public class QueryBuilder {

    private Query query = new Query();

    private QueryBuilder() {
    }

    public static QueryBuilder getInstance() {
        return new QueryBuilder();
    }

    public Query getQuery() {
        return this.query;
    }

    public String build() throws IOException {
        // verify that there is at least one tag for each metric
        checkState(query.getStart() > 0, " must contain start.");
        checkState(query.getQueries() != null, " must contain at least one subQuery.");
        return JSONObject.toJSONString(query);
    }
}
