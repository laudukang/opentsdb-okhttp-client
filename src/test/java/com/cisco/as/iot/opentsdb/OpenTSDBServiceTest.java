package com.cisco.as.iot.opentsdb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cisco.as.iot.opentsdb.builder.MetricBuilder;
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
        MetricBuilder builder = MetricBuilder.getInstance();
        builder.addMetric("metric1").setDataPoint(40).addTag("tag1", "tag1v");
        JSONObject jsonObject = openTSDBService.pushMetrics(builder, ExpectResponse.SUMMARY);

        System.out.println(jsonObject);
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

        JSONArray jsonArray = openTSDBService.pushQueries(builder, ExpectResponse.SUMMARY);

        System.out.println(jsonArray);
    }
}
