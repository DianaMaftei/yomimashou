package com.github.dianamaftei.yomimashou.sentence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.atilika.kuromoji.jumandic.Token;
import com.atilika.kuromoji.jumandic.Tokenizer;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@ExtendWith(MockitoExtension.class)
class SentenceServiceTest {

  private static SentenceService sentenceService;

  private static Resource resource;

  @Mock
  private static Tokenizer tokenizer;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    ResourceLoader resourceLoader = mock(ResourceLoader.class);
    resource = mock(Resource.class);
    when(resourceLoader.getResource(anyString())).thenReturn(resource);
    when(resource.getInputStream()).thenReturn(new ByteArrayInputStream("abcd".getBytes()));
    sentenceService = new SentenceService(resourceLoader);
  }

  @BeforeEach
  void setUp() {
    sentenceService.setTokenizer(tokenizer);
  }

  @Test
  void analyzeShouldReturnAnEmptyListForEmptySentence() {
    when(tokenizer.tokenize(anyString())).thenReturn(new ArrayList<>());
    List<SentenceToken> tokens = sentenceService.analyze("");
    assertEquals(0, tokens.size());
  }

  @Test
  void analyzeShouldReturnASentenceTokenForASingleWordSentence() {
    Token token = mock(Token.class);
    when(tokenizer.tokenize(anyString())).thenReturn(Collections.singletonList(token));
    List<SentenceToken> tokens = sentenceService.analyze("word");
    assertEquals(1, tokens.size());
  }

  @Test
  void analyzeShouldPopulateTheSentenceTokenWithTheBaseForm() {
    Token token = mock(Token.class);
    String baseForm = "base form";
    when(token.getBaseForm()).thenReturn(baseForm);
    when(tokenizer.tokenize(anyString())).thenReturn(Collections.singletonList(token));
    List<SentenceToken> tokens = sentenceService.analyze("word");
    SentenceToken sentenceToken = tokens.get(0);
    assertEquals(baseForm, sentenceToken.getBaseForm());
  }

  @Test
  void analyzeShouldPopulateTheSentenceTokenWithTheSurfaceForm() {
    Token token = mock(Token.class);
    String surface = "surface";
    when(token.getSurface()).thenReturn(surface);
    when(tokenizer.tokenize(anyString())).thenReturn(Collections.singletonList(token));
    List<SentenceToken> tokens = sentenceService.analyze("word");
    SentenceToken sentenceToken = tokens.get(0);
    assertEquals(surface, sentenceToken.getSurface());
  }

  @Test
  void analyzeShouldPopulateTheSentenceTokenWithTheReadingForm() {
    Token token = mock(Token.class);
    String reading = "reading";
    when(token.getReading()).thenReturn(reading);
    when(tokenizer.tokenize(anyString())).thenReturn(Collections.singletonList(token));
    List<SentenceToken> tokens = sentenceService.analyze("word");
    SentenceToken sentenceToken = tokens.get(0);
    assertEquals(reading, sentenceToken.getReading());
  }

  @Test
  void analyzeShouldPopulateTheSentenceTokenWithTheSemanticInfo() {
    Token token = mock(Token.class);
    String semanticInfo = "semantic info";
    when(token.getSemanticInformation()).thenReturn(semanticInfo);
    when(tokenizer.tokenize(anyString())).thenReturn(Collections.singletonList(token));
    List<SentenceToken> tokens = sentenceService.analyze("word");
    SentenceToken sentenceToken = tokens.get(0);
    assertEquals(semanticInfo, sentenceToken.getSemanticInformation());
  }
}
