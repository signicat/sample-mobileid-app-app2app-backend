package com.signicat.demo.sampleapp.app.app2app.beans.responses;

public class ErrorResponse extends BaseResponse {
    public ErrorResponse() {
        super();
    }

    public ErrorResponse(final String errorMsg) {
        super("ERROR", errorMsg);
    }
}
