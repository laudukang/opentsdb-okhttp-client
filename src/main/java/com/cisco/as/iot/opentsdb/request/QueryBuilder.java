package com.cisco.as.iot.opentsdb.request;

import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class QueryBuilder {

	private transient Gson mapper;
	private Query query = new Query();

	private QueryBuilder() {
		GsonBuilder builder = new GsonBuilder();
		mapper = builder.create();
	}

	public static QueryBuilder getInstance() {
		return new QueryBuilder();
	}

	public Query getQuery() {
		return this.query;
	}

	public String build() throws IOException {
		// verify that there is at least one tag for each metric
		checkState(query.getStart() > 0, " must contain start.");
		checkState(query.getQueries() != null, " must contain at least one subQuery.");
		return mapper.toJson(query);
	}
}
