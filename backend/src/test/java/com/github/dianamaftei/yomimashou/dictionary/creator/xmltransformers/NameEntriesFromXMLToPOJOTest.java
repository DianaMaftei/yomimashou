package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.Entry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.JMnedict;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.KEle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.REle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.Trans;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.TransDet;
import com.github.dianamaftei.yomimashou.dictionary.name.Name;
import com.github.dianamaftei.yomimashou.dictionary.name.NameRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NameEntriesFromXMLToPOJOTest {

  @InjectMocks
  private NameEntriesFromXMLToPOJO nameEntriesFromXMLToPOJO;

  @Mock
  private JMnedict dictionaryFile;

  @Captor
  private ArgumentCaptor<Name> nameArgumentCaptor;

  @Mock
  private NameRepository nameRepository;

  @Test
  void fillDatabaseShouldAddAllNamesToTheDB() {
    List<Entry> entries = dictionaryFile.getEntry();
    entries.add(new Entry());
    entries.add(new Entry());
    entries.add(new Entry());
    entries.add(new Entry());
    nameEntriesFromXMLToPOJO.saveToDB(entries);
    verify(nameRepository, times(4)).save(any());
  }

  @Test
  void fillDatabaseShouldParseEntryComponentsCorrectly() {
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
  void getEntriesShouldReturnAListOfEntryObjects() {
    List<Entry> entries = nameEntriesFromXMLToPOJO.getEntries(dictionaryFile);
    verify(dictionaryFile, times(1)).getEntry();
    assertEquals(entries, dictionaryFile.getEntry());
  }

  @Test
  void getClassForJaxbShouldReturnJMnedict() {
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
  void saveToFile() {
    // TODO test this method
  }
}
