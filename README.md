# OpenTSDB Java Async Client

## Changes

1. replace gson by `fastjson`

2. using `OkHttp3`, support query retry by custom `RetryInterceptor`, configuration example:

    ```java
        private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory(), x509TrustManager())//fix SSLHandshakeException
                .hostnameVerifier((hostname, session) -> true)//fix SSLPeerUnverifiedException
                .retryOnConnectionFailure(false)
                .connectionPool(pool())
                .addInterceptor(new RetryInterceptor.Builder().executionCount(3).retryInterval(1000).build())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();
    ```

3. default method provided in `OpenTSDBService`:

    ```java
    default String buildUrl(String serviceUrl, String postApiEndPoint, ExpectResponse expectResponse)

    default String pushDataPointsString(DataPointBuilder builder, ExpectResponse expectResponse)

    default String pushQueriesString(QueryBuilder builder, ExpectResponse expectResponse)

    default void asyncPushDataPoints(DataPointBuilder builder, ExpectResponse expectResponse, Callback callback)

    default QueryResponse pushQueries(QueryBuilder builder, ExpectResponse expectResponse)
    ```

## Spring Boot init `OpenTSDBService` example:

```java
    @Bean
    public OpenTSDBService openTSDBService() {
        List<String> openTSDBServerList = configProperties.getOpentsdbServer();
        if (CollectionUtils.isEmpty(openTSDBServerList)) {
            throw new IllegalArgumentException("empty opentsdb server url");
        }

        return () -> openTSDBServerList;
    }
```

## Reference

- [OkHttpUtils.java](https://github.com/EwingTsai/spring-boot-faster/blob/master/common/src/main/java/ewing/common/utils/OkHttpUtils.java)

- [yangwn/opentsdb-okhttp-client](https://github.com/yangwn/opentsdb-okhttp-client)

## License

Apache License, Version 2.0