package com.paintogain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

@Data
@ConfigurationProperties(prefix = "jwt")
public class AppConfig {

    private byte[] secretKey;

    public void setSecretKey(String secretKey) {
        this.secretKey = Base64.getDecoder().decode(secretKey);
    }

    public byte[] getSecretKey() {
        return secretKey;
    }
}
