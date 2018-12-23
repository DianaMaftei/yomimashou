package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.*;
import com.github.dianamaftei.yomimashou.dictionary.word.Word;
import com.github.dianamaftei.yomimashou.dictionary.word.WordMeaning;
import com.github.dianamaftei.yomimashou.dictionary.word.WordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WordEntriesFromXMLToPOJOTest {

    @InjectMocks
    private WordEntriesFromXMLToPOJO wordEntriesFromXMLToPOJO;

    @Mock
    private JMdict dictionaryFile;

    @Mock
    private WordRepository wordRepository;

    @Captor
    private ArgumentCaptor<Word> wordArgumentCaptor;

    @Test
    public void getEntriesShoulrReturnAListOfEntryObjects() {
        List<Entry> entries = wordEntriesFromXMLToPOJO.getEntries(dictionaryFile);
        verify(dictionaryFile, times(1)).getEntry();
        assertEquals(entries, dictionaryFile.getEntry());
    }

    @Test
    public void getClassForJaxbShouldReturnJMdict() {
        assertEquals(JMdict.class, wordEntriesFromXMLToPOJO.getClassForJaxb());
    }

    @Test
    public void saveToFile() {
        //TODO test this method
    }

    @Test
    public void fillDatabaseShouldAddAllEntriesToTheDatabase() {
        List<Entry> entries = dictionaryFile.getEntry();
        entries.add(new Entry());
        entries.add(new Entry());

        wordEntriesFromXMLToPOJO.saveToDB(entries);

        verify(wordRepository, times(2)).save(any());
    }

    @Test
    public void fillDatabaseShouldParseWordComponentsCorrectly() {
        List<Entry> entries = dictionaryFile.getEntry();
        entries.add(getMockEntry());

        wordEntriesFromXMLToPOJO.saveToDB(entries);

        verify(wordRepository).save(wordArgumentCaptor.capture());
        Word word = wordArgumentCaptor.getValue();
        WordMeaning wordMeaning = word.getMeanings().get(0);

        assertTrue(wordMeaning.getGlosses().contains("meaning"));
        assertEquals("kanji", word.getKanjiElements());
        assertEquals("reading", word.getReadingElements());
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
        entry.getKEle().add(kanjiElement);

        REle readingElement = new REle();
        readingElement.setReb("reading");
        entry.getREle().add(readingElement);

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