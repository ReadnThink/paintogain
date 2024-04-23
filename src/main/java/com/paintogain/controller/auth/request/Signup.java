package com.paintogain.controller.auth.request;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Signup {

    private String email;
    private String name;
    private String password;

}
