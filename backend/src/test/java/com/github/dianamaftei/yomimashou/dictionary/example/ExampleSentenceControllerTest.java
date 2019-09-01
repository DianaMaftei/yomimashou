package com.github.dianamaftei.yomimashou.dictionary.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.github.dianamaftei.appscommon.model.ExampleSentence;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ExampleSentenceControllerTest {

  private MockMvc mvc;

  @Mock
  private ExampleSentenceService exampleSentenceService;

  private ExampleSentenceController exampleSentenceController;

  @BeforeEach
  void setup() {
    exampleSentenceController = new ExampleSentenceController(exampleSentenceService);
    mvc = MockMvcBuilders.standaloneSetup(exampleSentenceController).build();
  }

  @Test
  void shouldGetAListOfExampleSentencesBasedOnASearchItem() throws Exception {
    final ExampleSentence exampleSentence = new ExampleSentence();
    exampleSentence.setSentence("test sentence by search");
    final String searchItem = "test sentence";
    when(exampleSentenceService.get(new String[]{searchItem}))
        .thenReturn(Collections.singletonList(exampleSentence));
    final MockHttpServletResponse response = mvc.perform(
        get("/api/dictionary/examples?searchItem={attribute_uri}", searchItem)
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString().indexOf("sentence\":\"test sentence by search"))
        .isGreaterThan(0);
  }

  @Test
  void shouldReturnAnEmptyListWhenSearchItemIsNull() throws Exception {
    final String searchItem = null;
    final MockHttpServletResponse response = mvc.perform(
        get("/api/dictionary/examples?searchItem={non_existent_variable}", searchItem)
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isEqualTo("[]");
  }

  @Test
  void shouldReturnAnEmptyListWhenSearchItemIsAnEmptyString() throws Exception {
    final String searchItem = "";
    final MockHttpServletResponse response = mvc.perform(
        get("/api/dictionary/examples?searchItem={attribute_uri}", searchItem)
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isEqualTo("[]");
  }
}
