package org.javacode.spring_security_oauth2.authentication;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OidcClient {

    private final String tokenRevocationUrl = "https://oauth2.googleapis.com/revoke"; // Google Token Revocation URL
    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(OidcClient.class);

    public void revokeToken(String token) {
        String url = tokenRevocationUrl + "?token=" + token;
        try {
            restTemplate.postForEntity(url, null, String.class);
            logger.info("Successfully revoked token");
        } catch (Exception e) {
            logger.error("The token has not been revoked. Exception - {}", e.getMessage());
        }
    }
}