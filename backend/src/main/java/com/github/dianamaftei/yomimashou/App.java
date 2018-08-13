package com.github.dianamaftei.yomimashou;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

@SpringBootApplication
public class App implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        app.run();
    }

    @Override
    public void run(String... args) throws Exception {
    }

    // to use Querydsl
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

}
