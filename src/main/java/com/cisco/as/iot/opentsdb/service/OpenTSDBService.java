package com.cisco.as.iot.opentsdb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cisco.as.iot.opentsdb.builder.MetricBuilder;
import com.cisco.as.iot.opentsdb.http.OkHttpUtil;
import com.cisco.as.iot.opentsdb.request.QueryBuilder;
import com.cisco.as.iot.opentsdb.response.ExpectResponse;
import okhttp3.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>Created with IDEA
 * <p>Author: laudukang
 * <p>Date: 2019/8/6
 * <p>Time: 20:46
 * <p>Version: 1.0
 */
public interface OpenTSDBService {
    Logger LOGGER = LoggerFactory.getLogger(OpenTSDBService.class);

    String PUT_POST_API = "/api/put";

    String QUERY_POST_API = "/api/query";

    String getOpenTSDBServer();

    default String buildUrl(String serviceUrl, String postApiEndPoint, ExpectResponse expectResponse) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(serviceUrl).append(postApiEndPoint);
        switch (expectResponse) {
            case SUMMARY:
                urlBuilder.append("?summary");
                break;
            case DETAIL:
                urlBuilder.append("?details");
                break;
            default:
                break;
        }
        return urlBuilder.toString();
    }

    default JSONObject pushMetrics(MetricBuilder builder, ExpectResponse expectResponse) {
        checkNotNull(builder);
        String url = buildUrl(this.getOpenTSDBServer(), PUT_POST_API, expectResponse);
        String body = builder.build();
        LOGGER.debug("post: {} body: {}", url, body);

        return OkHttpUtil.bodyPost(url).body(body).callForObject(JSONObject.class);
    }

    default JSONArray pushQueries(QueryBuilder builder, ExpectResponse expectResponse) {
        checkNotNull(builder);
        String url = buildUrl(this.getOpenTSDBServer(), QUERY_POST_API, expectResponse);
        String body = builder.build();
        LOGGER.debug("post: {} body: {}", url, body);

        return OkHttpUtil.bodyPost(url).body(body).callForObject(JSONArray.class);
    }

    default void asyncPushMetrics(MetricBuilder builder, ExpectResponse expectResponse, Callback callback) {
        checkNotNull(builder);
        String url = buildUrl(this.getOpenTSDBServer(), PUT_POST_API, expectResponse);
        String body = builder.build();
        LOGGER.debug("post: {} body: {}", url, body);

        OkHttpUtil.bodyPost(url).body(body).callback(callback);
    }
}