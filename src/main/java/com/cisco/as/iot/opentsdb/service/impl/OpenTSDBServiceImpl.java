package com.cisco.as.iot.opentsdb.service.impl;

import com.cisco.as.iot.opentsdb.service.OpenTSDBService;

/**
 * <p>Created with IDEA
 * <p>Author: laudukang
 * <p>Date: 2019/8/6
 * <p>Time: 20:49
 * <p>Version: 1.0
 */
public class OpenTSDBServiceImpl implements OpenTSDBService {
    private String openTSDBServer = "http://localhost:4242";

    public OpenTSDBServiceImpl() {
    }

    public OpenTSDBServiceImpl(String openTSDBServer) {
        this.openTSDBServer = openTSDBServer;
    }

    @Override
    public String getOpenTSDBServer() {
        return openTSDBServer;
    }
}
