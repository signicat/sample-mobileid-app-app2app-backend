package com.signicat.demo.sampleapp.app.app2app.controllers;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.signicat.demo.sampleapp.app.app2app.OIDCProperties;
import com.signicat.demo.sampleapp.app.app2app.beans.responses.BaseResponse;
import com.signicat.demo.sampleapp.app.app2app.beans.responses.ErrorResponse;
import com.signicat.demo.sampleapp.app.app2app.beans.responses.SuccessResponse;
import com.signicat.demo.sampleapp.app.app2app.exception.ApplicationException;
import com.signicat.demo.sampleapp.app.app2app.utils.OIDCUtils;

@RestController("BackendController")
@RequestMapping("")
@EnableAutoConfiguration
public class BackendController {

    private static final Logger LOG = LoggerFactory.getLogger(BackendController.class);

    @Autowired
    private OIDCProperties      oidcProperties;

    @GetMapping("/consumeOidc")
    public @ResponseBody BaseResponse consume(
            @RequestParam(value = "state", required = true) final String state,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {

        LOG.debug("/consumeOidc message received with state= " + state);

        final Map<String, String[]> paramsMap = request.getParameterMap();
        LOG.debug("paramsMap " + paramsMap.toString());

        if (paramsMap.containsKey("error")) {
            LOG.error(paramsMap.get("error_description")[0]);
            return new ErrorResponse(paramsMap.get("error_description")[0]);
        }

        final String originalState = (String) request.getSession().getAttribute(OIDCUtils.ORIG_STATE);
        if (originalState == null || !originalState.equals(state)) {
            return new ErrorResponse("State empty or mismatch");
        }

        if (paramsMap.containsKey("code")) {
            final String code = paramsMap.get("code")[0];
            return processAuthorizationCode(code);
        }

        return new ErrorResponse("Fail. Something went wrong!!!");
    }

    private BaseResponse processAuthorizationCode(final String code) throws IOException {

        final CloseableHttpClient httpClient =
                HttpClientBuilder.create().useSystemProperties().build();

        LOG.debug("Received code =" + code + " - exec /token request to obtain access_token");
        final JSONObject tokenJson = OIDCUtils.getToken(httpClient, oidcProperties, code);

        final String errorDesc = "error_description";
        if (tokenJson.containsKey(errorDesc)) {
            LOG.error(errorDesc);
            return new ErrorResponse((String) tokenJson.get(errorDesc));
        }

        final String accessToken = (String) tokenJson.get("access_token");
        LOG.debug("Received accessToken =" + accessToken + " - exec /userinfo request");

        final JSONObject userInfoJson = getUserInfo(httpClient, accessToken);
        httpClient.close();
        if (!userInfoJson.isEmpty()) {
            return new SuccessResponse(userInfoJson);
        }

        return new ErrorResponse("Fail. Something went wrong!!!");
    }

    private JSONObject getUserInfo(final CloseableHttpClient httpClient, final String accessToken) {
        final HttpGet httpGet = new HttpGet(oidcProperties.getIssuerId() + "userinfo");
        httpGet.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        httpGet.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());

        try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
            final String content = EntityUtils.toString(httpResponse.getEntity(), Charsets.UTF_8);
            return new ObjectMapper().readValue(content, JSONObject.class);
        } catch (final IOException e) {
            throw new ApplicationException("Fetching userinfo failed: " + e.getMessage());
        }
    }
}
