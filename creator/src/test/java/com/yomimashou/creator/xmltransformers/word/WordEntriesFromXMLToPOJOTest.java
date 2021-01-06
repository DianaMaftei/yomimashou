package com.yomimashou.creator.xmltransformers.word;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.yomimashou.appscommon.model.Word;
import com.yomimashou.creator.jaxbgeneratedmodels.jmdict.Entry;
import com.yomimashou.creator.jaxbgeneratedmodels.jmdict.JMdict;
import com.yomimashou.creator.jaxbgeneratedmodels.jmdict.KEle;
import com.yomimashou.creator.jaxbgeneratedmodels.jmdict.REle;
import com.yomimashou.creator.xmltransformers.XMLEntryToPOJO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WordEntriesFromXMLToPOJOTest {

  @InjectMocks
  private WordEntriesFromXMLToPOJO wordEntriesFromXMLToPOJO;

  @Mock
  private JMdict dictionaryFile;

  @Mock
  private WordRepository wordRepository;

  @Captor
  private ArgumentCaptor<List<Word>> argumentCaptor;

  @Test
  void getEntriesShouldReturnAListOfEntryObjects() {
    final List<Entry> entries = wordEntriesFromXMLToPOJO.getEntries(dictionaryFile);
    assertEquals(dictionaryFile.getEntry(), entries);
  }

  @Test
  void getClassForJaxbShouldReturnJMdict() {
    assertEquals(JMdict.class, wordEntriesFromXMLToPOJO.getClassForJaxb());
  }

  @Test
  void persistShouldExtractASetOfDistinctKanjiLiteralsAndReadingsFromEntriesBeforeCallingWriteToFile() {
    final Entry entry = new Entry();
    final KEle kanjiElement = new KEle();
    kanjiElement.setKeb("大");
    entry.getKEle().add(kanjiElement);
    final REle readingElement = new REle();
    readingElement.setReb("おお.きい");
    entry.getREle().add(readingElement);

    final Entry entry2 = new Entry();
    final KEle kanjiElement2 = new KEle();
    kanjiElement2.setKeb("国");
    entry2.getKEle().add(kanjiElement2);
    final REle readingElement2 = new REle();
    readingElement2.setReb("おお.きい");
    entry2.getREle().add(readingElement2);

    final WordEntriesFromXMLToPOJO spyWordEntriesFromXMLToPOJO = Mockito.spy(wordEntriesFromXMLToPOJO);
    doNothing().when((XMLEntryToPOJO) spyWordEntriesFromXMLToPOJO).writeToFile(any(), any());
    final ArgumentCaptor<Set> argument = ArgumentCaptor.forClass(Set.class);
    spyWordEntriesFromXMLToPOJO.persist(Arrays.asList(entry, entry2));

    verify(spyWordEntriesFromXMLToPOJO).writeToFile(argument.capture(), isNull());

    assertAll("Should contain the kanji literals and readings from the Dictionary Entries list",
        () -> assertTrue(argument.getValue().contains("大")),
        () -> assertTrue(argument.getValue().contains("おお.きい")),
        () -> assertTrue(argument.getValue().contains("国")),
        () -> assertEquals(3, argument.getValue().size())
    );
  }

  @Test
  void persistShouldAddAllEntriesToTheDatabase() {
    final List<Entry> entries = new ArrayList<>();
    entries.add(new Entry());
    entries.add(new Entry());

    final WordEntriesFromXMLToPOJO spyWordEntriesFromXMLToPOJO = Mockito.spy(wordEntriesFromXMLToPOJO);
    doNothing().when((XMLEntryToPOJO) spyWordEntriesFromXMLToPOJO).writeToFile(any(), any());

    spyWordEntriesFromXMLToPOJO.persist(entries);

    verify(wordRepository, times(1)).saveAll(argumentCaptor.capture());
    assertEquals(2, argumentCaptor.getAllValues().get(0).size());
  }

}
