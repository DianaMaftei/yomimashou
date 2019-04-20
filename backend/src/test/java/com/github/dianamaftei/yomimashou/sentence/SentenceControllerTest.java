package com.github.dianamaftei.yomimashou.sentence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class SentenceControllerTest {

  private MockMvc mvc;

  @Mock
  private SentenceService sentenceService;

  @InjectMocks
  private SentenceController sentenceController;

  @Before
  public void setup() {
    mvc = MockMvcBuilders.standaloneSetup(sentenceController).build();
  }

  @Test
  public void analyzeShouldCallSentenceServiceWithGivenSentence() throws Exception {
    String sentence = "sentence";
    when(sentenceService.analyze(anyString()))
        .thenReturn(Collections.singletonList(new SentenceToken()));

    MockHttpServletResponse response = mvc.perform(post("/api/sentence")
        .content(sentence)
        .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    verify(sentenceService, times(1)).analyze(sentence);
  }

  @Test
  public void analyzeShouldReturnBadRequestWhenGivenNoSentence() throws Exception {
    MockHttpServletResponse response = mvc.perform(post("/api/sentence")
        .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }
}