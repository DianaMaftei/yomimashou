package com.github.dianamaftei.creator.examplesentence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.dianamaftei.appscommon.model.ExampleSentence;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExampleSentencesCSVtoPOJOTest {

  @InjectMocks
  private ExampleSentencesCSVtoPOJO exampleSentencesCSVtoPOJO;

  @Mock
  private ExampleSentenceRepository exampleSentenceRepository;

  @Captor
  private ArgumentCaptor<Set<ExampleSentence>> argumentCaptor;

  @Test
  void saveSentencesToDBShouldSaveAllExampleSentences() {
    final List<String> sentenceLines = Arrays.asList("3400993\tjpn\tこれ、可愛いな。\tThis is cute.",
        "187088\tjpn\t家に帰る時間を知らせてくれ。\tLet me know when you'll return home.\t家(いえ)[01] に 帰る[01] 時間 を 知らせる{知らせて} 呉れる{くれ}");

    exampleSentencesCSVtoPOJO.saveSentencesToDB(sentenceLines);

    verify(exampleSentenceRepository, times(1)).saveAll(argumentCaptor.capture());

    assertEquals(2, argumentCaptor.getAllValues().get(0).size());
  }

  @Test
  void saveSentencesToDBShouldParseTheSentencesCorrectly() {
    final List<String> sentenceLines = Arrays.asList("3400993\tjpn\tこれ、可愛いな。\tThis is cute.",
        "187088\tjpn\t家に帰る時間を知らせてくれ。\tLet me know when you'll return home.\t家(いえ)[01] に 帰る[01] 時間 を 知らせる{知らせて} 呉れる{くれ}");

    exampleSentencesCSVtoPOJO.saveSentencesToDB(sentenceLines);

    verify(exampleSentenceRepository, times(1)).saveAll(argumentCaptor.capture());
    final List<ExampleSentence> exampleSentences = argumentCaptor.getAllValues().stream()
        .flatMap(Set::stream)
        .sorted(Comparator.comparing(ExampleSentence::getSentence))
        .collect(Collectors.toList());

    assertAll("Should extract the first sentence, meaning and breakdown from the text",
        () -> assertEquals("これ、可愛いな。", exampleSentences.get(0).getSentence()),
        () -> assertEquals("This is cute.", exampleSentences.get(0).getMeaning()),
        () -> assertNull(exampleSentences.get(0).getTextBreakdown())
    );

    assertAll("Should extract the second sentence, meaning and breakdown from the text",
        () -> assertEquals("家に帰る時間を知らせてくれ。", exampleSentences.get(1).getSentence()),
        () -> assertEquals("Let me know when you'll return home.",
            exampleSentences.get(1).getMeaning()),
        () -> assertEquals("家(いえ)[01] に 帰る[01] 時間 を 知らせる{知らせて} 呉れる{くれ}",
            exampleSentences.get(1).getTextBreakdown())
    );
  }
}
