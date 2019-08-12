package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Character;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Codepoint;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.CpValue;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.DicNumber;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.DicRef;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Meaning;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Misc;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.QCode;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.QueryCode;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.RadValue;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Radical;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Reading;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.ReadingMeaning;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Rmgroup;
import com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.kanjicomponents.KanjiCharacterComponent;
import com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.kanjicomponents.KanjiCodepointComponent;
import com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.kanjicomponents.KanjiComponent;
import com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.kanjicomponents.KanjiDicNumberComponent;
import com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.kanjicomponents.KanjiMiscComponent;
import com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.kanjicomponents.KanjiQueryCodeComponent;
import com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.kanjicomponents.KanjiRadicalComponent;
import com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.kanjicomponents.KanjiReadingMeaningComponent;
import com.github.dianamaftei.yomimashou.dictionary.kanji.Kanji;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BuildKanjiEntryTest {

  private KanjiEntriesFromXMLToPOJO kanjiEntriesFromXMLToPOJO;

  @BeforeEach
  void setUp() {
    final List<KanjiComponent> enrichers = new ArrayList<>();
    enrichers.add(new KanjiCharacterComponent());
    enrichers.add(new KanjiCodepointComponent());
    enrichers.add(new KanjiDicNumberComponent());
    enrichers.add(new KanjiMiscComponent());
    enrichers.add(new KanjiQueryCodeComponent());
    enrichers.add(new KanjiRadicalComponent());
    enrichers.add(new KanjiReadingMeaningComponent());

    kanjiEntriesFromXMLToPOJO = new KanjiEntriesFromXMLToPOJO(null, "", "", enrichers);
  }

  @Test
  void buildKanjiEntryShouldExtractKanjiLiteral() {
    final Character character = new Character();
    character.getLiteralAndCodepointAndRadical().add("大");

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals(character.getLiteralAndCodepointAndRadical().get(0), kanji.getCharacter());
  }

  @Test
  void buildKanjiEntryShouldExtractMeaning() {
    final Character character = new Character();
    final ReadingMeaning readingMeaning = new ReadingMeaning();
    final Rmgroup rmgroup = new Rmgroup();

    final Meaning meaning = new Meaning();
    meaning.setContent("big");
    rmgroup.getMeaning().add(meaning);

    final Meaning meaning2 = new Meaning();
    meaning2.setContent("large");
    rmgroup.getMeaning().add(meaning2);

    readingMeaning.getRmgroup().add(rmgroup);
    character.getLiteralAndCodepointAndRadical().add(readingMeaning);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    final String expected = meaning.getContent().concat("|").concat(meaning2.getContent());
    assertEquals(expected, kanji.getMeaning());
  }

  @Test
  void buildKanjiEntryShouldExtractOnReadings() {
    final Character character = new Character();
    final ReadingMeaning readingMeaning = new ReadingMeaning();
    final Rmgroup rmgroup = new Rmgroup();

    final Reading ja_on = new Reading();
    ja_on.setRType("ja_on");
    ja_on.setContent("ダイ");
    rmgroup.getReading().add(ja_on);

    final Reading ja_on2 = new Reading();
    ja_on2.setRType("ja_on");
    ja_on2.setContent("タイ");
    rmgroup.getReading().add(ja_on2);

    readingMeaning.getRmgroup().add(rmgroup);
    character.getLiteralAndCodepointAndRadical().add(readingMeaning);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    final String expected = ja_on.getContent().concat("|").concat(ja_on2.getContent());
    assertEquals(expected, kanji.getOnReading());
  }

  @Test
  void buildKanjiEntryShouldExtractKunReadings() {
    final Character character = new Character();
    final ReadingMeaning readingMeaning = new ReadingMeaning();
    final Rmgroup rmgroup = new Rmgroup();

    final Reading ja_kun = new Reading();
    ja_kun.setRType("ja_kun");
    ja_kun.setContent("おお-");
    rmgroup.getReading().add(ja_kun);

    final Reading ja_kun2 = new Reading();
    ja_kun2.setRType("ja_kun");
    ja_kun2.setContent("おお.きい");
    rmgroup.getReading().add(ja_kun2);

    readingMeaning.getRmgroup().add(rmgroup);
    character.getLiteralAndCodepointAndRadical().add(readingMeaning);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    final String expected = ja_kun.getContent().concat("|").concat(ja_kun2.getContent());
    assertEquals(expected, kanji.getKunReading());
  }

  @Test
  void buildKanjiEntryShouldExtractCodePointIfPresent() {
    final Character character = new Character();
    final Codepoint codepoint = new Codepoint();
    final CpValue cpValue1 = new CpValue();
    cpValue1.setContent("5927");
    cpValue1.setCpType("ucs");
    final CpValue cpValue2 = new CpValue();
    cpValue2.setContent("34-71");
    cpValue2.setCpType("jis208");
    codepoint.getCpValue().add(cpValue1);
    codepoint.getCpValue().add(cpValue2);
    character.getLiteralAndCodepointAndRadical().add(codepoint);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    final String expected = cpValue1.getContent().concat(";").concat(cpValue1.getCpType())
        .concat("|")
        .concat(cpValue2.getContent().concat(";").concat(cpValue2.getCpType()));
    assertEquals(expected, kanji.getCodepoint());
  }

  @Test
  void buildKanjiEntryShouldNotExtractCodePointIfAbsent() {
    final Character character = new Character();
    final Codepoint codepoint = new Codepoint();
    character.getLiteralAndCodepointAndRadical().add(codepoint);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertNull(kanji.getCodepoint());
  }

  @Test
  void buildKanjiEntryShouldExtractOnlyTheFirstRadicalValueIfPresent() {
    final Character character = new Character();
    final Radical radical = new Radical();
    final RadValue radValue = new RadValue();
    radValue.setContent("radical value");
    radical.getRadValue().add(radValue);
    final RadValue radValue2 = new RadValue();
    radValue2.setContent("2nd radical value");
    radical.getRadValue().add(radValue2);
    character.getLiteralAndCodepointAndRadical().add(radical);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("radical value", kanji.getRadical());
  }

  @Test
  void buildKanjiEntryShouldNotExtractRadicalValueIfAbsent() {
    final Character character = new Character();
    final Radical radical = new Radical();
    character.getLiteralAndCodepointAndRadical().add(radical);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertNull(kanji.getRadical());
  }

  @Test
  void buildKanjiEntryShouldExtractSkipCodeIfPresent() {
    final Character character = new Character();
    final QueryCode queryCode = new QueryCode();
    final QCode qCode = new QCode();
    qCode.setQcType("skip");
    qCode.setContent("skip code");
    queryCode.getQCode().add(qCode);
    character.getLiteralAndCodepointAndRadical().add(queryCode);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("skip code", kanji.getSkipCode());
  }

  @Test
  void buildKanjiEntryShouldNotExtractSkipCodeIfAbsent() {
    final Character character = new Character();
    final QueryCode queryCode = new QueryCode();
    final QCode qCode = new QCode();
    queryCode.getQCode().add(qCode);
    character.getLiteralAndCodepointAndRadical().add(queryCode);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertNull(kanji.getSkipCode());
  }

  @Test
  void buildKanjiEntryShouldExtractGradeIfPresent() {
    final Character character = new Character();
    final Misc misc = new Misc();
    misc.setGrade("5");
    character.getLiteralAndCodepointAndRadical().add(misc);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals(Integer.valueOf(5), kanji.getGrade());
  }

  @Test
  void buildKanjiEntryShouldNotExtractGradeIfAbsent() {
    final Character character = new Character();
    final Misc misc = new Misc();
    character.getLiteralAndCodepointAndRadical().add(misc);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertNull(kanji.getGrade());
  }

  @Test
  void buildKanjiEntryShouldExtractFrequencyIfPresent() {
    final Character character = new Character();
    final Misc misc = new Misc();
    misc.setFreq("8");
    character.getLiteralAndCodepointAndRadical().add(misc);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals(Integer.valueOf(8), kanji.getFrequency());
  }

  @Test
  void buildKanjiEntryShouldNotExtractFrequencyIfAbsent() {
    final Character character = new Character();
    final Misc misc = new Misc();
    character.getLiteralAndCodepointAndRadical().add(misc);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertNull(kanji.getFrequency());
  }

  @Test
  void buildKanjiEntryShouldExtractOldJLPTLevel() {
    final Character character = new Character();
    final Misc misc = new Misc();
    misc.setJlpt("4");
    character.getLiteralAndCodepointAndRadical().add(misc);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("4", kanji.getReferences().getJlptOldLevel());
  }

  @Test
  void buildKanjiEntryShouldExtractStrokeCountIfPresent() {
    final Character character = new Character();
    final Misc misc = new Misc();
    misc.getStrokeCount().add("7");
    character.getLiteralAndCodepointAndRadical().add(misc);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals(Integer.valueOf(7), kanji.getStrokeCount());
  }

  @Test
  void buildKanjiEntryShouldNotExtractStrokeCountIfAbsent() {
    final Character character = new Character();
    final Misc misc = new Misc();
    character.getLiteralAndCodepointAndRadical().add(misc);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertNull(kanji.getStrokeCount());
  }

  @Test
  void buildKanjiEntryShouldExtractNelsonNDictionaryReference() {
    final DicRef nelsonC = new DicRef();
    nelsonC.setDrType("nelson_c");
    nelsonC.setContent("10");
    final Character character = buildCharacterWithDictionaryReference(nelsonC);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("10", kanji.getReferences().getNelsonC());
  }

  @Test
  void buildKanjiEntryShouldExtractNelsonCDictionaryReference() {
    final DicRef nelsonN = new DicRef();
    nelsonN.setDrType("nelson_n");
    nelsonN.setContent("905");
    final Character character = buildCharacterWithDictionaryReference(nelsonN);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("905", kanji.getReferences().getNelsonN());
  }

  @Test
  void buildKanjiEntryShouldExtractHalpernNjecdDictionaryReference() {
    final DicRef halpernNjecd = new DicRef();
    halpernNjecd.setDrType("halpern_njecd");
    halpernNjecd.setContent("3341");
    final Character character = buildCharacterWithDictionaryReference(halpernNjecd);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("3341", kanji.getReferences().getHalpernNjecd());
  }

  @Test
  void buildKanjiEntryShouldExtractHalpernKkdDictionaryReference() {
    final DicRef halpernKkd = new DicRef();
    halpernKkd.setDrType("halpern_kkd");
    halpernKkd.setContent("4148");
    final Character character = buildCharacterWithDictionaryReference(halpernKkd);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("4148", kanji.getReferences().getHalpernKkd());
  }

  @Test
  void buildKanjiEntryShouldExtractHalpernKkldDictionaryReference() {
    final DicRef halpernKkld = new DicRef();
    halpernKkld.setDrType("halpern_kkld");
    halpernKkld.setContent("2105");
    final Character character = buildCharacterWithDictionaryReference(halpernKkld);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("2105", kanji.getReferences().getHalpernKkld());
  }

  @Test
  void buildKanjiEntryShouldExtractHalpernKkld2edDictionaryReference() {
    final DicRef halpernKkld2ed = new DicRef();
    halpernKkld2ed.setDrType("halpern_kkld_2ed");
    halpernKkld2ed.setContent("2850");
    final Character character = buildCharacterWithDictionaryReference(halpernKkld2ed);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("2850", kanji.getReferences().getHalpernKkld2ed());
  }

  @Test
  void buildKanjiEntryShouldExtractHeisigDictionaryReference() {
    final DicRef heisig = new DicRef();
    heisig.setDrType("heisig");
    heisig.setContent("2949");
    final Character character = buildCharacterWithDictionaryReference(heisig);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("2949", kanji.getReferences().getHeisig());
  }

  @Test
  void buildKanjiEntryShouldExtractHeisig6DictionaryReference() {
    final DicRef heisig6 = new DicRef();
    heisig6.setDrType("heisig6");
    heisig6.setContent("2956");
    final Character character = buildCharacterWithDictionaryReference(heisig6);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("2956", kanji.getReferences().getHeisig6());
  }

  @Test
  void buildKanjiEntryShouldExtractGakkenDictionaryReference() {
    final DicRef gakken = new DicRef();
    gakken.setDrType("gakken");
    gakken.setContent("217");
    final Character character = buildCharacterWithDictionaryReference(gakken);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("217", kanji.getReferences().getGakken());
  }

  @Test
  void buildKanjiEntryShouldExtractOneillNamesDictionaryReference() {
    final DicRef oneillNames = new DicRef();
    oneillNames.setDrType("oneill_names");
    oneillNames.setContent("312");
    final Character character = buildCharacterWithDictionaryReference(oneillNames);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("312", kanji.getReferences().getOneillNames());
  }

  @Test
  void buildKanjiEntryShouldExtractOneillKkDictionaryReference() {
    final DicRef oneillKk = new DicRef();
    oneillKk.setDrType("oneill_kk");
    oneillKk.setContent("215");
    final Character character = buildCharacterWithDictionaryReference(oneillKk);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("215", kanji.getReferences().getOneillKk());
  }

  @Test
  void buildKanjiEntryShouldExtractMoroDictionaryReference() {
    final DicRef moro = new DicRef();
    moro.setDrType("moro");
    moro.setContent("19457");
    final Character character = buildCharacterWithDictionaryReference(moro);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("19457", kanji.getReferences().getMoro());
  }

  @Test
  void buildKanjiEntryShouldExtractHenshallDictionaryReference() {
    final DicRef henshall = new DicRef();
    henshall.setDrType("henshall");
    henshall.setContent("1080");
    final Character character = buildCharacterWithDictionaryReference(henshall);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("1080", kanji.getReferences().getHenshall());
  }

  @Test
  void buildKanjiEntryShouldExtractHenshall3DictionaryReference() {
    final DicRef henshall3 = new DicRef();
    henshall3.setDrType("henshall3");
    henshall3.setContent("294");
    final Character character = buildCharacterWithDictionaryReference(henshall3);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("294", kanji.getReferences().getHenshall3());
  }

  @Test
  void buildKanjiEntryShouldExtractShKkDictionaryReference() {
    final DicRef shKk = new DicRef();
    shKk.setDrType("sh_kk");
    shKk.setContent("1918");
    final Character character = buildCharacterWithDictionaryReference(shKk);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("1918", kanji.getReferences().getShKk());
  }

  @Test
  void buildKanjiEntryShouldExtractShKk2DictionaryReference() {
    final DicRef shKk2 = new DicRef();
    shKk2.setDrType("sh_kk2");
    shKk2.setContent("2098");
    final Character character = buildCharacterWithDictionaryReference(shKk2);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("2098", kanji.getReferences().getShKk2());
  }

  @Test
  void buildKanjiEntryShouldExtractJfCardsDictionaryReference() {
    final DicRef jfCards = new DicRef();
    jfCards.setDrType("jf_cards");
    jfCards.setContent("402");
    final Character character = buildCharacterWithDictionaryReference(jfCards);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("402", kanji.getReferences().getJfCards());
  }

  @Test
  void buildKanjiEntryShouldExtractSakadeDictionaryReference() {
    final DicRef sakade = new DicRef();
    sakade.setDrType("sakade");
    sakade.setContent("213");
    final Character character = buildCharacterWithDictionaryReference(sakade);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("213", kanji.getReferences().getSakade());
  }

  @Test
  void buildKanjiEntryShouldExtractTuttCardsDictionaryReference() {
    final DicRef tuttCards = new DicRef();
    tuttCards.setDrType("tutt_cards");
    tuttCards.setContent("266");
    final Character character = buildCharacterWithDictionaryReference(tuttCards);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("266", kanji.getReferences().getTuttCards());
  }

  @Test
  void buildKanjiEntryShouldExtractCrowleyDictionaryReference() {
    final DicRef crowley = new DicRef();
    crowley.setDrType("crowley");
    crowley.setContent("192");
    final Character character = buildCharacterWithDictionaryReference(crowley);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("192", kanji.getReferences().getCrowley());
  }

  @Test
  void buildKanjiEntryShouldExtractKanjiInContextDictionaryReference() {
    final DicRef kanjiInContext = new DicRef();
    kanjiInContext.setDrType("kanji_in_context");
    kanjiInContext.setContent("557");
    final Character character = buildCharacterWithDictionaryReference(kanjiInContext);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("557", kanji.getReferences().getKanjiInContext());
  }

  @Test
  void buildKanjiEntryShouldExtractBusyPeopleDictionaryReference() {
    final DicRef busyPeople = new DicRef();
    busyPeople.setDrType("busy_people");
    busyPeople.setContent("1.A");
    final Character character = buildCharacterWithDictionaryReference(busyPeople);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("1.A", kanji.getReferences().getBusyPeople());
  }

  @Test
  void buildKanjiEntryShouldExtractKodanshaCompactDictionaryReference() {
    final DicRef kodanshaCompact = new DicRef();
    kodanshaCompact.setDrType("kodansha_compact");
    kodanshaCompact.setContent("319");
    final Character character = buildCharacterWithDictionaryReference(kodanshaCompact);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("319", kanji.getReferences().getKodanshaCompact());
  }

  @Test
  void buildKanjiEntryShouldExtractManietteDictionaryReference() {
    final DicRef maniette = new DicRef();
    maniette.setDrType("maniette");
    maniette.setContent("2045");
    final Character character = buildCharacterWithDictionaryReference(maniette);

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("2045", kanji.getReferences().getManiette());
  }

  private Character buildCharacterWithDictionaryReference(final DicRef dicRef) {
    final Character character = new Character();
    final List<DicRef> dicRefs = new ArrayList<>();

    dicRefs.add(dicRef);
    final DicNumber dicNumber = new DicNumber();
    dicNumber.getDicRef().addAll(dicRefs);
    character.getLiteralAndCodepointAndRadical().add(dicNumber);
    return character;
  }

  @Test
  void buildKanjiEntryShouldPopulateNewJLPTLevelIfPresent() {
    final Map<String, String> kanjiJLPTLevelMap = new HashMap<>();
    kanjiJLPTLevelMap.put("大", "mock N5");
    kanjiEntriesFromXMLToPOJO.setKanjiJLPTLevelMap(kanjiJLPTLevelMap);

    final Character character = new Character();
    character.getLiteralAndCodepointAndRadical().add("大");

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertEquals("mock N5", kanji.getReferences().getJlptNewLevel());
  }

  @Test
  void buildKanjiEntryShouldNotPopulateNewJLPTLevelIfAbsent() {
    final Map<String, String> kanjiJLPTLevelMap = new HashMap<>();
    kanjiEntriesFromXMLToPOJO.setKanjiJLPTLevelMap(kanjiJLPTLevelMap);

    final Character character = new Character();
    character.getLiteralAndCodepointAndRadical().add("大");

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertNull(kanji.getReferences().getJlptNewLevel());
  }

  @Test
  void buildKanjiEntryShouldPopulateRtkInfoIfPresent() {
    final Map<String, RtkKanji> rtkKanjiMap = new HashMap<>();
    final RtkKanji rtkKanji = new RtkKanji();
    rtkKanji.setKeyword("mock large");
    rtkKanji.setComponents("mock 大: big, big radical (no. 37)");
    rtkKanji.setStory1(
        "mock A person stretching their arms and legs to look large and scare a bear away.");
    rtkKanji.setStory2("mock Think of a large St. Bernard dog with his paws all stretched out.");
    rtkKanjiMap.put("大", rtkKanji);
    kanjiEntriesFromXMLToPOJO.setRtkKanjiMap(rtkKanjiMap);

    final Character character = new Character();
    character.getLiteralAndCodepointAndRadical().add("大");

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertAll("Should populate kanji with all the values from the rtkkanji object",
        () -> assertEquals(rtkKanji.getKeyword(), kanji.getKeyword()),
        () -> assertEquals(rtkKanji.getComponents(), kanji.getComponents()),
        () -> assertEquals(rtkKanji.getStory1(), kanji.getStory1()),
        () -> assertEquals(rtkKanji.getStory2(), kanji.getStory2())
    );
  }

  @Test
  void buildKanjiEntryShouldNotPopulateRtkInfoIfAbsent() {
    final Map<String, RtkKanji> rtkKanjiMap = new HashMap<>();
    kanjiEntriesFromXMLToPOJO.setRtkKanjiMap(rtkKanjiMap);

    final Character character = new Character();
    character.getLiteralAndCodepointAndRadical().add("大");

    final Kanji kanji = kanjiEntriesFromXMLToPOJO.buildKanjiEntry(character);

    assertAll("Should not populate kanji with rtk values if it can't find the rtk kanji",
        () -> assertNull(kanji.getKeyword()),
        () -> assertNull(kanji.getComponents()),
        () -> assertNull(kanji.getStory1()),
        () -> assertNull(kanji.getStory2())
    );
  }
}
