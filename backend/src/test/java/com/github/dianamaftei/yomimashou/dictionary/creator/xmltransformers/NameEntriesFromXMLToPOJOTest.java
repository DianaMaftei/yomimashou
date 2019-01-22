package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.*;
import com.github.dianamaftei.yomimashou.dictionary.name.Name;
import com.github.dianamaftei.yomimashou.dictionary.name.NameRepository;
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
public class NameEntriesFromXMLToPOJOTest {
    @InjectMocks
    private NameEntriesFromXMLToPOJO nameEntriesFromXMLToPOJO;

    @Mock
    private JMnedict dictionaryFile;

    @Captor
    private ArgumentCaptor<Name> nameArgumentCaptor;

    @Mock
    private NameRepository nameRepository;

    @Test
    public void fillDatabaseShouldAddAllNamesToTheDB() {
        List<Entry> entries = dictionaryFile.getEntry();
        entries.add(new Entry());
        entries.add(new Entry());
        entries.add(new Entry());
        entries.add(new Entry());

        nameEntriesFromXMLToPOJO.saveToDB(entries);

        verify(nameRepository, times(4)).save(any());
    }

    @Test
    public void fillDatabaseShouldParseEntryComponentsCorrectly() {
        List<Entry> entries = dictionaryFile.getEntry();
        entries.add(getAMockEntry());

        nameEntriesFromXMLToPOJO.saveToDB(entries);

        verify(nameRepository).save(nameArgumentCaptor.capture());
        Name name = nameArgumentCaptor.getValue();

        assertEquals("reading", name.getReading());
        assertEquals("kanji", name.getKanji());
        assertEquals("translation", name.getTranslations());
    }

    @Test
    public void getEntriesShouldReturnAListOfEntryObjects() {
        List<Entry> entries = nameEntriesFromXMLToPOJO.getEntries(dictionaryFile);
        verify(dictionaryFile, times(1)).getEntry();
        assertEquals(entries, dictionaryFile.getEntry());
    }

    @Test
    public void getClassForJaxbShouldReturnJMnedict() {
        assertEquals(JMnedict.class, nameEntriesFromXMLToPOJO.getClassForJaxb());
    }

    private Entry getAMockEntry() {
        Entry entry = new Entry();
        List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
        KEle kEle = new KEle();
        kEle.setKeb("kanji");
        REle rEle = new REle();
        rEle.setReb("reading");
        Trans trans = new Trans();
        TransDet transDet = new TransDet();
        transDet.setvalue("translation");
        trans.getTransDet().add(transDet);

        entSeqOrKEleOrREleOrTrans.add(kEle);
        entSeqOrKEleOrREleOrTrans.add(rEle);
        entSeqOrKEleOrREleOrTrans.add(trans);
        return entry;
    }


    @Test
    public void saveToFile() {
        //TODO test this method
    }
}