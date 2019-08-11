package com.github.dianamaftei.yomimashou;

import com.github.dianamaftei.yomimashou.audit.UserAuditorAware;
import com.github.dianamaftei.yomimashou.dictionary.creator.EntriesCreator;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class App implements CommandLineRunner {

  @Value("${unmarshal.XML}")
  private String unmarshalXML;

  @Autowired
  private EntriesCreator entriesCreator;

  public static void main(final String[] args) {
    final SpringApplication app = new SpringApplication(App.class);
    app.run();
  }

  @Override
  public void run(final String... args) throws Exception {
    if ("true".equalsIgnoreCase(unmarshalXML)) {
      entriesCreator.run();
    }
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
