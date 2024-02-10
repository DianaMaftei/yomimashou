package com.yomimashou.config;

import com.atilika.kuromoji.ipadic.Tokenizer;
import com.yomimashou.audit.UserAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

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
