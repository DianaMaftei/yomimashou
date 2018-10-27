package com.github.dianamaftei.yomimashou.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class ApplicationUserService implements UserDetailsService {

    private ApplicationUserRepository applicationUserRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(ApplicationUser user) {
        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new RuntimeException("Missing or empty email.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    }
}
