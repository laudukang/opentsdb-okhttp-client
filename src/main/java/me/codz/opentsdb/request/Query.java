package me.codz.opentsdb.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shifeng on 2016/5/19.
 * MyProject
 */

public class Query {

    private Object start;
    private long end;
    private List<SubQueries> queries = new ArrayList<>();
    private Boolean noAnnotations = Boolean.FALSE;
    private Boolean globalAnnotations = Boolean.FALSE;
    private Boolean msResolution = Boolean.FALSE;
    private Boolean showTSUIDs = Boolean.FALSE;
    private Boolean showSummary = Boolean.FALSE;
    private Boolean showQuery = Boolean.FALSE;
    private Boolean showStats = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;
    private String timezone = "UTC";
    private Boolean useCalendar = Boolean.FALSE;
    private Object customField;
    private long limit;

    public Query() {
    }

    public Query addSubQuery(SubQueries subQueries) {
        this.queries.add(subQueries);
        return this;
    }

    public Query addStart(long start) {
        this.start = start;
        return this;
    }

    public Query addStart(String start) {
        this.start = start;
        return this;
    }

    public Query addEnd(long end) {
        this.end = end;
        return this;
    }

    public Query noAnnotations() {
        this.noAnnotations = Boolean.TRUE;
        return this;
    }

    public Query globalAnnotations() {
        this.globalAnnotations = Boolean.TRUE;
        return this;
    }

    public Query msResolution() {
        this.msResolution = Boolean.TRUE;
        return this;
    }

    public Query showTSUIDs() {
        this.showTSUIDs = Boolean.TRUE;
        return this;
    }

    public Query showQuery() {
        this.showQuery = Boolean.TRUE;
        return this;
    }

    public Query showSummary() {
        this.showSummary = Boolean.TRUE;
        return this;
    }

    public Query showStats() {
        this.showStats = Boolean.TRUE;
        return this;
    }

    public Query delete() {
        this.delete = Boolean.TRUE;
        return this;
    }

    public Query customField(Object customField) {
        this.customField = customField;
        return this;
    }

    public Query timezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public Query useCalendar(Boolean useCalendar) {
        this.useCalendar = useCalendar;
        return this;
    }

    public Query limit(long limit) {
        this.limit = limit;
        return this;
    }

    public Object getStart() {
        return start;
    }

    public void setStart(Object start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public List<SubQueries> getQueries() {
        return queries;
    }

    public void setQueries(List<SubQueries> queries) {
        this.queries = queries;
    }

    public Boolean getNoAnnotations() {
        return noAnnotations;
    }

    public void setNoAnnotations(Boolean noAnnotations) {
        this.noAnnotations = noAnnotations;
    }

    public Boolean getGlobalAnnotations() {
        return globalAnnotations;
    }

    public void setGlobalAnnotations(Boolean globalAnnotations) {
        this.globalAnnotations = globalAnnotations;
    }

    public Boolean getMsResolution() {
        return msResolution;
    }

    public void setMsResolution(Boolean msResolution) {
        this.msResolution = msResolution;
    }

    public Boolean getShowTSUIDs() {
        return showTSUIDs;
    }

    public void setShowTSUIDs(Boolean showTSUIDs) {
        this.showTSUIDs = showTSUIDs;
    }

    public Boolean getShowSummary() {
        return showSummary;
    }

    public void setShowSummary(Boolean showSummary) {
        this.showSummary = showSummary;
    }

    public Boolean getShowQuery() {
        return showQuery;
    }

    public void setShowQuery(Boolean showQuery) {
        this.showQuery = showQuery;
    }

    public Boolean getShowStats() {
        return showStats;
    }

    public void setShowStats(Boolean showStats) {
        this.showStats = showStats;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Object getCustomField() {
        return customField;
    }

    public void setCustomField(Object customField) {
        this.customField = customField;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Boolean getUseCalendar() {
        return useCalendar;
    }

    public void setUseCalendar(Boolean useCalendar) {
        this.useCalendar = useCalendar;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }
}
