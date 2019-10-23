package me.codz.opentsdb.response;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>Created with IDEA
 * <p>Author: laudukang
 * <p>Date: 2019/8/7
 * <p>Time: 17:21
 * <p>Version: 1.0
 */
public class QueryResponse {
    private int code;           //error response
    private String message;     //error response
    private String details;     //error response

    private List<MetricData> metricDatas = new ArrayList<>();
    private String statsSummary;

    public boolean hasError() {
        return code != 0;
    }

    public QueryResponse addMetricData(MetricData metricData) {
        this.metricDatas.add(metricData);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<MetricData> getMetricDatas() {
        return metricDatas;
    }

    public void setMetricDatas(List<MetricData> metricDatas) {
        this.metricDatas = metricDatas;
    }

    public String getStatsSummary() {
        return statsSummary;
    }

    public void setStatsSummary(String statsSummary) {
        this.statsSummary = statsSummary;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public static class MetricData {
        private String metric;
        private List<String> aggregateTags;
        private List<String> tsuids;
        private Map<String, String> tags;
        private Map<String, Long> dps;
        private Map<String, Long> stats;
        private List<Map<String, Long>> annotations;
        private List<Map<String, Long>> globalAnnotations;
        private String query;

        public String getMetric() {
            return metric;
        }

        public void setMetric(String metric) {
            this.metric = metric;
        }

        public List<String> getAggregateTags() {
            return aggregateTags;
        }

        public void setAggregateTags(List<String> aggregateTags) {
            this.aggregateTags = aggregateTags;
        }

        public List<String> getTsuids() {
            return tsuids;
        }

        public void setTsuids(List<String> tsuids) {
            this.tsuids = tsuids;
        }

        public Map<String, String> getTags() {
            return tags;
        }

        public void setTags(Map<String, String> tags) {
            this.tags = tags;
        }

        public Map<String, Long> getDps() {
            return dps;
        }

        public void setDps(Map<String, Long> dps) {
            this.dps = dps;
        }

        public Map<String, Long> getStats() {
            return stats;
        }

        public void setStats(Map<String, Long> stats) {
            this.stats = stats;
        }

        public List<Map<String, Long>> getAnnotations() {
            return annotations;
        }

        public void setAnnotations(List<Map<String, Long>> annotations) {
            this.annotations = annotations;
        }

        public List<Map<String, Long>> getGlobalAnnotations() {
            return globalAnnotations;
        }

        public void setGlobalAnnotations(List<Map<String, Long>> globalAnnotations) {
            this.globalAnnotations = globalAnnotations;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public long dpsSum() {
            if (Objects.isNull(dps) || dps.isEmpty()) {
                return 0;
            }

            return dps.values().stream().mapToLong(v -> v).sum();
        }
    }


}
