package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Character;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.*;
import com.github.dianamaftei.yomimashou.dictionary.kanji.Kanji;
import com.github.dianamaftei.yomimashou.dictionary.kanji.KanjiReferences;
import com.github.dianamaftei.yomimashou.dictionary.kanji.KanjiRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KanjiEntriesFromXMLToPOJOTest {
    @InjectMocks
    private KanjiEntriesFromXMLToPOJO kanjiEntriesFromXMLToPOJO;

    @Mock
    private JPAQueryFactory jpaQueryFactory;
    @Mock
    private KanjiRepository kanjiRepository;
    @Mock
    private Kanjidic2 dictionaryFile;
    @Captor
    private ArgumentCaptor<Kanji> kanjiArgumentCaptor;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(kanjiEntriesFromXMLToPOJO, "filePath", "mockFilePath");
    }

    @Test
    public void fillDatabaseShouldAddAllCharactersToTheDatabase() {
        List<Character> characters = dictionaryFile.getCharacter();
        characters.add(new Character());
        characters.add(new Character());
        characters.add(new Character());

        kanjiEntriesFromXMLToPOJO.saveToDB(characters);

        verify(kanjiRepository, times(3)).save(any());
    }

    @Test
    public void fillDatabaseShouldParseKanjiComponentsCorrectly() {
        List<Character> characters = dictionaryFile.getCharacter();
        characters.add(getAMockCharacter());

        kanjiEntriesFromXMLToPOJO.saveToDB(characters);

        verify(kanjiRepository).save(kanjiArgumentCaptor.capture());
        Kanji kanji = kanjiArgumentCaptor.getValue();
        assertEquals("kanji literal", kanji.getCharacter());
        assertEquals("meaning", kanji.getMeaning());
        assertEquals("ja_kun reading", kanji.getKunReading());
        assertEquals("ja_on reading", kanji.getOnReading());
        assertEquals("mock;cpType", kanji.getCodepoint());
        assertEquals("4", kanji.getReferences().getJlptOldLevel());
        assertEquals("radical", kanji.getRadical());
        assertEquals("skip", kanji.getSkipCode());
        assertEquals(Integer.valueOf(8), kanji.getFrequency());
        assertEquals(Integer.valueOf(5), kanji.getGrade());
        assertEquals(Integer.valueOf(7), kanji.getStrokeCount());
    }

    @Test
    public void fillDatabaseShouldParseKanjiDictionaryReferencesCorrectly() {
        List<Character> characters = dictionaryFile.getCharacter();
        characters.add(getAMockCharacter());

        kanjiEntriesFromXMLToPOJO.saveToDB(characters);

        verify(kanjiRepository).save(kanjiArgumentCaptor.capture());
        Kanji kanji = kanjiArgumentCaptor.getValue();
        KanjiReferences kanjiReferences = kanji.getReferences();
        assertEquals("10", kanjiReferences.getNelsonC());
        assertEquals("905", kanjiReferences.getNelsonN());
        assertEquals("3341", kanjiReferences.getHalpernNjecd());
        assertEquals("4148", kanjiReferences.getHalpernKkd());
        assertEquals("2105", kanjiReferences.getHalpernKkld());
        assertEquals("2850", kanjiReferences.getHalpernKkld2ed());
        assertEquals("2949", kanjiReferences.getHeisig());
        assertEquals("2956", kanjiReferences.getHeisig6());
        assertEquals("217", kanjiReferences.getGakken());
        assertEquals("312", kanjiReferences.getOneillNames());
        assertEquals("215", kanjiReferences.getOneillKk());
        assertEquals("19457", kanjiReferences.getMoro());
        assertEquals("1080", kanjiReferences.getHenshall());
        assertEquals("294", kanjiReferences.getHenshall3());
        assertEquals("1918", kanjiReferences.getShKk());
        assertEquals("2098", kanjiReferences.getShKk2());
        assertEquals("402", kanjiReferences.getJfCards());
        assertEquals("213", kanjiReferences.getSakade());
        assertEquals("266", kanjiReferences.getTuttCards());
        assertEquals("192", kanjiReferences.getCrowley());
        assertEquals("557", kanjiReferences.getKanjiInContext());
        assertEquals("1.A", kanjiReferences.getBusyPeople());
        assertEquals("319", kanjiReferences.getKodanshaCompact());
        assertEquals("2045", kanjiReferences.getManiette());
    }

    private Character getAMockCharacter() {
        Character mockCharacter = new Character();
        mockCharacter.getLiteralAndCodepointAndRadical().add("kanji literal");

        ReadingMeaning readingMeaning = new ReadingMeaning();
        Rmgroup rmgroup = new Rmgroup();

        Reading ja_on = new Reading();
        ja_on.setRType("ja_on");
        ja_on.setContent("ja_on reading");
        rmgroup.getReading().add(ja_on);

        Reading ja_kun = new Reading();
        ja_kun.setRType("ja_kun");
        ja_kun.setContent("ja_kun reading");
        rmgroup.getReading().add(ja_kun);

        Meaning meaning = new Meaning();
        meaning.setContent("meaning");
        rmgroup.getMeaning().add(meaning);
        readingMeaning.getRmgroup().add(rmgroup);
        mockCharacter.getLiteralAndCodepointAndRadical().add(readingMeaning);

        Misc misc = new Misc();
        misc.setGrade("5");
        misc.setJlpt("4");
        misc.getStrokeCount().add("7");
        misc.setFreq("8");
        mockCharacter.getLiteralAndCodepointAndRadical().add(misc);

        Codepoint codepoint = new Codepoint();
        CpValue cpValue = new CpValue();
        cpValue.setContent("mock");
        cpValue.setCpType("cpType");
        codepoint.getCpValue().add(cpValue);
        mockCharacter.getLiteralAndCodepointAndRadical().add(codepoint);

        Radical radical = new Radical();
        RadValue radValue = new RadValue();
        radValue.setContent("radical");
        radical.getRadValue().add(radValue);
        mockCharacter.getLiteralAndCodepointAndRadical().add(radical);

        QueryCode queryCode = new QueryCode();
        QCode qCode = new QCode();
        qCode.setQcType("skip");
        qCode.setContent("skip");
        queryCode.getQCode().add(qCode);
        mockCharacter.getLiteralAndCodepointAndRadical().add(queryCode);

        DicNumber dicNumber = new DicNumber();
        addMockDictionaryReferences(dicNumber);
        mockCharacter.getLiteralAndCodepointAndRadical().add(dicNumber);

        return mockCharacter;
    }

    private void addMockDictionaryReferences(DicNumber dicNumber) {
        List<DicRef> dicRefs = dicNumber.getDicRef();

        DicRef nelsonC = new DicRef();
        nelsonC.setDrType("nelson_c");
        nelsonC.setContent("10");
        dicRefs.add(nelsonC);

        DicRef nelsonN = new DicRef();
        nelsonN.setDrType("nelson_n");
        nelsonN.setContent("905");
        dicRefs.add(nelsonN);

        DicRef halpernNjecd = new DicRef();
        halpernNjecd.setDrType("halpern_njecd");
        halpernNjecd.setContent("3341");
        dicRefs.add(halpernNjecd);

        DicRef halpernKkd = new DicRef();
        halpernKkd.setDrType("halpern_kkd");
        halpernKkd.setContent("4148");
        dicRefs.add(halpernKkd);

        DicRef halpernKkld = new DicRef();
        halpernKkld.setDrType("halpern_kkld");
        halpernKkld.setContent("2105");
        dicRefs.add(halpernKkld);

        DicRef halpernKkld2ed = new DicRef();
        halpernKkld2ed.setDrType("halpern_kkld_2ed");
        halpernKkld2ed.setContent("2850");
        dicRefs.add(halpernKkld2ed);

        DicRef heisig = new DicRef();
        heisig.setDrType("heisig");
        heisig.setContent("2949");
        dicRefs.add(heisig);

        DicRef heisig6 = new DicRef();
        heisig6.setDrType("heisig6");
        heisig6.setContent("2956");
        dicRefs.add(heisig6);

        DicRef gakken = new DicRef();
        gakken.setDrType("gakken");
        gakken.setContent("217");
        dicRefs.add(gakken);

        DicRef oneillNames = new DicRef();
        oneillNames.setDrType("oneill_names");
        oneillNames.setContent("312");
        dicRefs.add(oneillNames);

        DicRef oneillKk = new DicRef();
        oneillKk.setDrType("oneill_kk");
        oneillKk.setContent("215");
        dicRefs.add(oneillKk);

        DicRef moro = new DicRef();
        moro.setDrType("moro");
        moro.setContent("19457");
        dicRefs.add(moro);

        DicRef henshall = new DicRef();
        henshall.setDrType("henshall");
        henshall.setContent("1080");
        dicRefs.add(henshall);

        DicRef henshall3 = new DicRef();
        henshall3.setDrType("henshall3");
        henshall3.setContent("294");
        dicRefs.add(henshall3);

        DicRef shKk = new DicRef();
        shKk.setDrType("sh_kk");
        shKk.setContent("1918");
        dicRefs.add(shKk);

        DicRef shKk2 = new DicRef();
        shKk2.setDrType("sh_kk2");
        shKk2.setContent("2098");
        dicRefs.add(shKk2);

        DicRef jfCards = new DicRef();
        jfCards.setDrType("jf_cards");
        jfCards.setContent("402");
        dicRefs.add(jfCards);

        DicRef sakade = new DicRef();
        sakade.setDrType("sakade");
        sakade.setContent("213");
        dicRefs.add(sakade);

        DicRef tuttCards = new DicRef();
        tuttCards.setDrType("tutt_cards");
        tuttCards.setContent("266");
        dicRefs.add(tuttCards);

        DicRef crowley = new DicRef();
        crowley.setDrType("crowley");
        crowley.setContent("192");
        dicRefs.add(crowley);

        DicRef kanjiInContext = new DicRef();
        kanjiInContext.setDrType("kanji_in_context");
        kanjiInContext.setContent("557");
        dicRefs.add(kanjiInContext);

        DicRef busyPeople = new DicRef();
        busyPeople.setDrType("busy_people");
        busyPeople.setContent("1.A");
        dicRefs.add(busyPeople);

        DicRef kodanshaCompact = new DicRef();
        kodanshaCompact.setDrType("kodansha_compact");
        kodanshaCompact.setContent("319");
        dicRefs.add(kodanshaCompact);

        DicRef maniette = new DicRef();
        maniette.setDrType("maniette");
        maniette.setContent("2045");
        dicRefs.add(maniette);

        //TODO test jlpt new
    }

    @Test
    public void fillDatabaseShouldAddKanjiAndKanjiVariantsIfThereAreAny() {
        List<Character> characters = dictionaryFile.getCharacter();
        Character mockCharacter = new Character();
        List<Object> literalAndCodepointAndRadical = mockCharacter.getLiteralAndCodepointAndRadical();
        literalAndCodepointAndRadical.add("kanji");
        Misc misc = new Misc();
        misc.getStrokeCount().add("42");
        literalAndCodepointAndRadical.add(misc);
        characters.add(mockCharacter);
        when(kanjiRepository.findByCharacter(any())).thenReturn(new Kanji());

        kanjiEntriesFromXMLToPOJO.saveToDB(characters);

        verify(kanjiRepository, times(2)).save(any());
        verify(kanjiRepository, times(1)).findByCharacter("kanji");
    }

    @Test
    public void getEntriesShouldReturnAListOfCharacterEntries() {
        List<Character> entries = kanjiEntriesFromXMLToPOJO.getEntries(dictionaryFile);
        verify(dictionaryFile, times(1)).getCharacter();
        assertEquals(entries, dictionaryFile.getCharacter());
    }

    @Test
    public void getClassForJaxbShouldReturnKanjidic2() {
        assertEquals(Kanjidic2.class, kanjiEntriesFromXMLToPOJO.getClassForJaxb());
    }

    @Test
    public void saveToFile() {
        //TODO test this method
    }
}