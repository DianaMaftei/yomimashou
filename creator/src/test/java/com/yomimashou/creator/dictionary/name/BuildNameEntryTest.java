package com.yomimashou.creator.dictionary.name;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.yomimashou.appscommon.model.Name;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.Entry;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.KEle;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.NameType;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.REle;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.Trans;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.TransDet;
import com.yomimashou.creator.dictionary.name.namecomponents.NameComponent;
import com.yomimashou.creator.dictionary.name.namecomponents.NameKanjiComponent;
import com.yomimashou.creator.dictionary.name.namecomponents.NameReadingComponent;
import com.yomimashou.creator.dictionary.name.namecomponents.NameTranslationComponent;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BuildNameEntryTest {

  private NameEntriesFromXMLToPOJO nameEntriesFromXMLToPOJO;

  @BeforeEach
  void setUp() {
    final List<NameComponent> nameComponentsEnrichers = new ArrayList<>();
    nameComponentsEnrichers.add(new NameKanjiComponent());
    nameComponentsEnrichers.add(new NameReadingComponent());
    nameComponentsEnrichers.add(new NameTranslationComponent());

    nameEntriesFromXMLToPOJO = new NameEntriesFromXMLToPOJO(null, "", "", nameComponentsEnrichers);
  }

  @Test
  void buildNameEntryShouldParseKEleCorrectly() {
    final Entry entry = new Entry();
    final List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
    final KEle kEle = new KEle();
    kEle.setKeb("安部浩平");
    entSeqOrKEleOrREleOrTrans.add(kEle);

    final Name nameEntry = nameEntriesFromXMLToPOJO.buildNameEntry(entry);

    assertEquals(kEle.getKeb(), nameEntry.getKanji());
  }

  @Test
  void buildNameEntryShouldParseREleCorrectly() {
    final Entry entry = new Entry();
    final List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
    final REle rEle = new REle();
    rEle.setReb("あべこうへい");
    entSeqOrKEleOrREleOrTrans.add(rEle);

    final Name nameEntry = nameEntriesFromXMLToPOJO.buildNameEntry(entry);

    assertEquals(rEle.getReb(), nameEntry.getReading());
  }

  @Test
  void buildNameEntryShouldParseTranslationsCorrectly() {
    final Entry entry = new Entry();
    final List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
    final Trans trans = new Trans();
    final TransDet transDet = new TransDet();
    transDet.setvalue("Abe Kouhei");
    final TransDet transDet2 = new TransDet();
    transDet2.setvalue("translation 2");
    trans.getTransDet().add(transDet);
    trans.getTransDet().add(transDet2);
    entSeqOrKEleOrREleOrTrans.add(trans);

    final Name nameEntry = nameEntriesFromXMLToPOJO.buildNameEntry(entry);

    assertEquals(transDet.getvalue().concat("|").concat(transDet2.getvalue()),
        nameEntry.getTranslations());
  }

  @Test
  void buildNameEntryShouldParseTypesCorrectly() {
    final Entry entry = new Entry();
    final List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
    final Trans trans = new Trans();
    final NameType nameType = new NameType();
    nameType.setvalue("female given name or forename");
    final NameType nameType2 = new NameType();
    nameType2.setvalue("full name of a particular person");
    trans.getNameType().add(nameType);
    trans.getNameType().add(nameType2);
    entSeqOrKEleOrREleOrTrans.add(trans);

    final Name nameEntry = nameEntriesFromXMLToPOJO.buildNameEntry(entry);

    assertEquals(nameType.getvalue().concat("|").concat(nameType2.getvalue()), nameEntry.getType());
  }

}