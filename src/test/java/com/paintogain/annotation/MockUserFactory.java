package com.paintogain.annotation;

import com.paintogain.config.security.UserPrincipal;
import com.paintogain.domain.User;
import com.paintogain.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class MockUserFactory implements WithSecurityContextFactory<CustomWithMockUser> {

    private final UserRepository userRepository;

    public MockUserFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SecurityContext createSecurityContext(CustomWithMockUser annotation) {
        var role = annotation.role();
        var user = User.builder()
                .email(annotation.email())
                .name(annotation.name())
                .password(annotation.password())
                .build();
        userRepository.save(user);

        var principal = new UserPrincipal(user);

        var authenticationToken = new UsernamePasswordAuthenticationToken(
                principal, user.getPassword(), List.of(new SimpleGrantedAuthority(role))
        );
        var context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}
