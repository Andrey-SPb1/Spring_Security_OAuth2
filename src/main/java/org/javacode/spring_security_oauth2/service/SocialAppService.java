package org.javacode.spring_security_oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.javacode.spring_security_oauth2.exception.TokenExpiredException;
import org.javacode.spring_security_oauth2.model.entity.Role;
import org.javacode.spring_security_oauth2.model.entity.User;
import org.javacode.spring_security_oauth2.authentication.CustomOAuth2User;
import org.javacode.spring_security_oauth2.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SocialAppService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SocialAppService.class);

    @SneakyThrows
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        checkToken(userRequest.getAccessToken());

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = userRepository.findByEmail(email);

        if (user == null) {
            if (name != null) {
                user = User.builder()
                        .username(name)
                        .email(email)
                        .role(name.toLowerCase().contains("admin") ? Role.ADMIN : Role.USER)
                        .build();

                userRepository.save(user);
            } else {
                logger.error("User name is empty");
                throw new UsernameNotFoundException("User name is empty");
            }
            logger.info("User created: {}", user);
        } else {
            logger.info("User found: {}", user);
        }

        return new CustomOAuth2User(user.getId(), name, email, user.getRole());
    }

    private void checkToken(OAuth2AccessToken accessToken) throws TokenExpiredException {
        if (accessToken != null) {
            Instant expiresAt = accessToken.getExpiresAt();

            if (expiresAt != null && expiresAt.isBefore(Instant.now())) {
                logger.info("Access token expired");
                throw new TokenExpiredException("Access token expired");
            }
        }
    }
}