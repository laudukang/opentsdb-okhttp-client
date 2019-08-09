package com.cisco.as.iot.opentsdb.request;

import com.cisco.as.iot.opentsdb.utils.AggregatorEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shifeng on 2016/5/19.
 * MyProject
 */
public class SubQueries {
    private AggregatorEnum aggregator;
    private String metric;
    private Boolean rate = false;
    private Map<String, String> rateOptions;
    private String downsample;
    private List<Filter> filters = new ArrayList<>();

    public SubQueries addAggregator(AggregatorEnum aggregator) {
        this.aggregator = aggregator;
        return this;
    }

    public SubQueries addMetric(String metric) {
        this.metric = metric;
        return this;
    }

    public SubQueries addDownsample(String downsample) {
        this.downsample = downsample;
        return this;
    }

    public SubQueries addFilter(Filter filter) {
        this.filters.add(filter);
        return this;
    }

    public SubQueries addFilter(List<Filter> filters) {
        this.filters.addAll(filters);
        return this;
    }

    public AggregatorEnum getAggregator() {
        return aggregator;
    }

    public void setAggregator(AggregatorEnum aggregator) {
        this.aggregator = aggregator;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Boolean getRate() {
        return rate;
    }

    public void setRate(Boolean rate) {
        this.rate = rate;
    }

    public Map<String, String> getRateOptions() {
        return rateOptions;
    }

    public void setRateOptions(Map<String, String> rateOptions) {
        this.rateOptions = rateOptions;
    }

    public String getDownsample() {
        return downsample;
    }

    public void setDownsample(String downsample) {
        this.downsample = downsample;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}
