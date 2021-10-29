package com.yomimashou.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class ApplicationUserServiceTest {

  @InjectMocks
  private ApplicationUserService applicationUserService;

  @Mock
  private ApplicationUserRepository applicationUserRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  private ApplicationUser applicationUser;

  private final String username = "JohnDemo";

  private String email = "test@email.com";

  private Long userId = 1L;

  @BeforeEach
  void setUp() throws Exception {
    applicationUser = new ApplicationUser();
    applicationUser.setId(userId);
    applicationUser.setUsername(username);
    applicationUser.setEmail(email);
    applicationUser.setPassword("password");
  }

  @Test
  void registerShouldSaveValidUser() throws Exception {
    when(applicationUserRepository.save(applicationUser)).thenReturn(applicationUser);
    when(passwordEncoder.encode(applicationUser.getPassword())).thenReturn("jibberish");
    applicationUserService.register(applicationUser);
    verify(applicationUserRepository, times(1)).save(applicationUser);
    assertEquals("jibberish", applicationUser.getPassword());
  }

  @Test
  void registerShouldThrowExceptionForInvalidUser() throws Exception {
    applicationUser.setEmail(null);

    assertThrows(InvalidUserInfoException.class, () -> {
      applicationUserService.register(applicationUser);
    });
    verify(applicationUserRepository, times(0)).save(applicationUser);
  }

  @Test
  void findByIdShouldReturnAValidUser() throws Exception {
    when(applicationUserRepository.findById(userId)).thenReturn(Optional.of(applicationUser));
    Optional<ApplicationUser> user = applicationUserService.findById(userId);
    verify(applicationUserRepository, times(1)).findById(userId);
    assertEquals(applicationUser, user.get());
    assertEquals(applicationUser.getId(), user.get().getId());
  }

  @Test
  void loadUserByUsernameShouldReturnAValidUser() throws Exception {
    when(applicationUserRepository.findByEmail(email)).thenReturn(Optional.of(applicationUser));
    UserDetails userDetails = applicationUserService.loadUserByUsername(email);
    verify(applicationUserRepository, times(1)).findByEmail(email);
    assertEquals(applicationUser.getUsername(), userDetails.getUsername());
  }
}
