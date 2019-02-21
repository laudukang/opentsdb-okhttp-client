package com.cisco.as.iot.opentsdb;

import java.io.IOException;

import com.cisco.as.iot.opentsdb.builder.MetricBuilder;
import com.cisco.as.iot.opentsdb.request.QueryBuilder;

import okhttp3.Callback;
import okhttp3.Response;

public interface Client {

	String PUT_POST_API = "/api/put";

	String QUERY_POST_API = "/api/query";

	/**
	 * Sends metrics from the builder to the OpenTSDB server.
	 *
	 * @param builder metrics builder
	 * @return response from the server
	 * @throws IOException problem occurred sending to the server
	 * 
	 */
	Response pushMetrics(MetricBuilder builder) throws IOException;

	/**
	 * Query value by QueryBuilder from OpenTSDB server.
	 * 
	 * @param builder
	 * @return
	 * @throws IOException
	 */
	Response pushQueries(QueryBuilder builder) throws IOException;

	/**
	 * 异步Push数据
	 * 
	 * @param builder
	 * @throws IOException
	 */
	void asyncPushMetrics(MetricBuilder builder, Callback callback) throws IOException;

}
