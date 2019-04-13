package com.github.dianamaftei.yomimashou.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

@WebMvcTest(ApplicationUserController.class)
@RunWith(MockitoJUnitRunner.class)
public class ApplicationUserControllerTest {

  @InjectMocks
  private ApplicationUserController applicationUserController;

  @Mock
  private ApplicationUserService applicationUserService;

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders.standaloneSetup(applicationUserController).build();
  }

  @Test
  public void registerReturns200WithValidUser() throws Exception {
    String validUser = "{\"id\":0,\"username\":\"Jane\",\"email\":\"test@email.com\", \"password\":\"password\"}";

    mockMvc.perform(
        post("/api/users/register")
            .content(validUser)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(applicationUserService, times(1)).register(any(ApplicationUser.class));
  }

  @Test
  public void registerShouldReturn4xxIfUserHasNoUsername() throws Exception {
    String userWithNoUsername = "{\"id\":0,\"email\":\"test@email.com\", \"password\":\"password\"}";

    MvcResult mvcResult = mockMvc.perform(
        post("/api/users/register")
            .content(userWithNoUsername)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError()).andReturn();

    assertEquals(MethodArgumentNotValidException.class,
        mvcResult.getResolvedException().getClass());
  }

  @Test
  public void registerShouldReturn4xxIfUserHasNoPassword() throws Exception {
    String userWithNoPassword = "{\"id\":0,\"email\":\"test@email.com\", \"username\":\"Jane\"}";

    MvcResult mvcResult = mockMvc.perform(
        post("/api/users/register")
            .content(userWithNoPassword)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError()).andReturn();

    assertEquals(MethodArgumentNotValidException.class,
        mvcResult.getResolvedException().getClass());
  }
}