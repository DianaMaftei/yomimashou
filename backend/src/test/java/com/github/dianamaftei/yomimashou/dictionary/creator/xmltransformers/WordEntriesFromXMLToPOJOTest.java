package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.Entry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.JMdict;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.KEle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.REle;
import com.github.dianamaftei.yomimashou.dictionary.word.Word;
import com.github.dianamaftei.yomimashou.dictionary.word.WordRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WordEntriesFromXMLToPOJOTest {

  @InjectMocks
  private WordEntriesFromXMLToPOJO wordEntriesFromXMLToPOJO;

  @Mock
  private JMdict dictionaryFile;

  @Mock
  private WordRepository wordRepository;

  @Test
  void getEntriesShouldReturnAListOfEntryObjects() {
    List<Entry> entries = wordEntriesFromXMLToPOJO.getEntries(dictionaryFile);
    assertEquals(dictionaryFile.getEntry(), entries);
  }

  @Test
  void getClassForJaxbShouldReturnJMdict() {
    assertEquals(JMdict.class, wordEntriesFromXMLToPOJO.getClassForJaxb());
  }

  @Test
  void saveToFileShouldExtractASetOfDistinctKanjiLiteralsAndReadingsFromEntries() {
    Entry entry = new Entry();
    KEle kanjiElement = new KEle();
    kanjiElement.setKeb("大");
    entry.getKEle().add(kanjiElement);
    REle readingElement = new REle();
    readingElement.setReb("おお.きい");
    entry.getREle().add(readingElement);

    Entry entry2 = new Entry();
    KEle kanjiElement2 = new KEle();
    kanjiElement2.setKeb("国");
    entry2.getKEle().add(kanjiElement2);
    REle readingElement2 = new REle();
    readingElement2.setReb("おお.きい");
    entry2.getREle().add(readingElement2);

    WordEntriesFromXMLToPOJO spyWordEntriesFromXMLToPOJO = spy(wordEntriesFromXMLToPOJO);
    doNothing().when((XMLEntryToPOJO) spyWordEntriesFromXMLToPOJO).writeToFile(any(), any());
    ArgumentCaptor<Set> argument = ArgumentCaptor.forClass(Set.class);
    spyWordEntriesFromXMLToPOJO.saveToFile(Arrays.asList(entry, entry2));

    verify(spyWordEntriesFromXMLToPOJO).writeToFile(argument.capture(), any(String.class));

    assertAll("Should contain the kanji literals and readings from the Dictionary Entries list",
        () -> assertTrue(argument.getValue().contains("大")),
        () -> assertTrue(argument.getValue().contains("おお.きい")),
        () -> assertTrue(argument.getValue().contains("国")),
        () -> assertEquals(3, argument.getValue().size())
    );
  }

  @Test
  void saveToDBShouldAddAllEntriesToTheDatabase() {
    List<Entry> entries = new ArrayList<>();
    entries.add(new Entry());
    entries.add(new Entry());
    wordEntriesFromXMLToPOJO.saveToDb(entries);
    verify(wordRepository, times(2)).save(any(Word.class));
  }

}
