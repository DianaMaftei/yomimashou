package com.yomimashou.dictionary.word;

import com.yomimashou.appscommon.model.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class WordControllerTest {

  public static final String API_DICTIONARY_WORDS_URL = "/api/dictionary/words";
  private MockMvc mvc;

  @Mock
  private WordService wordService;

  @InjectMocks
  private WordController wordController;

  private Pageable pageable;

  @BeforeEach
  void setup() {
    mvc = MockMvcBuilders.standaloneSetup(wordController)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .build();

    pageable = new PageRequest(0, 10);
  }

  @Test
  void shouldGetAListOfWordEntriesBasedOnASearchItem() throws Exception {
    final Word word = new Word();
    word.setKanjiElements(new HashSet<>(Arrays.asList("猫")));
    final String searchItem = "猫";
    when(wordService.getByReadingElemOrKanjiElem(new String[]{searchItem}, pageable))
        .thenReturn(new PageImpl<>(Collections.singletonList(word), pageable, 1));
    final MockHttpServletResponse response = mvc.perform(
        get(API_DICTIONARY_WORDS_URL)
            .param("searchItem", searchItem)
            .param("page", "0")
            .param("size", "10")
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString().indexOf("kanjiElements\":[\"猫\"]")).isGreaterThan(0);
  }

  @Test
  void shouldThrowExceptionWhenSearchItemIsNull() throws Exception {
    final String searchItem = null;
    final MockHttpServletResponse response = mvc.perform(
        get(API_DICTIONARY_WORDS_URL)
            .param("searchItem", searchItem)
            .param("page", "0")
            .param("size", "1")
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void shouldThrowExceptionWhenSearchItemIsAnEmptyString() throws Exception {
    final MockHttpServletResponse response = mvc.perform(
        get(API_DICTIONARY_WORDS_URL)
            .param("searchItem", "")
            .param("page", "0")
            .param("size", "1")
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }
}
