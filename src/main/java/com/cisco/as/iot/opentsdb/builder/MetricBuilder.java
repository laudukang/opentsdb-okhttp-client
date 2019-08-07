/*
 * Copyright 2013 Proofpoint Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.cisco.as.iot.opentsdb.builder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * Builder used to create the JSON to push metrics to KairosDB.
 */
public class MetricBuilder {

    private List<Metric> metrics = Lists.newArrayList();

    private MetricBuilder() {
    }

    /**
     * Returns a new metric builder.
     *
     * @return metric builder
     */
    public static MetricBuilder getInstance() {
        return new MetricBuilder();
    }

    /**
     * Adds a metric to the builder.
     *
     * @param metricName metric name
     * @return the new metric
     */
    public Metric addMetric(String metricName) {
        Metric metric = new Metric(metricName);
        metrics.add(metric);
        return metric;
    }

    /**
     * Returns a list of metrics added to the builder.
     *
     * @return list of metrics
     */
    public List<Metric> getMetrics() {
        return metrics;
    }

    public String build() {
        for (Metric metric : metrics) {
            // verify that there is at least one tag for each metric
            checkState(metric.getTags().size() > 0, metric.getName() + " must contain at least one tag.");
        }
        return JSON.toJSONString(metrics, SerializerFeature.IgnoreNonFieldGetter);
    }
}
