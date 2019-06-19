package com.github.dianamaftei.yomimashou.dictionary.word;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class WordControllerTest {

  private MockMvc mvc;

  @Mock
  private WordService wordService;

  @InjectMocks
  private WordController wordController;

  @BeforeEach
  void setup() {
    mvc = MockMvcBuilders.standaloneSetup(wordController).build();
  }

  @Test
  void shouldGetAListOfWordEntriesBasedOnASearchItem() throws Exception {
    Word word = new Word();
    word.setKanjiElements("猫");
    String searchItem = "猫";
    when(wordService.get(new String[]{searchItem})).thenReturn(Collections.singletonList(word));
    MockHttpServletResponse response = mvc.perform(
        get("/api/dictionary/words?searchItem={attribute_uri}", "猫")
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString().indexOf("kanjiElements\":\"猫")).isGreaterThan(0);
  }

  @Test
  void shouldReturnAnEmptyListWhenSearchItemIsNull() throws Exception {
    String searchItem = null;
    MockHttpServletResponse response = mvc.perform(
        get("/api/dictionary/words?searchItem={non_existent_variable}", searchItem)
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isEqualTo("[]");
  }

  @Test
  void shouldReturnAnEmptyListWhenSearchItemIsAnEmptyString() throws Exception {
    String searchItem = "";
    MockHttpServletResponse response = mvc.perform(
        get("/api/dictionary/words?searchItem={attribute_uri}", searchItem)
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isEqualTo("[]");
  }
}
