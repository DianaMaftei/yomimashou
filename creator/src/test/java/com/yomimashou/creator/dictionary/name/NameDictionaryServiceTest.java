package com.yomimashou.creator.dictionary.name;

import com.yomimashou.appscommon.model.Name;
import com.yomimashou.creator.dictionary.DictionaryService;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.Entry;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.JMnedict;
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
class NameDictionaryServiceTest {

    @InjectMocks
    @Spy
    private NameDictionaryService nameDictionaryService;

    @Mock
    private JMnedict dictionaryFile;
    @Mock
    private NameRepository nameRepository;
    @Mock
    private NameParser nameParser;

    @Captor
    private ArgumentCaptor<List<Name>> argumentCaptor;

    @Test
    void persistShouldAddAllNamesToTheDB() {
        final List<Entry> entries = new ArrayList<>();
        entries.add(new Entry());
        entries.add(new Entry());
        entries.add(new Entry());
        entries.add(new Entry());

        when(nameParser.parse(any())).thenReturn(new Name());
        doNothing().when((DictionaryService) nameDictionaryService).writeToFile(any(), any());

        nameDictionaryService.persist(entries);

        verify(nameRepository, times(1)).saveAll(argumentCaptor.capture());
        assertEquals(4, argumentCaptor.getAllValues().get(0).size());
    }

    @Test
    void persistShouldFilterOutNullEntries() {
        final List<Entry> entries = new ArrayList<>();
        entries.add(new Entry());
        entries.add(null);
        entries.add(new Entry());

        when(nameParser.parse(any())).thenReturn(new Name());
        doNothing().when((DictionaryService) nameDictionaryService).writeToFile(any(), any());

        nameDictionaryService.persist(entries);

        verify(nameRepository, times(1)).saveAll(argumentCaptor.capture());
        assertEquals(2, argumentCaptor.getAllValues().get(0).size());
    }

    @Test
    void getEntriesShouldReturnAListOfEntryObjects() {
        final List<Entry> entries = nameDictionaryService.getEntries(dictionaryFile);
        assertEquals(dictionaryFile.getEntry(), entries);
    }

    @Test
    void getClassForJaxbShouldReturnJMnedict() {
        assertEquals(JMnedict.class, nameDictionaryService.getClassForJaxb());
    }


}
