package com.github.dianamaftei.yomimashou.dictionary.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class ExampleSentenceControllerTest {

  private MockMvc mvc;

  @Mock
  private ExampleSentenceService exampleSentenceService;

  private ExampleSentenceController exampleSentenceController;

  @Before
  public void setup() {
    exampleSentenceController = new ExampleSentenceController(exampleSentenceService);
    mvc = MockMvcBuilders.standaloneSetup(exampleSentenceController)
        .build();
  }

  @Test
  public void shouldGetAListOfExampleSentencesBasedOnASearchItem() throws Exception {
    ExampleSentence exampleSentence = new ExampleSentence();
    exampleSentence.setSentence("test sentence by search");
    String searchItem = "test sentence";
    when(exampleSentenceService.get(new String[]{searchItem}))
        .thenReturn(Collections.singletonList(exampleSentence));

    MockHttpServletResponse response = mvc
        .perform(get("/api/dictionary/examples?searchItem={attribute_uri}", searchItem)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString().indexOf("sentence\":\"test sentence by search"))
        .isGreaterThan(0);
  }

  @Test
  public void shouldReturnAnEmptyListWhenSearchItemIsNull() throws Exception {
    String searchItem = null;

    MockHttpServletResponse response = mvc
        .perform(get("/api/dictionary/examples?searchItem={non_existent_variable}", searchItem)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isEqualTo("[]");
  }

  @Test
  public void shouldReturnAnEmptyListWhenSearchItemIsAnEmptyString() throws Exception {
    String searchItem = "";

    MockHttpServletResponse response = mvc
        .perform(get("/api/dictionary/examples?searchItem={attribute_uri}", searchItem)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isEqualTo("[]");
  }
}