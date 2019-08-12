package com.cisco.as.iot.opentsdb;

import com.cisco.as.iot.opentsdb.builder.DataPointBuilder;
import com.cisco.as.iot.opentsdb.request.Filter;
import com.cisco.as.iot.opentsdb.request.QueryBuilder;
import com.cisco.as.iot.opentsdb.request.SubQueries;
import com.cisco.as.iot.opentsdb.response.ExpectResponse;
import com.cisco.as.iot.opentsdb.service.OpenTSDBService;
import com.cisco.as.iot.opentsdb.utils.AggregatorEnum;
import com.cisco.as.iot.opentsdb.utils.FilterTypeEnum;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class OpenTSDBServiceTest {

    private OpenTSDBService openTSDBService = null;

    @Before
    public void init() {
        String openTSDBServer = "http://n2:4242";

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

        String response = openTSDBService.pushQueries(builder, ExpectResponse.SUMMARY);
        System.out.println(response);
    }
}
