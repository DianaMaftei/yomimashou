package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.Entry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.Gloss;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.JMdict;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.KEle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.REle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.Sense;
import com.github.dianamaftei.yomimashou.dictionary.word.Word;
import com.github.dianamaftei.yomimashou.dictionary.word.WordMeaning;
import com.github.dianamaftei.yomimashou.dictionary.word.WordRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

  @Captor
  private ArgumentCaptor<Word> wordArgumentCaptor;

  @Test
  void getEntriesShouldReturnAListOfEntryObjects() {
    List<Entry> entries = wordEntriesFromXMLToPOJO.getEntries(dictionaryFile);
    verify(dictionaryFile, times(1)).getEntry();
    assertEquals(entries, dictionaryFile.getEntry());
  }

  @Test
  void getClassForJaxbShouldReturnJMdict() {
    assertEquals(JMdict.class, wordEntriesFromXMLToPOJO.getClassForJaxb());
  }

  @Test
  void saveToFile() {
    // TODO test this method
  }

  @Test
  void saveToDBShouldAddAllEntriesToTheDatabase() {
    List<Entry> entries = dictionaryFile.getEntry();
    entries.add(new Entry());
    entries.add(new Entry());
    wordEntriesFromXMLToPOJO.saveToDB(entries);
    verify(wordRepository, times(2)).save(any());
  }

  @Test
  void fillDatabaseShouldParseWordComponentsCorrectly() {
    List<Entry> entries = dictionaryFile.getEntry();
    entries.add(getMockEntry());
    wordEntriesFromXMLToPOJO.saveToDB(entries);
    verify(wordRepository).save(wordArgumentCaptor.capture());
    Word word = wordArgumentCaptor.getValue();
    WordMeaning wordMeaning = word.getMeanings().get(0);
    assertTrue(wordMeaning.getGlosses().contains("meaning"));
    assertEquals("kanji|kanji2", word.getKanjiElements());
    assertEquals("reading|reading2", word.getReadingElements());
    assertEquals("field of application", wordMeaning.getFieldOfApplication());
    assertEquals("iv", wordMeaning.getPartOfSpeech());
    assertEquals("antonym", wordMeaning.getAntonym());
    assertEquals(1, word.getPriority());
  }

  private Entry getMockEntry() {
    Entry entry = new Entry();
    KEle kanjiElement = new KEle();
    kanjiElement.setKeb("kanji");
    kanjiElement.getKePri().add("news1");
    KEle kanjiElement2 = new KEle();
    kanjiElement2.setKeb("kanji2");
    kanjiElement2.getKePri().add("news1");
    entry.getKEle().add(kanjiElement);
    entry.getKEle().add(kanjiElement2);
    REle readingElement = new REle();
    readingElement.setReb("reading");
    REle readingElement2 = new REle();
    readingElement2.setReb("reading2");
    entry.getREle().add(readingElement);
    entry.getREle().add(readingElement2);
    Sense sense = new Sense();
    Gloss meaning = new Gloss();
    meaning.getContent().add("meaning");
    sense.getGloss().add(meaning);
    sense.getPos().add("irregular verb");
    sense.getField().add("field of application");
    sense.getAnt().add("antonym");
    entry.getSense().add(sense);
    return entry;
  }
}
