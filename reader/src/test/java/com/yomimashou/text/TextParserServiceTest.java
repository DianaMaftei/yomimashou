package com.yomimashou.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.yomimashou.text.dictionary.Deinflector;
import com.yomimashou.text.dictionary.NameDictionary;
import com.yomimashou.text.dictionary.WordDictionary;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TextParserServiceTest {

  @Mock
  private Deinflector deinflector;

  @Mock
  private WordDictionary wordDictionary;

  @Mock
  private NameDictionary nameDictionary;

  @InjectMocks
  private TextParserService textParserService;

  @Test
  void parseWordsReturnsAllWordsFromTextThatAreInDictionary() {
    when(deinflector.getDeinflectedWords(anyString()))
        .thenReturn(Collections.emptySet());

    when(wordDictionary.isWordInDictionary(anyString())).thenReturn(true);

    Set<String> parsedWords = textParserService.parseWords("欲張りな");

    //欲, 欲張, 欲張り, 欲張りな, 張, 張り, 張りな, り, りな, な
    assertEquals(10, parsedWords.size());
  }

  @Test
  void parseWordsDoesNotReturnsWordsFromTextThatAreNotInDictionary() {
    when(deinflector.getDeinflectedWords(anyString()))
        .thenReturn(Collections.emptySet());

    when(wordDictionary.isWordInDictionary(anyString()))
        .thenReturn(false, true, false, true);

    Set<String> parsedWords = textParserService.parseWords("欲張りな");

    //欲張, 欲張りな, 張, 張り, 張りな, り, りな, な
    assertEquals(8, parsedWords.size());
  }

  @TestFactory
  Stream<DynamicTest> parseWordsReturnsWordsUntilEndingCharacter() {
    when(deinflector.getDeinflectedWords(anyString()))
        .thenReturn(Collections.emptySet());

    when(wordDictionary.isWordInDictionary(anyString())).thenReturn(true);

    EndingCharacter[] endingCharacters = EndingCharacter.values();

    return Arrays.stream(endingCharacters).map(endingCharacter -> DynamicTest.dynamicTest(
        "Test ending character: " + endingCharacter, () -> {
          String textWithEndingCharacter = String.format("欲張り%sな", endingCharacter);
          Set<String> parsedWords = textParserService.parseWords(textWithEndingCharacter);

          //欲, 欲張, 張, 欲張り, 張り, り, //欲, 欲張, 張, 欲張り, 張り, り, な
          assertEquals(7, parsedWords.size());
        }));
  }

  @Test
  void parseNamesReturnsAllNamesFromTextThatAreInDictionary() {
    when(nameDictionary.isNameInDictionary(anyString())).thenReturn(true);

    Set<String> parsedNames = textParserService.parseNames("欲張りな");

    //欲, 欲張, 欲張り, 欲張りな, 張, 張り, 張りな, り, りな, な
    assertEquals(10, parsedNames.size());
  }

  @Test
  void parseNamesDoesNotReturnsNamesFromTextThatAreNotInDictionary() {
    when(nameDictionary.isNameInDictionary(anyString()))
        .thenReturn(false, true, false, true);

    Set<String> parsedNames = textParserService.parseNames("欲張りな");

    //欲張, 欲張りな, 張, 張り, 張りな, り, りな, な
    assertEquals(8, parsedNames.size());
  }

  @TestFactory
  Stream<DynamicTest> parseNamesReturnsNamesUntilEndingCharacter() {
    when(nameDictionary.isNameInDictionary(anyString())).thenReturn(true);

    EndingCharacter[] endingCharacters = EndingCharacter.values();

    return Arrays.stream(endingCharacters).map(endingCharacter -> DynamicTest.dynamicTest(
        "Test ending character: " + endingCharacter, () -> {
          String textWithEndingCharacter = String.format("欲張り%sな", endingCharacter);
          Set<String> parsedNames = textParserService.parseNames(textWithEndingCharacter);

          //欲, 欲張, 張, 欲張り, 張り, り, な
          assertEquals(7, parsedNames.size());
        }));
  }
}
