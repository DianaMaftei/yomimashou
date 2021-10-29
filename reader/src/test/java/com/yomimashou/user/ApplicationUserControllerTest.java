package com.yomimashou.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ApplicationUserControllerTest {

  @InjectMocks
  private ApplicationUserController applicationUserController;

  @Mock
  private ApplicationUserService applicationUserService;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders.standaloneSetup(applicationUserController).build();
  }

  @Test
  void registerReturns200WithValidUser() throws Exception {
    String validUser = "{\"id\":0,\"username\":\"Jane\",\"email\":\"test@email.com\", \"password\":\"password\"}";
    mockMvc.perform(
        post("/api/users/register").content(validUser).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(applicationUserService, times(1)).register(any(ApplicationUser.class));
  }

  @Test
  void registerShouldReturn4xxIfUserHasNoUsername() throws Exception {
    String userWithNoUsername = "{\"id\":0,\"email\":\"test@email.com\", \"password\":\"password\"}";
    MvcResult mvcResult = mockMvc.perform(post("/api/users/register").content(userWithNoUsername)
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
        .andReturn();
    assertEquals(MethodArgumentNotValidException.class,
        mvcResult.getResolvedException().getClass());
  }

  @Test
  void registerShouldReturn4xxIfUserHasNoPassword() throws Exception {
    String userWithNoPassword = "{\"id\":0,\"email\":\"test@email.com\", \"username\":\"Jane\"}";
    MvcResult mvcResult = mockMvc.perform(post("/api/users/register").content(userWithNoPassword)
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
        .andReturn();
    assertEquals(MethodArgumentNotValidException.class,
        mvcResult.getResolvedException().getClass());
  }
}
