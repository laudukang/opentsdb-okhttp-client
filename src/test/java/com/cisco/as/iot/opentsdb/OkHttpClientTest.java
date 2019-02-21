package com.cisco.as.iot.opentsdb;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.cisco.as.iot.opentsdb.builder.MetricBuilder;
import com.cisco.as.iot.opentsdb.config.HttpRequestConfig;
import com.cisco.as.iot.opentsdb.http.OkHttpClientImpl;
import com.cisco.as.iot.opentsdb.response.ExpectResponse;

public class OkHttpClientTest {

	private HttpClient client = null;

	@Before
	public void init() {
		HttpRequestConfig config = new HttpRequestConfig();
		config.setMaxConnections(200);
		config.setKeepAliveDuration(30);
		config.setRequestRetryEnabled(false);
		config.setOpentsdbServerUrl("http://localhost:4399");
		client = new OkHttpClientImpl(config);
	}

	@Test
	public void test_pushMetrics_DefaultRetries() {

		MetricBuilder builder = MetricBuilder.getInstance();
		builder.addMetric("metric1").setDataPoint(40).addTag("tag1", "tag1v");
		try {
			client.pushMetrics(builder, ExpectResponse.SUMMARY);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
