# OpenTSDB Java Async Client

1. replace gson by fastjson

2. using OkHttp3, support query retry base `RetryInterceptor`

3. default method provide in `OpenTSDBService`:

    - default String buildUrl(String serviceUrl, String postApiEndPoint, ExpectResponse expectResponse)
    
    - default String pushDataPointsString(DataPointBuilder builder, ExpectResponse expectResponse)
    
    - default String pushQueriesString(QueryBuilder builder, ExpectResponse expectResponse)
    
    - default void asyncPushDataPoints(DataPointBuilder builder, ExpectResponse expectResponse, Callback callback)
    
    - default QueryResponse pushQueries(QueryBuilder builder, ExpectResponse expectResponse)
    
4. Spring Boot init `OpenTSDBService` example:

    ```java
        @Bean
        public OpenTSDBService openTSDBService() {
            String openTSDBServer = configProperties.getOpentsdbServer();
            if (StringUtils.isBlank(openTSDBServer)) {
                throw new IllegalArgumentException("empty opentsdb server url");
            }
    
            return () -> openTSDBServer;
        }
    ```


Reference:

- [OkHttpUtils.java](https://github.com/EwingTsai/spring-boot-faster/blob/master/common/src/main/java/ewing/common/utils/OkHttpUtils.java)

- [yangwn/opentsdb-okhttp-client](https://github.com/yangwn/opentsdb-okhttp-client)
