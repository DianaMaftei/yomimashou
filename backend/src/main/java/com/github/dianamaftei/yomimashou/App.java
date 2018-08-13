package com.github.dianamaftei.yomimashou;

import com.github.dianamaftei.yomimashou.dictionary.creator.EntriesCreator;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Value("${unmarshal.XML}")
    private String unmarshalXML;

    @Autowired
    private EntriesCreator entriesCreator;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        app.run();
    }

    @Override
    public void run(String... args) throws Exception {
        if("true".equalsIgnoreCase(unmarshalXML)) {
            entriesCreator.run();
        }
    }

    // to use Querydsl
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

}
