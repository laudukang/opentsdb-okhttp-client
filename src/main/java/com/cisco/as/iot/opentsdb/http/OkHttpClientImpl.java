package com.cisco.as.iot.opentsdb.http;

import com.cisco.as.iot.opentsdb.HttpClient;
import com.cisco.as.iot.opentsdb.builder.MetricBuilder;
import com.cisco.as.iot.opentsdb.config.HttpRequestConfig;
import com.cisco.as.iot.opentsdb.request.QueryBuilder;
import com.cisco.as.iot.opentsdb.response.ExpectResponse;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class OkHttpClientImpl extends HttpClientAbstract implements HttpClient {

    private static Logger logger = LoggerFactory.getLogger(OkHttpClientImpl.class);

    private OkHttpClient client;

    private HttpRequestConfig config;

    public OkHttpClientImpl(HttpRequestConfig config) {

        checkNotNull(config);
        this.config = config;

        ConnectionPool connectionPool = new ConnectionPool(config.getMaxIdleConnections(), config.getKeepAliveDuration(), TimeUnit.SECONDS);
        client = new OkHttpClient.Builder()
                .readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
                .connectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .connectionPool(connectionPool)
                .retryOnConnectionFailure(false)
                .build();
    }

    /**
     * add the MetricValue to server
     *
     * @param builder
     * @return
     * @throws IOException
     */
    @Override
    public Response pushMetrics(MetricBuilder builder) throws IOException {
        logger.debug(builder.toString());
        return pushMetrics(builder, ExpectResponse.STATUS_CODE);
    }

    /**
     * get the value from server
     *
     * @param builder
     * @return
     * @throws IOException
     */
    @Override
    public Response pushQueries(QueryBuilder builder) throws IOException {
        logger.debug(builder.toString());
        return pushQueries(builder, ExpectResponse.STATUS_CODE);
    }

    /**
     * add the MetricValue to server
     *
     * @param builder
     * @param expectResponse
     * @return
     * @throws IOException
     */
    @Override
    public Response pushMetrics(MetricBuilder builder, ExpectResponse expectResponse) throws IOException {

        checkNotNull(builder);
        String url = buildUrl(config.getOpentsdbServerUrl(), PUT_POST_API, expectResponse);
        Request request = createPostBody(url, builder.build());
        try {
            execute(request);
        } catch (HttpRequestException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * get the value from server
     *
     * @param builder
     * @param expectResponse
     * @return
     * @throws IOException
     */
    @Override
    public Response pushQueries(QueryBuilder builder, ExpectResponse expectResponse) throws IOException {
        checkNotNull(builder);
        // TODO 错误处理，比如IOException或者failed>0，写到队列或者文件后续重试。
        String url = buildUrl(config.getOpentsdbServerUrl(), QUERY_POST_API, expectResponse);
        Request request = createPostBody(url, builder.build());
        try {
            return execute(request);
        } catch (HttpRequestException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 异步push数据
     */
    @Override
    public void asyncPushMetrics(MetricBuilder builder, Callback callback) throws IOException {
        checkNotNull(builder);
        String url = buildUrl(config.getOpentsdbServerUrl(), QUERY_POST_API, ExpectResponse.STATUS_CODE);
        Request request = createPostBody(url, builder.build());
        try {
            asyncExecute(request, callback);
        } catch (HttpRequestException e) {
            logger.error(e.getMessage());
        }
    }

    private Response execute(Request request) throws HttpRequestException {
        try {
            return client.newCall(request).execute();
        } catch (Exception e) {
            logger.error("OkHttp post error", e);
            throw new HttpRequestException("OkHttp post error", e);
        }
    }

    private void asyncExecute(Request request, Callback callback) throws HttpRequestException {
        try {
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            logger.error("OkHttp post error", e);
            throw new HttpRequestException("OkHttp post error", e);
        }
    }

}
