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
package me.codz.opentsdb.builder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * Builder used to create the JSON to push metrics to KairosDB.
 */
public class DataPointBuilder {

    private List<DataPoint> dataPoints = Lists.newArrayList();

    private DataPointBuilder() {
    }

    /**
     * Returns a new metric builder.
     *
     * @return metric builder
     */
    public static DataPointBuilder getInstance() {
        return new DataPointBuilder();
    }

    /**
     * Adds a metric to the builder.
     *
     * @param metricName metric name
     * @return the new metric
     */
    public DataPoint addMetric(String metricName) {
        DataPoint dataPoint = new DataPoint(metricName);
        dataPoints.add(dataPoint);
        return dataPoint;
    }

    /**
     * Returns a list of metrics added to the builder.
     *
     * @return list of metrics
     */
    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public String build() {
        for (DataPoint dataPoint : dataPoints) {
            // verify that there is at least one tag for each metric
            checkState(dataPoint.getTags().size() > 0, dataPoint.getMetric() + " must contain at least one tag.");
        }
        return JSON.toJSONString(dataPoints, SerializerFeature.IgnoreNonFieldGetter);
    }
}
