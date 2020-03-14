package com.signicat.demo.sampleapp.app.app2app.beans;

public class RegistrationData extends OperationData {

    public RegistrationData() {}

    public RegistrationData(final String oidcBase, final String clientId, final String scope, final String redirectUri,
            final String regMethod, final String state) {
        super(oidcBase, clientId, scope, redirectUri, regMethod, state);
    }
}
