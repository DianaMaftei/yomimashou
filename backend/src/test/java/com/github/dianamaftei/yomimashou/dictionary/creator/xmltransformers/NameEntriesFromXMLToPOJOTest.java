package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.Entry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.JMnedict;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.KEle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.REle;
import com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.namecomponents.NameComponent;
import com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.namecomponents.NameKanjiComponent;
import com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.namecomponents.NameReadingComponent;
import com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.namecomponents.NameTranslationComponent;
import com.github.dianamaftei.yomimashou.dictionary.name.Name;
import com.github.dianamaftei.yomimashou.dictionary.name.NameRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
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

  @Mock
  private NameRepository nameRepository;

  @Captor
  private ArgumentCaptor<List<Name>> argumentCaptor;

  @BeforeEach
  void setUp() {
    final List<NameComponent> nameComponentsEnrichers = new ArrayList<>();
    nameComponentsEnrichers.add(new NameReadingComponent());
    nameComponentsEnrichers.add(new NameKanjiComponent());
    nameComponentsEnrichers.add(new NameTranslationComponent());
    nameEntriesFromXMLToPOJO.setNameComponentsEnrichers(nameComponentsEnrichers);
  }

  @Test
  void persistShouldAddAllNamesToTheDB() {
    final List<Entry> entries = new ArrayList<>();
    entries.add(new Entry());
    entries.add(new Entry());
    entries.add(new Entry());
    entries.add(new Entry());

    final NameEntriesFromXMLToPOJO spyNameEntriesFromXMLToPOJO = spy(nameEntriesFromXMLToPOJO);
    doNothing().when((XMLEntryToPOJO) spyNameEntriesFromXMLToPOJO).writeToFile(any(), any());

    spyNameEntriesFromXMLToPOJO.persist(entries);

    verify(nameRepository, times(1)).saveAll(argumentCaptor.capture());
    assertEquals(4, argumentCaptor.getAllValues().get(0).size());
  }

  @Test
  void persistShouldFilterOutNullEntries() {
    final List<Entry> entries = new ArrayList<>();
    entries.add(new Entry());
    entries.add(null);
    entries.add(new Entry());

    final NameEntriesFromXMLToPOJO spyNameEntriesFromXMLToPOJO = spy(nameEntriesFromXMLToPOJO);
    doNothing().when((XMLEntryToPOJO) spyNameEntriesFromXMLToPOJO).writeToFile(any(), any());

    spyNameEntriesFromXMLToPOJO.persist(entries);

    verify(nameRepository, times(1)).saveAll(argumentCaptor.capture());
    assertEquals(2, argumentCaptor.getAllValues().get(0).size());

  }

  @Test
  void getEntriesShouldReturnAListOfEntryObjects() {
    final List<Entry> entries = nameEntriesFromXMLToPOJO.getEntries(dictionaryFile);
    assertEquals(dictionaryFile.getEntry(), entries);
  }

  @Test
  void getClassForJaxbShouldReturnJMnedict() {
    assertEquals(JMnedict.class, nameEntriesFromXMLToPOJO.getClassForJaxb());
  }

  @Test
  void persistShouldParseAllKanjiAndReadingsOfTheNameEntriesBeforeCallingWriteToFile() {
    final List<Entry> entries = new ArrayList<>();
    final Entry entry = new Entry();
    final List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
    final KEle kEle = new KEle();
    kEle.setKeb("大");
    entSeqOrKEleOrREleOrTrans.add(kEle);
    final REle rEle = new REle();
    rEle.setReb("あべこうへい");
    entSeqOrKEleOrREleOrTrans.add(rEle);
    entries.add(entry);

    final NameEntriesFromXMLToPOJO spyNameEntriesFromXMLToPOJO = spy(nameEntriesFromXMLToPOJO);
    doNothing().when((XMLEntryToPOJO) spyNameEntriesFromXMLToPOJO).writeToFile(any(), any());

    final ArgumentCaptor<Set> argument = ArgumentCaptor.forClass(Set.class);
    spyNameEntriesFromXMLToPOJO.persist(entries);

    verify(spyNameEntriesFromXMLToPOJO).writeToFile(argument.capture(), isNull());

    assertAll("Should contain the kanji and reading of the name",
        () -> assertTrue(argument.getValue().contains("大")),
        () -> assertTrue(argument.getValue().contains("あべこうへい")),
        () -> assertEquals(2, argument.getValue().size())
    );
  }
}