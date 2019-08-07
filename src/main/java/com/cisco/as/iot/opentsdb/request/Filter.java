package com.cisco.as.iot.opentsdb.request;

import com.cisco.as.iot.opentsdb.utils.FilterTypeEnum;

/**
 * Created by shifeng on 2016/5/19.
 * MyProject
 */
public class Filter {
    private FilterTypeEnum type;
    private String tagk;
    private String filter;
    private Boolean groupBy = false;

    public FilterTypeEnum getType() {
        return type;
    }

    public void setType(FilterTypeEnum type) {
        this.type = type;
    }

    public String getTagk() {
        return tagk;
    }

    public void setTagk(String tagk) {
        this.tagk = tagk;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Boolean getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(Boolean groupBy) {
        this.groupBy = groupBy;
    }
}
