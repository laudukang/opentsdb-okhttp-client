package com.cisco.as.iot.opentsdb.http;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import javax.net.ssl.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp请求工具类，支持链式添加多种类型的参数并调用，简化OkHttp的调用流程。
 * 支持多种参数源，大多数情况都不需要手动重组参数，直接从你的上下文给参数即可。
 * <p>
 * OKHttp原始API简化调用，可返回多种结果或回调:
 * OkHttpUtil.callSuccess()/callForResponse()/callForString()
 * /callForBytes()/callForStream()/callForObject()/callback()
 * <p>
 * 使用Url参数的GET请求：
 * OkHttpUtil.get().header().param().map().bean().callXxx()
 * <p>
 * 使用Url参数的DELETE请求：
 * OkHttpUtil.delete().header().param().map().bean().callXxx()
 * <p>
 * 使用Form参数的POST请求：
 * OkHttpUtil.formPost().header().param().map().bean().callXxx()
 * <p>
 * 带文件上传的Form参数的POST请求：
 * OkHttpUtil.multiPost().header().param().file().part().map().bean().callXxx()
 * <p>
 * 使用JSON的Body参数的POST请求：
 * OkHttpUtil.bodyPost().header().json().add().param().map().bean().callXxx()
 * <p>
 * 使用JSON的Body参数的PUT请求：
 * OkHttpUtil.bodyPut().header().json().add().param().map().bean().callXxx()
 *
 * <p>Created with IDEA
 * <p>Author: Ewing
 * <p>Author: laudukang
 * <p>Date: 2017/6/15
 * <p>Date: 2019/07/25
 * <p>Time: 17:26
 * <p>Version: 1.0
 * <p>
 * see https://github.com/EwingTsai/spring-boot-faster/blob/master/common/src/main/java/ewing/common/utils/OkHttpUtils.java
 */
public class OkHttpUtil {

    private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory(), x509TrustManager())//fix SSLHandshakeException
            .hostnameVerifier((hostname, session) -> true)//fix SSLPeerUnverifiedException
            .retryOnConnectionFailure(false)
            .connectionPool(pool())
//            .addInterceptor(new RetryInterceptor.Builder().executionCount(3).build())
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    private static SSLSocketFactory sslSocketFactory() {
        try {
            //信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(new KeyManager[0], new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new IllegalStateException("Get SSLSocketFactory fail", e);
        }
    }

    /**
     * 信任任何证书的凭证管理器。
     */
    private static X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    private static ConnectionPool pool() {
        return new ConnectionPool(200, 10, TimeUnit.MINUTES);
    }

    /**
     * 私有化构造方法，禁止创建实例。
     */
    private OkHttpUtil() {
    }

    /**
     * 请求器。
     */
    @SuppressWarnings("unchecked")
    private abstract static class Requester<R extends Requester> {
        protected Request.Builder builder = new Request.Builder();
        protected Object tag;

        public R header(String name, String value) {
            builder.header(name, value);
            return (R) this;
        }

        public R tag(Object tag) {
            this.tag = tag;
            return (R) this;
        }

        public abstract R param(String name, Object value);

        public R bean(Object bean) {
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor descriptor : descriptors) {
                    // 需要可用的属性
                    Method readMethod = descriptor.getReadMethod();
                    if (readMethod == null || descriptor.getWriteMethod() == null) {
                        continue;
                    }
                    Object value = readMethod.invoke(bean);
                    param(descriptor.getName(), value);
                }
            } catch (IntrospectionException | ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
            return (R) this;
        }

        public R map(Map<String, ?> map) {
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                param(entry.getKey(), entry.getValue());
            }
            return (R) this;
        }

        protected abstract Request buildRequest();

        protected Request.Builder builder() {
            if (Objects.nonNull(tag)) {
                builder.tag(tag);
            }

            return builder;
        }

        public void callSuccess() {
            OkHttpUtil.callSuccess(buildRequest());
        }

        public Response callForResponse() {
            return OkHttpUtil.callForResponse(buildRequest());
        }

        public <T> T callForObject(Class<T> type) {
            return OkHttpUtil.callForObject(buildRequest(), type);
        }

        public String callForString() {
            return OkHttpUtil.callForString(buildRequest());
        }

        public byte[] callForBytes() {
            return OkHttpUtil.callForBytes(buildRequest());
        }

        public InputStream callForStream() {
            return OkHttpUtil.callForStream(buildRequest());
        }

        public void callback(Callback callback) {
            OkHttpUtil.callback(buildRequest(), callback);
        }
    }

    /**
     * Get请求器。
     */
    public static class Getter extends Requester<Getter> {
        protected StringBuilder urlBuilder;
        private boolean hasParam;

        public Getter(String url) {
            this.urlBuilder = new StringBuilder(url);
            this.hasParam = url.contains("?");
        }

        @Override
        public Getter param(String name, Object value) {
            if (hasParam) {
                urlBuilder.append('&');
            } else {
                urlBuilder.append('?');
                hasParam = true;
            }
            urlBuilder.append(encodeUrl(name)).append('=').append(encodeUrl(value));
            return this;
        }

        public <C extends Collection<?>> Getter collection(String name, C params) {
            StringBuilder builder = new StringBuilder();
            if (params != null && params.size() > 0) {
                for (Object param : params) {
                    if (builder.length() > 0) {
                        builder.append(',');
                    }
                    builder.append(param);
                }
            }
            return param(name, builder.toString());
        }

        public Getter array(String name, Object... params) {
            StringBuilder builder = new StringBuilder();
            if (params != null && params.length > 0) {
                for (Object param : params) {
                    if (builder.length() > 0) {
                        builder.append(',');
                    }
                    builder.append(param);
                }
            }
            return param(name, builder.toString());
        }

        @Override
        protected Request buildRequest() {
            return builder().get().url(urlBuilder.toString()).build();
        }
    }

    /**
     * Post请求器。
     */
    public static class FormPoster extends Requester<FormPoster> {
        protected FormBody.Builder formBuilder = new FormBody.Builder();

        public FormPoster(String url) {
            this.builder.url(url);
        }

        @Override
        public FormPoster param(String name, Object value) {
            formBuilder.add(String.valueOf(name), String.valueOf(value));
            return this;
        }

        @Override
        protected Request buildRequest() {
            return builder().post(formBuilder.build()).build();
        }
    }

    /**
     * 带文件流Post请求器。
     */
    public static class MultiPoster extends Requester<MultiPoster> {
        protected MultipartBody.Builder multiBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        public MultiPoster(String url) {
            this.builder.url(url);
        }

        @Override
        public MultiPoster param(String name, Object value) {
            multiBuilder.addFormDataPart(String.valueOf(name), String.valueOf(value));
            return this;
        }

        public MultiPoster file(String name, File file) {
            multiBuilder.addFormDataPart(String.valueOf(name),
                    file.getName(), RequestBody.create(MEDIA_TYPE_STREAM, file));
            return this;
        }

        public MultiPoster part(MultipartBody.Part part) {
            multiBuilder.addPart(part);
            return this;
        }

        @Override
        protected Request buildRequest() {
            return builder().post(multiBuilder.build()).build();
        }
    }

    /**
     * Body的Post请求器。
     */
    public static class BodyPoster extends Requester<BodyPoster> {
        protected String jsonStr;

        public BodyPoster(String url) {
            this.builder.url(url);
        }

        public BodyPoster body(String jsonStr) {
            this.jsonStr = jsonStr;
            return this;
        }

        @Override
        public BodyPoster param(String name, Object value) {
            throw new UnsupportedOperationException("not support yet");
        }

        @Override
        protected Request buildRequest() {
            return builder().post(RequestBody.create(MEDIA_TYPE_JSON, jsonStr)).build();
        }
    }

    /**
     * Body的Put请求器。
     */
    public static class BodyPutter extends BodyPoster {

        public BodyPutter(String url) {
            super(url);
        }

        @Override
        protected Request buildRequest() {
            return builder().put(RequestBody.create(MEDIA_TYPE_JSON, jsonStr)).build();
        }
    }

    /**
     * Delete请求器。
     */
    public static class DeleteBuilder extends Getter {

        public DeleteBuilder(String url) {
            super(url);
        }

        @Override
        protected Request buildRequest() {
            return builder().delete().url(urlBuilder.toString()).build();
        }
    }

    /**
     * 准备创建Url的Get请求。
     */
    public static Getter get(String url) {
        return new Getter(url);
    }

    /**
     * 准备创建表单的Post请求。
     */
    public static FormPoster formPost(String url) {
        return new FormPoster(url);
    }

    /**
     * 准备创建带文件的Post请求。
     */
    public static MultiPoster multiPost(String url) {
        return new MultiPoster(url);
    }

    /**
     * 准备创建Body的Post请求。
     */
    public static BodyPoster bodyPost(String url) {
        return new BodyPoster(url);
    }

    /**
     * 准备创建Body的Put请求。
     */
    public static BodyPutter bodyPut(String url) {
        return new BodyPutter(url);
    }

    /**
     * 准备创建Url的Delete请求。
     */
    public static DeleteBuilder delete(String url) {
        return new DeleteBuilder(url);
    }

    /**
     * 使用UTF-8进行URL参数编码。
     */
    public static String encodeUrl(Object source) {
        try {
            return URLEncoder.encode(String.valueOf(source), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行请求要求成功并忽略结果。
     */
    public static void callSuccess(Request request) {
        try {
            Response response = CLIENT.newCall(request).execute();
            response.close(); // 内部静默关闭
            if (!response.isSuccessful()) {
                throw new RuntimeException("Request Failure, Code: "
                        + response.code() + " Message: " + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException("Request IOException", e);
        }
    }

    /**
     * 执行Request请求并返回Response。
     */
    public static Response callForResponse(Request request) {
        try {
            return CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException("Request IOException", e);
        }
    }

    /**
     * 执行Request请求并返回String值。
     */
    public static String callForString(Request request) {
        try {
            Response response = CLIENT.newCall(request).execute();
            String result = response.body().string();
            response.close(); // 内部静默关闭
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Request IOException", e);
        }
    }

    /**
     * 执行Request请求并返回字节数组。
     */
    public static byte[] callForBytes(Request request) {
        try {
            Response response = CLIENT.newCall(request).execute();
            byte[] result = response.body().bytes();
            response.close(); // 内部静默关闭
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Request IOException", e);
        }
    }

    /**
     * 执行Request请求并返回数据流。
     */
    public static InputStream callForStream(Request request) {
        return callForResponse(request).body().byteStream();
    }

    /**
     * 执行Request请求并将返回的Json转成对象。
     */
    public static <T> T callForObject(Request request, Class<T> type) {
        String result = OkHttpUtil.callForString(request);
        return JSONObject.parseObject(result, type);
    }

    /**
     * 执行Request请求并在完成时执行回调方法。
     */
    public static void callback(Request request, Callback callback) {
        CLIENT.newCall(request).enqueue(callback);
    }

    public static int cancelCallWithTag(Object tagObject) {
        int cancelNum = 0;

        if (Objects.isNull(tagObject)) {
            return cancelNum;
        }

        for (Call call : CLIENT.dispatcher().queuedCalls()) {
            Object tag = call.request().tag();
            if (Objects.nonNull(tag) && tag.equals(tagObject)) {
                call.cancel();
                cancelNum++;
            }
        }

        for (Call call : CLIENT.dispatcher().runningCalls()) {
            Object tag = call.request().tag();
            if (Objects.nonNull(tag) && tag.equals(tagObject)) {
                call.cancel();
                cancelNum++;
            }
        }

        return cancelNum;
    }

    public static OkHttpClient getClient() {
        return CLIENT;
    }
}
