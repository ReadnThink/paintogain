package com.paintogain.config.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserPrincipal extends User {

    private final Long userId;

    /**
     * Role : ROLE_ 필요
     * Authority : ROLE_ 필요업음
     */
    public UserPrincipal(com.paintogain.domain.User appUser) {
        super(appUser.getEmail(), appUser.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.userId = appUser.getId();
    }

    public Long getUserId() {
        return userId;
    }
}
