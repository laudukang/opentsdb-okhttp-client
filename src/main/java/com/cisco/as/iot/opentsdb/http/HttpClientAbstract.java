package com.cisco.as.iot.opentsdb.http;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cisco.as.iot.opentsdb.response.ExpectResponse;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public abstract class HttpClientAbstract {

    protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected static Logger logger = LoggerFactory.getLogger(HttpClientAbstract.class);

    /**
     * 获取请求URL
     *
     * @param serviceUrl
     * @param postApiEndPoint
     * @param expectResponse
     * @return
     */
    public String buildUrl(String serviceUrl, String postApiEndPoint, ExpectResponse expectResponse) {
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

    public Request createGetRequest(String url) {
        return new Request.Builder().url(url).build();
    }

    /**
     * 创建请求
     *
     * @param url
     * @param jsonObject
     * @return
     */
    public Request createPostBody(String url, JSONObject jsonObject) {
        return createPostBody(url, jsonObject.toJSONString());
    }

    /**
     * 创建请求
     *
     * @param url
     * @param json
     * @return
     */
    public Request createPostBody(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        return new Request.Builder().url(url).post(body).build();
    }
}
