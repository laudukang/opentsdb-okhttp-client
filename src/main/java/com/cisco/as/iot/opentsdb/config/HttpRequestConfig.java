package com.cisco.as.iot.opentsdb.config;

public class HttpRequestConfig {

	public static final HttpRequestConfig DEFAULT = new HttpRequestConfig();

	private String opentsdbServerUrl;

	private int maxIdleConnections = 5;
	private int keepAliveDuration = 5;

	private int readTimeout = 6000;
	private int socketTimeout = 6000;
	private int connectTimeout = 6000;
	private int connectionRequestTimeout = 6000;

	private int maxConnections = 200;
	private int maxConnectionPerHost = 50;

	private boolean requestRetryEnabled;
	private boolean retryCount;

	public String getOpentsdbServerUrl() {
		return opentsdbServerUrl;
	}

	public void setOpentsdbServerUrl(String opentsdbServerUrl) {
		this.opentsdbServerUrl = opentsdbServerUrl;
	}

	public int getMaxIdleConnections() {
		return maxIdleConnections;
	}

	public void setMaxIdleConnections(int maxIdleConnections) {
		this.maxIdleConnections = maxIdleConnections;
	}

	public int getKeepAliveDuration() {
		return keepAliveDuration;
	}

	public void setKeepAliveDuration(int keepAliveDuration) {
		this.keepAliveDuration = keepAliveDuration;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public int getMaxConnectionPerHost() {
		return maxConnectionPerHost;
	}

	public void setMaxConnectionPerHost(int maxConnectionPerHost) {
		this.maxConnectionPerHost = maxConnectionPerHost;
	}

	public boolean isRequestRetryEnabled() {
		return requestRetryEnabled;
	}

	public void setRequestRetryEnabled(boolean requestRetryEnabled) {
		this.requestRetryEnabled = requestRetryEnabled;
	}

	public boolean isRetryCount() {
		return retryCount;
	}

	public void setRetryCount(boolean retryCount) {
		this.retryCount = retryCount;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}
}
