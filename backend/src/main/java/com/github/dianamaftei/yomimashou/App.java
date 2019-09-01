package com.github.dianamaftei.yomimashou;

import com.github.dianamaftei.yomimashou.audit.UserAuditorAware;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ComponentScan({"com.github.dianamaftei.appscommon", "com.github.dianamaftei.yomimashou"})
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class App {

  public static void main(final String[] args) {
    final SpringApplication app = new SpringApplication(App.class);
    app.run();
  }

  // to use Querydsl
  @Bean
  public JPAQueryFactory jpaQueryFactory(final EntityManager entityManager) {
    return new JPAQueryFactory(entityManager);
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuditorAware<String> auditorAware() {
    return new UserAuditorAware();
  }
}
