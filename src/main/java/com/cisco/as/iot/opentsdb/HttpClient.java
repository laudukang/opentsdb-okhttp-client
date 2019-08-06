package com.cisco.as.iot.opentsdb;

import java.io.IOException;

import com.cisco.as.iot.opentsdb.builder.MetricBuilder;
import com.cisco.as.iot.opentsdb.request.QueryBuilder;
import com.cisco.as.iot.opentsdb.response.ExpectResponse;

import okhttp3.Response;

public interface HttpClient extends Client {

    public Response pushMetrics(MetricBuilder builder, ExpectResponse expectResponse) throws IOException;

    public Response pushQueries(QueryBuilder builder, ExpectResponse expectResponse) throws IOException;

}
