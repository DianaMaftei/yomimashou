package com.github.dianamaftei.creator.xmltransformers.word;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.dianamaftei.appscommon.model.Word;
import com.github.dianamaftei.appscommon.model.WordMeaning;
import com.github.dianamaftei.creator.jaxbgeneratedmodels.jmdict.Entry;
import com.github.dianamaftei.creator.jaxbgeneratedmodels.jmdict.Gloss;
import com.github.dianamaftei.creator.jaxbgeneratedmodels.jmdict.KEle;
import com.github.dianamaftei.creator.jaxbgeneratedmodels.jmdict.REle;
import com.github.dianamaftei.creator.jaxbgeneratedmodels.jmdict.Sense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BuildWordEntryTest {

  private WordEntriesFromXMLToPOJO wordEntriesFromXMLToPOJO;

  @BeforeEach
  void setUp() {
    wordEntriesFromXMLToPOJO = new WordEntriesFromXMLToPOJO(null, "", "");
  }

  @Test
  void buildWordEntryShouldParseKanjiElementsCorrectly() {
    final Entry entry = new Entry();
    final KEle kanjiElement = new KEle();
    kanjiElement.setKeb("kanji");
    final KEle kanjiElement2 = new KEle();
    kanjiElement2.setKeb("kanji2");
    entry.getKEle().add(kanjiElement);
    entry.getKEle().add(kanjiElement2);

    final Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);

    assertEquals("[kanji, kanji2]", word.getKanjiElements().toString());
  }

  @Test
  void buildWordEntryShouldParseReadingElementsCorrectly() {
    final Entry entry = new Entry();
    final REle readingElement = new REle();
    readingElement.setReb("reading");
    final REle readingElement2 = new REle();
    readingElement2.setReb("reading2");
    entry.getREle().add(readingElement);
    entry.getREle().add(readingElement2);

    final Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);

    assertEquals("[reading2, reading]", word.getReadingElements().toString());
  }

  @Test
  void buildWordEntryShouldParseFieldOfApplicationCorrectly() {
    final Entry entry = new Entry();
    final Sense sense = new Sense();
    sense.getField().add("field of application");
    entry.getSense().add(sense);

    final Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);
    final WordMeaning wordMeaning = word.getMeanings().get(0);

    assertEquals("field of application", wordMeaning.getFieldOfApplication());
  }

  @Test
  void buildWordEntryShouldParsePartOfSpeechCorrectly() {
    final Entry entry = new Entry();
    final Sense sense = new Sense();
    sense.getPos().add("Godan verb with 'bu' ending");
    entry.getSense().add(sense);

    final Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);
    final WordMeaning wordMeaning = word.getMeanings().get(0);

    assertEquals("v5b", wordMeaning.getPartOfSpeech());
  }

  @Test
  void buildWordEntryShouldParseAntonymCorrectly() {
    final Entry entry = new Entry();
    final Sense sense = new Sense();
    sense.getAnt().add("antonym");
    entry.getSense().add(sense);

    final Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);
    final WordMeaning wordMeaning = word.getMeanings().get(0);

    assertEquals("antonym", wordMeaning.getAntonym());
  }

  @Test
  void buildWordEntryShouldParseMeaningCorrectly() {
    final Entry entry = new Entry();
    final Sense sense = new Sense();
    final Gloss meaning = new Gloss();
    meaning.getContent().add("meaning");
    sense.getGloss().add(meaning);
    entry.getSense().add(sense);

    final Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);
    final WordMeaning wordMeaning = word.getMeanings().get(0);

    assertTrue(wordMeaning.getGlosses().contains("meaning"));
  }

  @Test
  void buildWordEntryShouldSetHighPriorityCorrectly() {
    final Entry entry = new Entry();
    final KEle kanjiElement = new KEle();
    kanjiElement.setKeb("kanji");
    kanjiElement.getKePri().add("news1");
    entry.getKEle().add(kanjiElement);

    final Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);

    assertEquals(1, word.getPriority());
  }

  @Test
  void buildWordEntryShouldSetModeratePriorityCorrectly() {
    final Entry entry = new Entry();
    final KEle kanjiElement = new KEle();
    kanjiElement.setKeb("kanji");
    kanjiElement.getKePri().add("news2");
    entry.getKEle().add(kanjiElement);

    final Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);

    assertEquals(2, word.getPriority());
  }

  @Test
  void buildWordEntryShouldSetLowPriorityWhenKEleHasNoPrioritySpecified() {
    final Entry entry = new Entry();
    final KEle kanjiElement = new KEle();
    kanjiElement.setKeb("kanji");
    entry.getKEle().add(kanjiElement);

    final Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);

    assertEquals(3, word.getPriority());
  }

  @Test
  void buildWordEntryShouldSetTheHighestPriorityWhenMultiplePrioritieAreSpecified() {
    final Entry entry = new Entry();
    final KEle kanjiElement = new KEle();
    kanjiElement.setKeb("kanji");
    kanjiElement.getKePri().add("news2");
    entry.getKEle().add(kanjiElement);
    final REle readingElement = new REle();
    readingElement.setReb("reading");
    readingElement.getRePri().add("news1");
    entry.getREle().add(readingElement);

    final Word word = wordEntriesFromXMLToPOJO.buildWordEntry(entry);

    assertEquals(1, word.getPriority());
  }
}
