package com.github.dianamaftei.yomimashou.sentence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
class SentenceControllerTest {

  public static final String API_SENTENCE_URL = "/api/sentence";
  private MockMvc mvc;

  @Mock
  private SentenceService sentenceService;

  @InjectMocks
  private SentenceController sentenceController;

  @BeforeEach
  void setup() {
    mvc = MockMvcBuilders.standaloneSetup(sentenceController).build();
  }

  @Test
  void analyzeShouldCallSentenceServiceWithGivenSentence() throws Exception {
    String sentence = "sentence";
    when(sentenceService.analyze(anyString()))
        .thenReturn(Collections.singletonList(new SentenceToken()));
    mvc.perform(post(API_SENTENCE_URL).content(sentence).accept(MediaType.APPLICATION_JSON))
        .andReturn();
    verify(sentenceService, times(1)).analyze(sentence);
  }

  @Test
  void analyzeShouldReturnBadRequestWhenGivenNoSentence() throws Exception {
    MockHttpServletResponse response = mvc
        .perform(post(API_SENTENCE_URL).accept(MediaType.APPLICATION_JSON)).andReturn()
        .getResponse();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }
}
