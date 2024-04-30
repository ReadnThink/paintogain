package com.paintogain.annotation;

import com.paintogain.domain.User;
import com.paintogain.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class MockUserFactory implements WithSecurityContextFactory<CustomWithMockUser> {


    @Override
    public SecurityContext createSecurityContext(CustomWithMockUser annotation) {
        String username = annotation.username();
        String role = annotation.role();
        User user = User.builder()
                .email(username)
                .build();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user, "", List.of(new SimpleGrantedAuthority(role))
        );
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        return context;
    }
}
