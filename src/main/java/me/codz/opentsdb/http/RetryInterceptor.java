package me.codz.opentsdb.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Objects;

/**
 * <p>Created with IDEA
 * <p>Author: laudukang
 * <p>Date: 2019/7/29
 * <p>Time: 17:26
 * <p>Version: 1.0
 * <p>
 * 重试拦截器
 * 默认：重试三次，重试间隔为2的N次方秒，也就是按1s/2s/4s重试
 * 支持自定义重试间隔（毫秒），会覆盖上面指数级间隔
 */
public class RetryInterceptor implements Interceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryInterceptor.class);

    private int executionCount;//最大重试次数
    private Long retryInterval;//重试的间隔，毫秒；如不设置则按1秒/2/4/8/16指数间隔重试

    private RetryInterceptor(Builder builder) {
        this.executionCount = builder.executionCount;
        this.retryInterval = builder.retryInterval;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = doRequest(chain, request);
        int retryNum = 0;

        while ((response == null || !response.isSuccessful()) && retryNum <= executionCount) {
            final long nextInterval = Objects.nonNull(retryInterval) ? retryInterval : getRetryInterval(retryNum);
            retryNum++;

            LOGGER.debug("intercept Request is not successful - request: {}, try count: {}, sleep {} mills", request, retryNum, nextInterval);

            try {
                Thread.sleep(nextInterval);
            } catch (final InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
                throw new InterruptedIOException(e.getMessage());
            }

            // retry the request
            try {
                response = chain.proceed(request);
            } catch (IOException e) {
                if (retryNum >= executionCount) {
                    throw e;
                }
            }
        }

        return response;
    }

    /**
     * 计算等待时间，指数级
     *
     * @param number
     * @return
     */
    private long getRetryInterval(int number) {
        return Double.valueOf(Math.pow(2, number)).longValue() * 1000;
    }

    private Response doRequest(Chain chain, Request request) {
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (Exception ignored) {
        }
        return response;
    }

    public static final class Builder {
        private int executionCount;
        private Long retryInterval;

        public Builder() {
            executionCount = 3;
            retryInterval = null;
        }

        public RetryInterceptor.Builder executionCount(int executionCount) {
            this.executionCount = executionCount;
            return this;
        }

        public RetryInterceptor.Builder retryInterval(long retryInterval) {
            this.retryInterval = retryInterval;
            return this;
        }

        public RetryInterceptor build() {
            return new RetryInterceptor(this);
        }
    }

}
