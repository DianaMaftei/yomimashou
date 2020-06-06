package com.github.dianamaftei.yomimashou.user;

import static java.util.Collections.emptyList;

import com.github.dianamaftei.yomimashou.text.ProgressStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicationUserService implements UserDetailsService {

  private final ApplicationUserRepository applicationUserRepository;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public ApplicationUserService(ApplicationUserRepository applicationUserRepository,
      PasswordEncoder passwordEncoder) {
    this.applicationUserRepository = applicationUserRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void register(ApplicationUser user) {
    validateUserInfo(user);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    applicationUserRepository.save(user);
  }

  private void validateUserInfo(ApplicationUser user) {
    if (user.getEmail() == null || user.getEmail().isEmpty()) {
      throw new InvalidUserInfoException("Missing or empty email.");
    }

    if (user.getUsername() == null || user.getUsername().isEmpty()) {
      throw new InvalidUserInfoException("Missing or empty username.");
    }

    if (applicationUserRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new InvalidUserInfoException("Email already in use");
    }

    if (applicationUserRepository.findByUsername(user.getUsername()).isPresent()) {
      throw new InvalidUserInfoException("Username already in use");
    }
  }

  public Optional<ApplicationUser> findById(Long id) {
    return applicationUserRepository.findById(id);
  }

  @Override
  public UserDetails loadUserByUsername(String email) {

    ApplicationUser applicationUser = applicationUserRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(email));

    return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
  }

  public void setTextStatus(String username, Long textId, ProgressStatus progressStatus) {
    applicationUserRepository.findByUsername(username).ifPresent(applicationUser -> {
      Map<Long, ProgressStatus> textsStatuses = applicationUser.getTextsStatuses();
      textsStatuses.put(textId, progressStatus);
      applicationUserRepository.save(applicationUser);
    });
  }

  public Map<Long, ProgressStatus> getTextStatuses(String username) {
    Optional<ApplicationUser> user = applicationUserRepository.findByUsername(username);
    if (user.isPresent()) {
      return user.get().getTextsStatuses();
    }
    return new HashMap<>();
  }
}
