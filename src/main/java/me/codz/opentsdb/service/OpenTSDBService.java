package me.codz.opentsdb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.codz.opentsdb.builder.DataPointBuilder;
import me.codz.opentsdb.http.OkHttpUtil;
import me.codz.opentsdb.request.QueryBuilder;
import me.codz.opentsdb.response.ExpectResponse;
import me.codz.opentsdb.response.QueryResponse;
import okhttp3.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;

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

    default String pushDataPointsString(DataPointBuilder builder, ExpectResponse expectResponse) {
        checkNotNull(builder);
        String url = buildUrl(this.getOpenTSDBServer(), PUT_POST_API, expectResponse);
        String body = builder.build();
        LOGGER.debug("post: {} body: {}", url, body);

        LocalDateTime start = LocalDateTime.now();
        String responseStr = OkHttpUtil.bodyPost(url).body(body).callForString();

        LOGGER.debug("cost: {} mills, response: {}", Duration.between(start, LocalDateTime.now()).toMillis(), responseStr);
        return responseStr;
    }

    default String pushQueriesString(QueryBuilder builder, ExpectResponse expectResponse) {
        checkNotNull(builder);
        String url = buildUrl(this.getOpenTSDBServer(), QUERY_POST_API, expectResponse);
        String body = builder.build();
        LOGGER.debug("post: {} body: {}", url, body);

        LocalDateTime start = LocalDateTime.now();
        String responseStr = OkHttpUtil.bodyPost(url).body(body).callForString();

        LOGGER.debug("cost: {} mills, response: {}", Duration.between(start, LocalDateTime.now()).toMillis(), responseStr);
        return responseStr;
    }

    default void asyncPushDataPoints(DataPointBuilder builder, ExpectResponse expectResponse, Callback callback) {
        checkNotNull(builder);
        String url = buildUrl(this.getOpenTSDBServer(), PUT_POST_API, expectResponse);
        String body = builder.build();
        LOGGER.debug("post: {} body: {}", url, body);

        OkHttpUtil.bodyPost(url).body(body).callback(callback);
    }

    default QueryResponse pushQueries(QueryBuilder builder, ExpectResponse expectResponse) {
        String responseStr = pushQueriesString(builder, expectResponse);
        QueryResponse queryResponse = new QueryResponse();
        try {
            JSONArray jsonArray = JSONArray.parseArray(responseStr);

            for (int index = 0; index < jsonArray.size(); index++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                if (jsonObject.containsKey("metric")) {
                    queryResponse.addMetricData(jsonObject.toJavaObject(QueryResponse.MetricData.class));
                } else if (jsonObject.containsKey("statsSummary")) {
                    queryResponse.setStatsSummary(jsonObject.getString("statsSummary"));
                }
            }
        } catch (Exception e) {
            JSONObject jsonObject = JSONObject.parseObject(responseStr).getJSONObject("error");

            queryResponse.setCode(jsonObject.getIntValue("code"));
            queryResponse.setMessage(jsonObject.getString("message"));
            queryResponse.setDetails(jsonObject.getString("details"));

            LOGGER.error("jvm error: {}, code: {}, message: {}, details:{}", e.getMessage(),
                    jsonObject.getIntValue("code"), jsonObject.getString("message"), jsonObject.getString("details"));
        }
        return queryResponse;
    }
}
