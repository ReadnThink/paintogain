package com.paintogain.annotation;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockUserFactory.class) // 회원가입 진행하고 UserPrincipal을 넣어줌
public @interface CustomWithMockUser {

    String email() default "sol@gmail.com";
    String name() default "sol";
    String role() default "ROLE_USER";
    String password() default "1234";

}
