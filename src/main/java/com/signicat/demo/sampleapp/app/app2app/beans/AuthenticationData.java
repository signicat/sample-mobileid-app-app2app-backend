package com.signicat.demo.sampleapp.app.app2app.beans;

public class AuthenticationData extends OperationData {
    private String deviceId;

    public AuthenticationData() {}

    public AuthenticationData(final String oidcBase, final String clientId, final String scope, final String redirectUri,
            final String authMethod, final String state, final String deviceId) {
        super(oidcBase, clientId, scope, redirectUri, authMethod, state);
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final String deviceId) {
        this.deviceId = deviceId;
    }
}
