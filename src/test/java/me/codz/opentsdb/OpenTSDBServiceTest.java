package me.codz.opentsdb;

import me.codz.opentsdb.builder.DataPointBuilder;
import me.codz.opentsdb.request.Filter;
import me.codz.opentsdb.request.QueryBuilder;
import me.codz.opentsdb.request.SubQueries;
import me.codz.opentsdb.response.ExpectResponse;
import me.codz.opentsdb.service.OpenTSDBService;
import me.codz.opentsdb.utils.AggregatorEnum;
import me.codz.opentsdb.utils.FilterTypeEnum;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class OpenTSDBServiceTest {

    private OpenTSDBService openTSDBService = null;

    @Before
    public void init() {
        String openTSDBServer = "http://localhost:4242";

        openTSDBService = () -> openTSDBServer;
    }

    @Test
    public void testPushMetrics() {
        DataPointBuilder builder = DataPointBuilder.getInstance();
        builder.addMetric("metric1").value(40).addTag("tag1", "tag1v");

        String response = openTSDBService.pushDataPointsString(builder, ExpectResponse.SUMMARY);
        System.out.println(response);
    }

    @Test
    public void testGetMetrics() {
        QueryBuilder builder = QueryBuilder.getInstance();

        Filter filter = new Filter();
        filter.setTagk("tag1");
        filter.setFilter("*");
//		filter.setType(FilterTypeEnum.literal_or);
        filter.setType(FilterTypeEnum.iwildcard);

        SubQueries subQueries = new SubQueries().addMetric("metric1").addFilter(filter).addAggregator(AggregatorEnum.zimsum);//.addDownsample("1s-" + sum);

        long now = new Date().getTime() / 1000;
        builder.getQuery().addStart(1546534700).addEnd(now).addSubQuery(subQueries);

        String response = openTSDBService.pushQueriesString(builder, ExpectResponse.SUMMARY);
        System.out.println(response);
    }

    @Test
    public void testAsyncPushMetrics() {
        DataPointBuilder builder = DataPointBuilder.getInstance();
        builder.addMetric("metric1").value(40).addTag("tag1", "tag1v");

        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        };

        openTSDBService.asyncPushDataPoints(builder, ExpectResponse.SUMMARY, callback);
    }
}
