package com.yomimashou.config;

import com.atilika.kuromoji.ipadic.Tokenizer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yomimashou.audit.UserAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;

@Configuration
public class AppConfig {

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
    public AuditorAware<String> auditorAware() {
        return new UserAuditorAware();
    }

    @Bean
    public Tokenizer getTokenizer() {
        return new Tokenizer();
    }
}
