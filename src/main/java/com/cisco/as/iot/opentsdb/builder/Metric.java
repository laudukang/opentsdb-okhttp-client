package com.cisco.as.iot.opentsdb.builder;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Metric {

    // 监控指标项
    @JSONField(name = "metric")
    private String name;

    // 时间戳 [生成时间]
    private long timestamp;

    // 值
    private Object value;

    // 标签kv项
    private Map<String, String> tags = Maps.newConcurrentMap();

    Metric(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("empty metric name");
        }
        this.name = name;
    }

    /**
     * Adds a tag to the data point.
     *
     * @param name  tag identifier
     * @param value tag value
     * @return the metric the tag was added to
     */
    public Metric addTag(String name, String value) {
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
    public Metric addTags(Map<String, String> tags) {
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
    private Metric innerAddDataPoint(long timestamp, Object value) {
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
    public Metric setDataPoint(long value) {
        return innerAddDataPoint(System.currentTimeMillis(), value);
    }

    public Metric setDataPoint(long timestamp, long value) {
        return innerAddDataPoint(timestamp, value);
    }

    /**
     * Adds the data point to the metric.
     *
     * @param timestamp when the measurement occurred
     * @param value     the measurement value
     * @return the metric
     */
    public Metric setDataPoint(long timestamp, double value) {
        return innerAddDataPoint(timestamp, value);
    }

    /**
     * Adds the data point to the metric with a timestamp of now.
     *
     * @param value the measurement value
     * @return the metric
     */
    public Metric setDataPoint(double value) {
        return innerAddDataPoint(System.currentTimeMillis(), value);
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

    public String stringValue()  {
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
    public String getName() {
        return name;
    }

    /**
     * Returns the tags associated with the data point.
     *
     * @return tag for the data point
     */
    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(tags);
    }
}
