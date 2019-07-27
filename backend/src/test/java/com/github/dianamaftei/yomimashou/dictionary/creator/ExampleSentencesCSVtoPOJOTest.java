package com.github.dianamaftei.yomimashou.dictionary.creator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dianamaftei.yomimashou.dictionary.example.ExampleSentence;
import com.github.dianamaftei.yomimashou.dictionary.example.ExampleSentenceRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

@ExtendWith(MockitoExtension.class)
class ExampleSentencesCSVtoPOJOTest {

  @InjectMocks
  private ExampleSentencesCSVtoPOJO exampleSentencesCSVtoPOJO;

  @Mock
  private ExampleSentenceRepository exampleSentenceRepository;

  @Mock
  private ClassPathResource classPathResource;

  @Captor
  private ArgumentCaptor<ExampleSentence> argumentCaptor;

  @Test
  void saveSentencesFromFileToDBShouldSaveAllExampleSentences() throws IOException {
    exampleSentencesCSVtoPOJO.setResource(classPathResource);
    when(classPathResource.getInputStream()).thenReturn(new ByteArrayInputStream(
        ("3400993\tjpn\tこれ、可愛いな。\tThis is cute.\n"
            + "1204364\tjpn\t明日の午前２時４０分てそれ１回目？それとも２回目？\tIs it the first time at 2:40AM tomorrow? Or is it the second time?")
            .getBytes()));

    exampleSentencesCSVtoPOJO.saveSentencesFromFileToDB();

    verify(exampleSentenceRepository, times(2)).save(any());
  }

  @Test
  void saveSentencesFromFileToDBShouldParseTheSentencesCorrectly() throws IOException {
    exampleSentencesCSVtoPOJO.setResource(classPathResource);
    when(classPathResource.getInputStream())
        .thenReturn(new ByteArrayInputStream("3400993\tjpn\tこれ、可愛いな。\tThis is cute.".getBytes()));

    exampleSentencesCSVtoPOJO.saveSentencesFromFileToDB();

    verify(exampleSentenceRepository).save(argumentCaptor.capture());
    ExampleSentence exampleSentence = argumentCaptor.getValue();

    assertAll("Should extract the sentence, meaning and breakdown from the text",
        () -> assertEquals("これ、可愛いな。", exampleSentence.getSentence()),
        () -> assertEquals("This is cute.", exampleSentence.getMeaning()),
        () -> assertNull(exampleSentence.getTextBreakdown())
    );
  }

  @Test
  void saveSentencesFromFileToDBShouldNotSaveAnythingToTheDBIfReadingTheFileThrowsAnError()
      throws IOException {
    exampleSentencesCSVtoPOJO.setResource(classPathResource);
    when(classPathResource.getInputStream()).thenThrow(new IOException());

    exampleSentencesCSVtoPOJO.saveSentencesFromFileToDB();

    verify(exampleSentenceRepository, times(0)).save(any());
  }
}
