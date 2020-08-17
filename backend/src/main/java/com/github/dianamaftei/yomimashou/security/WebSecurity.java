package com.github.dianamaftei.yomimashou.security;

import com.github.dianamaftei.yomimashou.user.ApplicationUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
@Order(2)
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final ApplicationUserService applicationUserService;
    private PasswordEncoder passwordEncoder;

    @Value("${security.SECRET}")
    public String secret;

    public WebSecurity(ApplicationUserService applicationUserService, PasswordEncoder passwordEncoder) {
        this.applicationUserService = applicationUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.logout().permitAll().logoutSuccessUrl("/api/users/afterLogout")
                .and().cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/file").authenticated()
                .antMatchers(HttpMethod.POST, "/api/users/textStatus").authenticated()
                .antMatchers(HttpMethod.GET, "/api/users/textStatus").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), secret))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), secret))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(applicationUserService).passwordEncoder(passwordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.setAllowedHeaders(Collections.unmodifiableList(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Username")));
        config.setExposedHeaders(Collections.unmodifiableList(Arrays.asList("Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Username")));
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Configuration
    @Order(1)
    public static class BasicWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Value("${security.SECRET}")
        public String secret;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable().authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/api/text").authenticated()
                    .and().httpBasic().and()
                    .addFilterBefore(new JWTAuthenticationFilter(authenticationManager(), secret), BasicAuthenticationFilter.class)
                    .addFilterBefore(new JWTAuthorizationFilter(authenticationManager(), secret), BasicAuthenticationFilter.class);
        }
    }
}
