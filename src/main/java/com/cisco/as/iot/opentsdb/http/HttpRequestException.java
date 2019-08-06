package com.cisco.as.iot.opentsdb.http;

public class HttpRequestException extends Exception {

    private static final long serialVersionUID = 2545413655207070449L;

    public HttpRequestException() {
        super();
    }

    public HttpRequestException(String s) {
        super(s);
    }

    public HttpRequestException(String s, Exception ex) {
        super(s);
    }
}
