package com.yomimashou.text.dictionary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeinflectorTest {

  @InjectMocks
  private Deinflector deinflector;

  @BeforeEach
  void setUp() {
    deinflector.setReader(new StringReader(
        "た\tる\n" + "ない\tる\n" + "い\tいる\n" + "い\tう\n" + "い\tる\n" + "え\tう\n" + "え\tえる\n" + "な"));
  }

  @Test
  void getDeinflectedWordsForTaEndingShouldReturnSingleResult() {
    assertEquals(Collections.singleton("夢を見る"), deinflector.getDeinflectedWords("夢を見た"));
  }

  @Test
  void getDeinflectedWordsForEnaiEndingShouldReturnMultipleResults() {
    Set<String> deinflectedWords = Stream.of("見える", "見えなう", "見う", "見えないる", "見え", "見えなる")
        .collect(Collectors.toSet());
    assertEquals(deinflectedWords, deinflector.getDeinflectedWords("見えない"));
  }

  @Test
  void getDeinflectedWordsForYouAwaEndingShouldReturnNoResults() {
    assertEquals(Collections.emptySet(), deinflector.getDeinflectedWords("見あわ"));
  }
}
