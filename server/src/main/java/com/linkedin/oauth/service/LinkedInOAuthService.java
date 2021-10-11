package com.linkedin.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.oauth.builder.AuthorizationUrlBuilder;
import com.linkedin.oauth.builder.ScopeBuilder;
import com.linkedin.oauth.pojo.AccessToken;
import com.linkedin.oauth.util.Preconditions;


import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import static com.linkedin.oauth.util.Constants.*;


/**
 * LinkedIn 3-Legged OAuth Service
 */
@SuppressWarnings({"AvoidStarImport"})
@RestController
public final class LinkedInOAuthService {

    private final String redirectUri;
    private final String apiKey;
    private final String apiSecret;
    private final String scope;

    private LinkedInOAuthService(final LinkedInOAuthServiceBuilder oauthServiceBuilder) {
        this.redirectUri = oauthServiceBuilder.redirectUri;
        this.apiKey = oauthServiceBuilder.apiKey;
        this.apiSecret = oauthServiceBuilder.apiSecret;
        this.scope = oauthServiceBuilder.scope;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getScope() {
        return scope;
    }

    /**
     *
     * @return an instance of {@link AuthorizationUrlBuilder}
     */
    public AuthorizationUrlBuilder createAuthorizationUrlBuilder() {
        return new AuthorizationUrlBuilder(this);
    }

    /**
     *
     * @param code authorization code
     * @return response of LinkedIn's 3-legged token flow captured in a POJO {@link AccessToken}
     * @throws IOException
     */
    public HttpEntity getAccessToken(final String code) throws IOException {

        MultiValueMap<String, String> parameters= new LinkedMultiValueMap<String, String>();
        parameters.add(GRANT_TYPE, GrantType.AUTHORIZATION_CODE.getGrantType());
        parameters.add(CODE, code);
        parameters.add(REDIRECT_URI, this.redirectUri);
        parameters.add(CLIENT_ID, this.apiKey);
        parameters.add(CLIENT_SECRET, this.apiSecret);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
        return request;

    }

    /**
     *
     * @param refreshToken the refresh token obtained from the authorization code exchange
     * @return response of LinkedIn's refresh token flow captured in a POJO {@link AccessToken}
     * @throws IOException
     */
    public HttpEntity getAccessTokenFromRefreshToken(final String refreshToken) throws IOException {
        MultiValueMap<String, String> parameters= new LinkedMultiValueMap<String, String>();
        parameters.add(GRANT_TYPE, GrantType.REFRESH_TOKEN.getGrantType());
        parameters.add(REFRESH_TOKEN, refreshToken);
        parameters.add(CLIENT_ID, this.apiKey);
        parameters.add(CLIENT_SECRET, this.apiSecret);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
        //String response = restTemplate.postForObject(REQUEST_TOKEN_URL, request , String.class);
        return request;
    }

    /**
     * Get access token by LinkedIn's OAuth2.0 Client Credentials flow
     * @return JSON String response
     * @throws IOException
     * String response= restTemplate.postForObject(REQUEST_TOKEN_URL, request , String.class);
     *       accessToken[0] = service.convertJsonTokenToPojo(response);
     */
    public HttpEntity getAccessToken()
    {
        MultiValueMap<String, String> parameters= new LinkedMultiValueMap<String, String>();
        parameters.add(GRANT_TYPE, GrantType.CLIENT_CREDENTIALS.getGrantType());
        parameters.add(CLIENT_ID, this.apiKey);
        parameters.add(CLIENT_SECRET, this.apiSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
        return request;
    }

    /**
     * Introspect token using LinkedIn's Auth2.0 tokenIntrospect API
     * @param token String representation of the access token
     * @return JSON String response
     */
    public HttpEntity introspectToken(final String token) {
        MultiValueMap<String, String> parameters= new LinkedMultiValueMap<String, String>();
        parameters.add(CLIENT_ID, this.apiKey);
        parameters.add(CLIENT_SECRET, this.apiSecret);
        parameters.add(TOKEN, token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
        return request;
    }

    /**
     * Method to convert JSON String OAuth Token to POJO
     * @param accessToken
     * @return
     * @throws IOException
     */
    public AccessToken convertJsonTokenToPojo(final String accessToken) throws IOException {
        return new ObjectMapper().readValue(accessToken, AccessToken.class);
    }

    /**
     * Method to get the access token via the 3-legged LinkedIn OAuth2.0 flow.
     * The consumer of this method does not need to invoke the {@link #getAccessToken(String)} separately,
     * as this is implicitly taken care of by the HttpHandler class
     *
     * @param service an instance of the LinkedInOAuthService
     * @throws Exception

    public AccessToken getAccessToken(@RequestParam(name="code", required=false) String code, final LinkedInOAuthService service) throws Exception {
        AccessToken accessToken = new AccessToken();

        final String secretState = "secret" + new Random().nextInt(999_999);
        final String authorizationUrl = service.createAuthorizationUrlBuilder()
            .state(secretState)
            .build();

        System.out.println("Authorize LinkedIn here:");
        System.out.println(authorizationUrl);

        if(code != null){
            HttpEntity response = getAccessToken(code);
            accessToken = service.convertJsonTokenToPojo(response);
        }
        return accessToken;
    }
     */

    /**
     * Builder class for LinkedIn's OAuth Service
     */
    public static final class LinkedInOAuthServiceBuilder {
        private String redirectUri;
        private String apiKey;
        private String apiSecret;
        private String scope;

        public LinkedInOAuthServiceBuilder apiKey(final String apiKey) {
            Preconditions.checkEmptyString(apiKey, "Invalid Api key");
            this.apiKey = apiKey;
            return this;
        }

        public LinkedInOAuthServiceBuilder apiSecret(final String apiSecret) {
            Preconditions.checkEmptyString(apiSecret, "Invalid Api secret");
            this.apiSecret = apiSecret;
            return this;
        }

        public LinkedInOAuthServiceBuilder callback(final String callback) {
            this.redirectUri = callback;
            return this;
        }

        private LinkedInOAuthServiceBuilder setScope(final String scope) {
            Preconditions.checkEmptyString(scope, "Invalid OAuth scope");
            this.scope = scope;
            return this;
        }

        public LinkedInOAuthServiceBuilder defaultScope(final ScopeBuilder scopeBuilder) {
            return setScope(scopeBuilder.build());
        }

        public LinkedInOAuthServiceBuilder defaultScope(final String scope) {
            return setScope(scope);
        }

        public LinkedInOAuthService build() {

            LinkedInOAuthService baseService = new LinkedInOAuthService(this);
            return baseService;
        }
    }

}