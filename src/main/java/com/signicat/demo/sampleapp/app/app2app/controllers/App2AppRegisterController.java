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
import org.springframework.web.bind.annotation.RestController;

import com.signicat.demo.sampleapp.app.app2app.OIDCProperties;
import com.signicat.demo.sampleapp.app.app2app.beans.RegistrationData;
import com.signicat.demo.sampleapp.app.app2app.exception.ApplicationException;
import com.signicat.demo.sampleapp.app.app2app.utils.OIDCUtils;

@RestController("App2AppRegisterController")
@RequestMapping("/register")
@EnableAutoConfiguration
public class App2AppRegisterController {

    private static final Logger LOG = LoggerFactory.getLogger(App2AppRegisterController.class);

    @Value("${server.servlet.session.timeout}")
    private String              sessionTimeout;

    @Autowired
    private OIDCProperties      oidcProperties;

    @GetMapping("/start")
    public void startRegistartion(final HttpServletRequest request, final HttpServletResponse response) {
        LOG.info("PATH: /start ");

        final RegistrationData registrationData = prepareRegistrationData();
        final String authorizeUrl = OIDCUtils.getAuthorizeUri(registrationData);
        LOG.info("AUTHORIZE URL:" + authorizeUrl);

        request.getSession().setMaxInactiveInterval(Integer.parseInt(this.sessionTimeout));
        request.getSession().setAttribute(OIDCUtils.ORIG_STATE, registrationData.getState());

        try {
            response.sendRedirect(authorizeUrl);
        } catch (final IOException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    private RegistrationData prepareRegistrationData() {
        final String state = OIDCUtils.createState(oidcProperties.getAcrValues() + oidcProperties.getRegMethod());

        return new RegistrationData(oidcProperties.getOidcBase() + OIDCUtils.AUTHORIZE_RESPONSE_TYPE_CODE, oidcProperties.getClientId(),
                oidcProperties.getScope(),
                oidcProperties.getRedirectUri(),
                oidcProperties.getAcrValues() + oidcProperties.getRegMethod(), state);
    }

}
