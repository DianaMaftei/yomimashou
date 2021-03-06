package com.github.dianamaftei.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@ComponentScan({"com.github.dianamaftei.appscommon", "com.github.dianamaftei.creator"})
@SpringBootApplication
public class CreatorApplication implements CommandLineRunner {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.build();
  }

  @Autowired
  private EntriesCreator entriesCreator;

  public static void main(final String[] args) {
    SpringApplication.run(CreatorApplication.class, args);
  }

  @Override
  public void run(final String... args) {
    entriesCreator.run();
  }
}