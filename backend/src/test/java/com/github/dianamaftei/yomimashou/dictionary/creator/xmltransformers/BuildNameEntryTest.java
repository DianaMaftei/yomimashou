package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.Entry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.KEle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.NameType;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.REle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.Trans;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.TransDet;
import com.github.dianamaftei.yomimashou.dictionary.name.Name;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BuildNameEntryTest {

  private NameEntriesFromXMLToPOJO nameEntriesFromXMLToPOJO;

  @BeforeEach
  void setUp() {
    nameEntriesFromXMLToPOJO = new NameEntriesFromXMLToPOJO(null);
  }

  @Test
  void buildNameEntryShouldParseKEleCorrectly() {
    Entry entry = new Entry();
    List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
    KEle kEle = new KEle();
    kEle.setKeb("安部浩平");
    entSeqOrKEleOrREleOrTrans.add(kEle);

    Name nameEntry = nameEntriesFromXMLToPOJO.buildNameEntry(entry);

    assertEquals(kEle.getKeb(), nameEntry.getKanji());
  }

  @Test
  void buildNameEntryShouldParseREleCorrectly() {
    Entry entry = new Entry();
    List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
    REle rEle = new REle();
    rEle.setReb("あべこうへい");
    entSeqOrKEleOrREleOrTrans.add(rEle);

    Name nameEntry = nameEntriesFromXMLToPOJO.buildNameEntry(entry);

    assertEquals(rEle.getReb(), nameEntry.getReading());
  }

  @Test
  void buildNameEntryShouldParseTranslationsCorrectly() {
    Entry entry = new Entry();
    List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
    Trans trans = new Trans();
    TransDet transDet = new TransDet();
    transDet.setvalue("Abe Kouhei");
    TransDet transDet2 = new TransDet();
    transDet2.setvalue("translation 2");
    trans.getTransDet().add(transDet);
    trans.getTransDet().add(transDet2);
    entSeqOrKEleOrREleOrTrans.add(trans);

    Name nameEntry = nameEntriesFromXMLToPOJO.buildNameEntry(entry);

    assertEquals(transDet.getvalue().concat("|").concat(transDet2.getvalue()),
        nameEntry.getTranslations());
  }

  @Test
  void buildNameEntryShouldParseTypesCorrectly() {
    Entry entry = new Entry();
    List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
    Trans trans = new Trans();
    NameType nameType = new NameType();
    nameType.setvalue("female given name or forename");
    NameType nameType2 = new NameType();
    nameType2.setvalue("full name of a particular person");
    trans.getNameType().add(nameType);
    trans.getNameType().add(nameType2);
    entSeqOrKEleOrREleOrTrans.add(trans);

    Name nameEntry = nameEntriesFromXMLToPOJO.buildNameEntry(entry);

    assertEquals(nameType.getvalue().concat("|").concat(nameType2.getvalue()), nameEntry.getType());
  }

}
