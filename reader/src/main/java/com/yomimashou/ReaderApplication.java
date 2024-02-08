package com.yomimashou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.yomimashou"})
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ReaderApplication {

    public static void main(final String[] args) {
        final SpringApplication app = new SpringApplication(ReaderApplication.class);
        app.run();
    }
}
