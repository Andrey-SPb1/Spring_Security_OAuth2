package org.javacode.spring_security_oauth2.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.javacode.spring_security_oauth2.exception.TokenExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class OidcLogoutSuccessHandler implements LogoutSuccessHandler {

    private final OidcClient oidcClient;
    private final OAuth2AuthorizedClientService authorizedClientService;

    private static final Logger logger = LoggerFactory.getLogger(OidcLogoutSuccessHandler.class);

    @SneakyThrows
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2AuthorizedClient authorizedClient = this.getAuthorizedClient(oauthToken);
            String accessToken = authorizedClient.getAccessToken().getTokenValue();

            if (accessToken != null) {
                oidcClient.revokeToken(accessToken);
            } else {
                logger.warn("No access token found for user {}", authentication.getName());
            }

            logger.info("The user({}) has successfully logged out", authentication.getName());
            response.sendRedirect("/logout-success");
        }
    }

    private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken oauthToken) {
        return authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());
    }

}