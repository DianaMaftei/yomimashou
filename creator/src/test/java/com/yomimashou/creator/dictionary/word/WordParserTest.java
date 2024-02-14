package com.yomimashou.creator.dictionary.word;

import com.yomimashou.appscommon.model.Word;
import com.yomimashou.appscommon.model.WordMeaning;
import com.yomimashou.creator.dictionary.word.jmdictXMLmodels.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordParserTest {

    private final WordParser wordParser = new WordParser();

    @Test
    void parseShouldExtractKanjiElements() {
        final Entry entry = new Entry();
        final KEle kanjiElement = new KEle();
        kanjiElement.setKeb("kanji");
        final KEle kanjiElement2 = new KEle();
        kanjiElement2.setKeb("kanji2");
        entry.getKEle().add(kanjiElement);
        entry.getKEle().add(kanjiElement2);

        final Word word = wordParser.parse(entry);

        assertEquals("[kanji, kanji2]", word.getKanjiElements().toString());
    }

    @Test
    void parseShouldExtractReadingElements() {
        final Entry entry = new Entry();
        final REle readingElement = new REle();
        readingElement.setReb("reading");
        final REle readingElement2 = new REle();
        readingElement2.setReb("reading2");
        entry.getREle().add(readingElement);
        entry.getREle().add(readingElement2);

        final Word word = wordParser.parse(entry);

        assertEquals("[reading2, reading]", word.getReadingElements().toString());
    }

    @Test
    void parseShouldExtractFieldOfApplication() {
        final Entry entry = new Entry();
        final Sense sense = new Sense();
        sense.getField().add("field of application");
        entry.getSense().add(sense);

        final Word word = wordParser.parse(entry);
        final WordMeaning wordMeaning = word.getMeanings().get(0);

        assertEquals("field of application", wordMeaning.getFieldOfApplication());
    }

    @Test
    void parseShouldExtractPartOfSpeech() {
        final Entry entry = new Entry();
        final Sense sense = new Sense();
        sense.getPos().add("Godan verb with 'bu' ending");
        entry.getSense().add(sense);

        final Word word = wordParser.parse(entry);
        final WordMeaning wordMeaning = word.getMeanings().get(0);

        assertEquals("Godan verb with 'bu' ending", wordMeaning.getPartOfSpeech());
    }

    @Test
    void parseShouldExtractAntonym() {
        final Entry entry = new Entry();
        final Sense sense = new Sense();
        sense.getAnt().add("antonym");
        entry.getSense().add(sense);

        final Word word = wordParser.parse(entry);
        final WordMeaning wordMeaning = word.getMeanings().get(0);

        assertEquals("antonym", wordMeaning.getAntonym());
    }

    @Test
    void parseShouldExtractMeaning() {
        final Entry entry = new Entry();
        final Sense sense = new Sense();
        final Gloss meaning = new Gloss();
        meaning.getContent().add("meaning");
        sense.getGloss().add(meaning);
        entry.getSense().add(sense);

        final Word word = wordParser.parse(entry);
        final WordMeaning wordMeaning = word.getMeanings().get(0);

        assertTrue(wordMeaning.getGlosses().contains("meaning"));
    }

    @Test
    void buildWordEntryShouldSetHighPriority() {
        final Entry entry = new Entry();
        final KEle kanjiElement = new KEle();
        kanjiElement.setKeb("kanji");
        kanjiElement.getKePri().add("news1");
        entry.getKEle().add(kanjiElement);

        final Word word = wordParser.parse(entry);

        assertEquals(1, word.getPriority());
    }

    @Test
    void buildWordEntryShouldSetModeratePriority() {
        final Entry entry = new Entry();
        final KEle kanjiElement = new KEle();
        kanjiElement.setKeb("kanji");
        kanjiElement.getKePri().add("news2");
        entry.getKEle().add(kanjiElement);

        final Word word = wordParser.parse(entry);

        assertEquals(2, word.getPriority());
    }

    @Test
    void buildWordEntryShouldSetLowPriorityWhenKEleHasNoPrioritySpecified() {
        final Entry entry = new Entry();
        final KEle kanjiElement = new KEle();
        kanjiElement.setKeb("kanji");
        entry.getKEle().add(kanjiElement);

        final Word word = wordParser.parse(entry);

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

        final Word word = wordParser.parse(entry);

        assertEquals(1, word.getPriority());
    }
}
