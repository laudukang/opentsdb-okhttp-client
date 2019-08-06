package com.cisco.as.iot.opentsdb;

import com.cisco.as.iot.opentsdb.config.HttpRequestConfig;
import com.cisco.as.iot.opentsdb.http.OkHttpClientImpl;
import com.cisco.as.iot.opentsdb.request.QueryBuilder;
import com.cisco.as.iot.opentsdb.request.SubQueries;
import com.cisco.as.iot.opentsdb.response.ExpectResponse;
import com.cisco.as.iot.opentsdb.utils.Aggregator;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

/**
 * Created by shifeng on 2016/5/19. MyProject
 */
public class QueryTest {

    private HttpClient client = null;

    @Before
    public void init() {
        HttpRequestConfig config = new HttpRequestConfig();
        config.setMaxConnections(200);
        config.setKeepAliveDuration(3000);
        config.setRequestRetryEnabled(false);
        config.setOpentsdbServerUrl("http://localhost:4399");
        client = new OkHttpClientImpl(config);
    }

    @Test
    public void queryTest() throws IOException {

        QueryBuilder builder = QueryBuilder.getInstance();
        SubQueries subQueries = new SubQueries();

        String sum = Aggregator.sum.toString();
        subQueries.addMetric("metric1").addTag("tag1", "tag1v").addAggregator(sum).addDownsample("1s-" + sum);
        long now = new Date().getTime() / 1000;
        builder.getQuery().addStart(126358720).addEnd(now).addSubQuery(subQueries);
        System.out.println(builder.build());

        try {
            Response response = client.pushQueries(builder, ExpectResponse.SUMMARY);
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
