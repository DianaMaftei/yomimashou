package com.github.dianamaftei.yomimashou.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationUserServiceTest {
    @InjectMocks
    private ApplicationUserService applicationUserService;

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private ApplicationUser applicationUser;
    private String username = "JohnDemo";
    private Long userId = 1L;

    @Before
    public void setUp() throws Exception {
        applicationUser = new ApplicationUser();
        applicationUser.setId(userId);
        applicationUser.setUsername(username);
        applicationUser.setEmail("test@email.com");
        applicationUser.setPassword("password");
    }

    @Test
    public void registerShouldSaveValidUser() throws Exception {
        when(applicationUserRepository.save(applicationUser)).thenReturn(applicationUser);
        when(passwordEncoder.encode(applicationUser.getPassword())).thenReturn("jibberish");

        applicationUserService.register(applicationUser);

        verify(applicationUserRepository, times(1)).save(applicationUser);
        assertEquals("jibberish", applicationUser.getPassword());
    }

    @Test(expected = InvalidUserInfoException.class)
    public void registerShouldThrowExceptionForInvalidUser() throws Exception {
        applicationUser.setEmail(null);

        applicationUserService.register(applicationUser);

        verify(applicationUserRepository, times(0)).save(applicationUser);
    }

    @Test
    public void findByUsernameShouldReturnAValidUser() throws Exception {
        when(applicationUserRepository.findByUsername(username)).thenReturn(Optional.of(applicationUser));
        Optional<ApplicationUser> user = applicationUserService.findByUsername(username);

        verify(applicationUserRepository, times(1)).findByUsername(username);
        assertEquals(applicationUser, user.get());
    }

    @Test
    public void findByIdShouldReturnAValidUser() throws Exception {
        when(applicationUserRepository.findById(userId)).thenReturn(Optional.of(applicationUser));
        Optional<ApplicationUser> user = applicationUserService.findById(userId);

        verify(applicationUserRepository, times(1)).findById(userId);
        assertEquals(applicationUser, user.get());
        assertEquals(applicationUser.getId(), user.get().getId());
    }

    @Test
    public void loadUserByUsernameShouldReturnAValidUser() throws Exception {
        when(applicationUserRepository.findByUsername(username)).thenReturn(Optional.of(applicationUser));
        UserDetails userDetails = applicationUserService.loadUserByUsername(username);

        verify(applicationUserRepository, times(1)).findByUsername(username);
        assertEquals(applicationUser.getUsername(), userDetails.getUsername());
    }

}