package org.javacode.spring_security_oauth2.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.javacode.spring_security_oauth2.model.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final Long id;
    private final String name;
    private final String email;
    private final Role role;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "id", id.toString(),
                "username", name,
                "email", email,
                "role", role.name());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

}