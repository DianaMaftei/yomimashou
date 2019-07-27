package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.Entry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.Gloss;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.KEle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.REle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.Sense;
import com.github.dianamaftei.yomimashou.dictionary.word.Word;
import com.github.dianamaftei.yomimashou.dictionary.word.WordMeaning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BuildWordEntryTest {

  private WordEntriesFromXMLToPOJO wordEntriesFromXMLToPOJO;

  @BeforeEach
  void setUp() {
    wordEntriesFromXMLToPOJO = new WordEntriesFromXMLToPOJO(null);
  }

  @Test
  void buildWordEntryShouldParseKanjiElementsCorrectly() {
    Entry entry = new Entry();
    KEle kanjiElement = new KEle();
    kanjiElement.setKeb("kanji");
    KEle kanjiElement2 = new KEle();
    kanjiElement2.setKeb("kanji2");
    entry.getKEle().add(kanjiElement);
    entry.getKEle().add(kanjiElement2);

    Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);

    assertEquals("kanji|kanji2", word.getKanjiElements());
  }

  @Test
  void buildWordEntryShouldParseReadingElementsCorrectly() {
    Entry entry = new Entry();
    REle readingElement = new REle();
    readingElement.setReb("reading");
    REle readingElement2 = new REle();
    readingElement2.setReb("reading2");
    entry.getREle().add(readingElement);
    entry.getREle().add(readingElement2);

    Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);

    assertEquals("reading|reading2", word.getReadingElements());
  }

  @Test
  void buildWordEntryShouldParseFieldOfApplicationCorrectly() {
    Entry entry = new Entry();
    Sense sense = new Sense();
    sense.getField().add("field of application");
    entry.getSense().add(sense);

    Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);
    WordMeaning wordMeaning = word.getMeanings().get(0);

    assertEquals("field of application", wordMeaning.getFieldOfApplication());
  }

  @Test
  void buildWordEntryShouldParsePartOfSpeechCorrectly() {
    Entry entry = new Entry();
    Sense sense = new Sense();
    sense.getPos().add("Godan verb with 'bu' ending");
    entry.getSense().add(sense);

    Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);
    WordMeaning wordMeaning = word.getMeanings().get(0);

    assertEquals("v5b", wordMeaning.getPartOfSpeech());
  }

  @Test
  void buildWordEntryShouldParseAntonymCorrectly() {
    Entry entry = new Entry();
    Sense sense = new Sense();
    sense.getAnt().add("antonym");
    entry.getSense().add(sense);

    Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);
    WordMeaning wordMeaning = word.getMeanings().get(0);

    assertEquals("antonym", wordMeaning.getAntonym());
  }

  @Test
  void buildWordEntryShouldParseMeaningCorrectly() {
    Entry entry = new Entry();
    Sense sense = new Sense();
    Gloss meaning = new Gloss();
    meaning.getContent().add("meaning");
    sense.getGloss().add(meaning);
    entry.getSense().add(sense);

    Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);
    WordMeaning wordMeaning = word.getMeanings().get(0);

    assertTrue(wordMeaning.getGlosses().contains("meaning"));
  }

  @Test
  void buildWordEntryShouldSetHighPriorityCorrectly() {
    Entry entry = new Entry();
    KEle kanjiElement = new KEle();
    kanjiElement.setKeb("kanji");
    kanjiElement.getKePri().add("news1");
    entry.getKEle().add(kanjiElement);

    Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);

    assertEquals(1, word.getPriority());
  }

  @Test
  void buildWordEntryShouldSetModeratePriorityCorrectly() {
    Entry entry = new Entry();
    KEle kanjiElement = new KEle();
    kanjiElement.setKeb("kanji");
    kanjiElement.getKePri().add("news2");
    entry.getKEle().add(kanjiElement);

    Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);

    assertEquals(2, word.getPriority());
  }

  @Test
  void buildWordEntryShouldSetLowPriorityWhenKEleHasNoPrioritySpecified() {
    Entry entry = new Entry();
    KEle kanjiElement = new KEle();
    kanjiElement.setKeb("kanji");
    entry.getKEle().add(kanjiElement);

    Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);

    assertEquals(3, word.getPriority());
  }

  @Test
  void buildWordEntryShouldSetTheHighestPriorityWhenMultiplePrioritieAreSpecified() {
    Entry entry = new Entry();
    KEle kanjiElement = new KEle();
    kanjiElement.setKeb("kanji");
    kanjiElement.getKePri().add("news2");
    entry.getKEle().add(kanjiElement);
    REle readingElement = new REle();
    readingElement.setReb("reading");
    readingElement.getRePri().add("news1");
    entry.getREle().add(readingElement);

    Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);

    assertEquals(1, word.getPriority());
  }
}
