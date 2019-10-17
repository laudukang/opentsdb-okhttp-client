package me.codz.opentsdb.builder;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class DataPoint {

    // 监控指标项
    private String metric;

    // 时间戳 [生成时间]
    private long timestamp;

    // 值
    private Object value;

    // 标签kv项
    private Map<String, String> tags = Maps.newHashMap();

    private String interval;

    private String aggregator;

    private String groupByAggregator;

    DataPoint(String metric) {
        if (StringUtils.isBlank(metric)) {
            throw new IllegalArgumentException("empty metric name");
        }
        this.metric = metric;
    }

    /**
     * Adds a tag to the data point.
     *
     * @param name  tag identifier
     * @param value tag value
     * @return the metric the tag was added to
     */
    public DataPoint addTag(String name, String value) {
        if (StringUtils.isAnyBlank(name, value)) {
            throw new IllegalArgumentException("empty tag name or value");
        }
        tags.put(name, value);
        return this;
    }

    /**
     * Adds tags to the data point.
     *
     * @param tags map of tags
     * @return the metric the tags were added to
     */
    public DataPoint addTags(Map<String, String> tags) {
        checkNotNull(tags);
        this.tags.putAll(tags);
        return this;
    }

    /**
     * set the data point for the metric.
     *
     * @param timestamp when the measurement occurred
     * @param value     the measurement value
     * @return the metric
     */
    private DataPoint innerValue(long timestamp, Object value) {
        checkArgument(timestamp > 0);
        this.timestamp = timestamp;
        this.value = checkNotNull(value);
        return this;
    }

    /**
     * Adds the data point to the metric with a timestamp of now.
     *
     * @param value the measurement value
     * @return the metric
     */
    public DataPoint value(long value) {
        return innerValue(System.currentTimeMillis(), value);
    }

    public DataPoint value(long timestamp, long value) {
        return innerValue(timestamp, value);
    }

    /**
     * Adds the data point to the metric.
     *
     * @param timestamp when the measurement occurred
     * @param value     the measurement value
     * @return the metric
     */
    public DataPoint value(long timestamp, double value) {
        return innerValue(timestamp, value);
    }

    /**
     * Adds the data point to the metric with a timestamp of now.
     *
     * @param value the measurement value
     * @return the metric
     */
    public DataPoint value(double value) {
        return innerValue(System.currentTimeMillis(), value);
    }

    public DataPoint interval(String interval) {
        if (StringUtils.isBlank(interval)) {
            throw new IllegalArgumentException("empty interval");
        }
        this.interval = interval;
        return this;
    }

    public DataPoint aggregator(String aggregator) {
        if (StringUtils.isBlank(aggregator)) {
            throw new IllegalArgumentException("empty aggregator");
        }
        this.aggregator = aggregator;
        return this;
    }

    public DataPoint groupByAggregator(String groupByAggregator) {
        if (StringUtils.isBlank(groupByAggregator)) {
            throw new IllegalArgumentException("empty groupByAggregator");
        }
        this.groupByAggregator = groupByAggregator;
        return this;
    }

    /**
     * Time when the data point was measured.
     *
     * @return time when the data point was measured
     */
    public long getTimestamp() {
        return timestamp;
    }

    public Object getValue() {
        return value;
    }

    public String stringValue() {
        return value.toString();
    }

    public long longValue() throws DataFormatException {
        try {
            return ((Number) value).longValue();
        } catch (Exception e) {
            throw new DataFormatException("Value is not a long");
        }
    }

    public double doubleValue() throws DataFormatException {
        try {
            return ((Number) value).doubleValue();
        } catch (Exception e) {
            throw new DataFormatException("Value is not a double");
        }
    }

    public boolean isDoubleValue() {
        return !(((Number) value).doubleValue() == Math.floor(((Number) value).doubleValue()));
    }

    public boolean isIntegerValue() {
        return ((Number) value).doubleValue() == Math.floor(((Number) value).doubleValue());
    }

    /**
     * Returns the metric name.
     *
     * @return metric name
     */
    public String getMetric() {
        return metric;
    }

    /**
     * Returns the tags associated with the data point.
     *
     * @return tag for the data point
     */
    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(tags);
    }

    public String getInterval() {
        return interval;
    }

    public String getAggregator() {
        return aggregator;
    }

    public String getGroupByAggregator() {
        return groupByAggregator;
    }
}
