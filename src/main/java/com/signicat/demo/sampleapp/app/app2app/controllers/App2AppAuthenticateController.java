package com.signicat.demo.sampleapp.app.app2app.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.signicat.demo.sampleapp.app.app2app.OIDCProperties;
import com.signicat.demo.sampleapp.app.app2app.beans.AuthenticationData;
import com.signicat.demo.sampleapp.app.app2app.exception.ApplicationException;
import com.signicat.demo.sampleapp.app.app2app.utils.OIDCUtils;

@RestController("App2AppAuthenticateController")
@RequestMapping("/authenticate")
@EnableAutoConfiguration
public class App2AppAuthenticateController {

    private static final Logger LOG = LoggerFactory.getLogger(App2AppAuthenticateController.class);

    @Value("${server.servlet.session.timeout}")
    protected String            sessionTimeout;

    @Autowired
    protected OIDCProperties    oidcProperties;

    @GetMapping("/start")
    public void startAuthentication(
            @RequestParam(value = "deviceId", required = true) final String deviceId,
            final HttpServletRequest request, final HttpServletResponse response) {
        LOG.info("PATH: /start ");

        final AuthenticationData authenticationData = prepareAuthenticationData(deviceId);
        final String authorizeUrl = OIDCUtils.getAuthorizeUri(authenticationData);
        LOG.info("AUTHORIZE URL:" + authorizeUrl);

        request.getSession().setMaxInactiveInterval(Integer.parseInt(this.sessionTimeout));
        request.getSession().setAttribute(OIDCUtils.ORIG_STATE, authenticationData.getState());

        try {
            response.sendRedirect(authorizeUrl);
        } catch (final IOException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    private AuthenticationData prepareAuthenticationData(final String deviceId) {
        final String state = OIDCUtils.createState(oidcProperties.getAcrValues() + oidcProperties.getAuthMethod());

        return new AuthenticationData(oidcProperties.getOidcBase() + OIDCUtils.AUTHORIZE_RESPONSE_TYPE_CODE, oidcProperties.getClientId(),
                oidcProperties.getScope(),
                oidcProperties.getRedirectUri(),
                oidcProperties.getAcrValues() + oidcProperties.getAuthMethod(), state, deviceId);
    }

}
