package com.github.dianamaftei.yomimashou.security;

import static com.github.dianamaftei.yomimashou.security.SecurityConstants.SIGN_UP_URL;

import java.util.Arrays;
import java.util.Collections;
import com.github.dianamaftei.yomimashou.user.ApplicationUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private ApplicationUserService applicationUserService;
    private PasswordEncoder passwordEncoder;

    @Value("${security.SECRET}")
    public String SECRET;

    public WebSecurity(ApplicationUserService applicationUserService, PasswordEncoder passwordEncoder) {
        this.applicationUserService = applicationUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.logout().permitAll().logoutSuccessUrl("/api/users/afterLogout")
                .and().cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/afterLogin/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/dictionary/*/**").permitAll()
                .antMatchers("/api/text/parse/*").permitAll()
                .antMatchers("/api/text/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/text").authenticated()
                .antMatchers(HttpMethod.GET, "/static/**").permitAll()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), SECRET))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), SECRET))
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
}
