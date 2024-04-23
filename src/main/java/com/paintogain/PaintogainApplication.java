package com.paintogain;

import com.paintogain.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class PaintogainApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaintogainApplication.class, args);
	}

}
