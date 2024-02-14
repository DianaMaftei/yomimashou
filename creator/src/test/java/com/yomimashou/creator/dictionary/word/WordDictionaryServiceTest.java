package com.yomimashou.creator.dictionary.word;

import com.yomimashou.appscommon.model.Word;
import com.yomimashou.creator.dictionary.DictionaryService;
import com.yomimashou.creator.dictionary.word.jmdictXMLmodels.Entry;
import com.yomimashou.creator.dictionary.word.jmdictXMLmodels.JMdict;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WordDictionaryServiceTest {

    @InjectMocks
    @Spy
    private WordDictionaryService wordDictionaryService;

    @Mock
    private JMdict dictionaryFile;
    @Mock
    private WordRepository wordRepository;
    @Mock
    private WordParser wordParser;

    @Captor
    private ArgumentCaptor<List<Word>> argumentCaptor;

    @Test
    void persistShouldAddAllEntriesToTheDatabase() {
        final List<Entry> entries = new ArrayList<>();
        entries.add(new Entry());
        entries.add(new Entry());

        when(wordParser.parse(any())).thenReturn(new Word());
        doNothing().when((DictionaryService) wordDictionaryService).writeToFile(any(), any());

        wordDictionaryService.persist(entries);

        verify(wordRepository, times(1)).saveAll(argumentCaptor.capture());
        assertEquals(2, argumentCaptor.getAllValues().get(0).size());
    }

    @Test
    void getEntriesShouldReturnAListOfEntryObjects() {
        final List<Entry> entries = wordDictionaryService.getEntries(dictionaryFile);
        assertEquals(dictionaryFile.getEntry(), entries);
    }

    @Test
    void getClassForJaxbShouldReturnJMdict() {
        assertEquals(JMdict.class, wordDictionaryService.getClassForJaxb());
    }

}
