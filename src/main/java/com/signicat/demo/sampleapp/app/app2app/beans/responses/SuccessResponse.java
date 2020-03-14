package com.signicat.demo.sampleapp.app.app2app.beans.responses;

public class SuccessResponse extends BaseResponse {
    public SuccessResponse() {
        super();
    }

    public SuccessResponse(final Object successData) {
        super("SUCCESS", successData);
    }

}
