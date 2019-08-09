package com.cisco.as.iot.opentsdb.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Objects;

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

    public String build() {
        // verify that there is at least one tag for each metric

        checkState(Objects.nonNull(query.getStart()), " must contain start.");
        checkState(query.getQueries() != null, " must contain at least one subQuery.");

        return JSON.toJSONString(query, SerializerFeature.IgnoreNonFieldGetter);
    }
}
