package com.yomimashou.creator.dictionary.kanji.kanjicomponents;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.appscommon.model.KanjiReferences;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.DicNumber;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.DicRef;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KanjiDicNumberComponentTest {

    private final KanjiDicNumberComponent kanjiDicNumberComponent = new KanjiDicNumberComponent();

    @ParameterizedTest
    @EnumSource(KanjiComponentType.class)
    void applies_should_returnTrue_onlyFor_dicNumberComponentType(KanjiComponentType type) {
        boolean expected = type == KanjiComponentType.DICNUMBER;
        assertEquals(expected, kanjiDicNumberComponent.applies(type));
    }

    @Test
    void enrich_shouldParse_nelsonCDictionaryReference() {
        final DicRef nelsonC = new DicRef();
        nelsonC.setDrType("nelson_c");
        nelsonC.setContent("10");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(nelsonC);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("10", kanji.getReferences().getNelsonC());
    }

    @Test
    void enrich_shouldParse_nelsonNDictionaryReference() {
        final DicRef nelsonN = new DicRef();
        nelsonN.setDrType("nelson_n");
        nelsonN.setContent("905");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(nelsonN);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("905", kanji.getReferences().getNelsonN());
    }

    @Test
    void enrich_shouldParse_halpernNjecdDictionaryReference() {
        final DicRef halpernNjecd = new DicRef();
        halpernNjecd.setDrType("halpern_njecd");
        halpernNjecd.setContent("3341");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(halpernNjecd);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("3341", kanji.getReferences().getHalpernNjecd());
    }

    @Test
    void enrich_shouldParse_halpernKkdDictionaryReference() {
        final DicRef halpernKkd = new DicRef();
        halpernKkd.setDrType("halpern_kkd");
        halpernKkd.setContent("4148");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(halpernKkd);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("4148", kanji.getReferences().getHalpernKkd());
    }

    @Test
    void enrich_shouldParse_halpernKkldDictionaryReference() {
        final DicRef halpernKkld = new DicRef();
        halpernKkld.setDrType("halpern_kkld");
        halpernKkld.setContent("2105");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(halpernKkld);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("2105", kanji.getReferences().getHalpernKkld());
    }

    @Test
    void enrich_shouldParse_halpernKkld2edDictionaryReference() {
        final DicRef halpernKkld2ed = new DicRef();
        halpernKkld2ed.setDrType("halpern_kkld_2ed");
        halpernKkld2ed.setContent("2850");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(halpernKkld2ed);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("2850", kanji.getReferences().getHalpernKkld2ed());
    }

    @Test
    void enrich_shouldParse_heisigDictionaryReference() {
        final DicRef heisig = new DicRef();
        heisig.setDrType("heisig");
        heisig.setContent("2949");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(heisig);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("2949", kanji.getReferences().getHeisig());
    }

    @Test
    void enrich_shouldParse_heisig6DictionaryReference() {
        final DicRef heisig6 = new DicRef();
        heisig6.setDrType("heisig6");
        heisig6.setContent("2956");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(heisig6);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("2956", kanji.getReferences().getHeisig6());
    }

    @Test
    void enrich_shouldParse_gakkenDictionaryReference() {
        final DicRef gakken = new DicRef();
        gakken.setDrType("gakken");
        gakken.setContent("217");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(gakken);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("217", kanji.getReferences().getGakken());
    }

    @Test
    void enrich_shouldParse_oneillNamesDictionaryReference() {
        final DicRef oneillNames = new DicRef();
        oneillNames.setDrType("oneill_names");
        oneillNames.setContent("312");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(oneillNames);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("312", kanji.getReferences().getOneillNames());
    }

    @Test
    void enrich_shouldParse_oneillKkDictionaryReference() {
        final DicRef oneillKk = new DicRef();
        oneillKk.setDrType("oneill_kk");
        oneillKk.setContent("215");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(oneillKk);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("215", kanji.getReferences().getOneillKk());
    }

    @Test
    void enrich_shouldParse_moroDictionaryReference() {
        final DicRef moro = new DicRef();
        moro.setDrType("moro");
        moro.setContent("19457");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(moro);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("19457", kanji.getReferences().getMoro());
    }

    @Test
    void enrich_shouldParse_henshallDictionaryReference() {
        final DicRef henshall = new DicRef();
        henshall.setDrType("henshall");
        henshall.setContent("1080");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(henshall);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("1080", kanji.getReferences().getHenshall());
    }

    @Test
    void enrich_shouldParse_henshall3DictionaryReference() {
        final DicRef henshall3 = new DicRef();
        henshall3.setDrType("henshall3");
        henshall3.setContent("294");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(henshall3);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("294", kanji.getReferences().getHenshall3());
    }

    @Test
    void enrich_shouldParse_shKkDictionaryReference() {
        final DicRef shKk = new DicRef();
        shKk.setDrType("sh_kk");
        shKk.setContent("1918");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(shKk);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("1918", kanji.getReferences().getShKk());
    }

    @Test
    void enrich_shouldParse_shKk2DictionaryReference() {
        final DicRef shKk2 = new DicRef();
        shKk2.setDrType("sh_kk2");
        shKk2.setContent("2098");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(shKk2);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("2098", kanji.getReferences().getShKk2());
    }

    @Test
    void enrich_shouldParse_jfCardsDictionaryReference() {
        final DicRef jfCards = new DicRef();
        jfCards.setDrType("jf_cards");
        jfCards.setContent("402");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(jfCards);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("402", kanji.getReferences().getJfCards());
    }

    @Test
    void enrich_shouldParse_sakadeDictionaryReference() {
        final DicRef sakade = new DicRef();
        sakade.setDrType("sakade");
        sakade.setContent("213");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(sakade);Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("213", kanji.getReferences().getSakade());
    }

    @Test
    void enrich_shouldParse_tuttCardsDictionaryReference() {
        final DicRef tuttCards = new DicRef();
        tuttCards.setDrType("tutt_cards");
        tuttCards.setContent("266");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(tuttCards);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("266", kanji.getReferences().getTuttCards());
    }

    @Test
    void enrich_shouldParse_crowleyDictionaryReference() {
        final DicRef crowley = new DicRef();
        crowley.setDrType("crowley");
        crowley.setContent("192");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(crowley);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("192", kanji.getReferences().getCrowley());
    }

    @Test
    void enrich_shouldParse_kanjiInContextDictionaryReference() {
        final DicRef kanjiInContext = new DicRef();
        kanjiInContext.setDrType("kanji_in_context");
        kanjiInContext.setContent("557");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(kanjiInContext);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("557", kanji.getReferences().getKanjiInContext());
    }

    @Test
    void enrich_shouldParse_busyPeopleDictionaryReference() {
        final DicRef busyPeople = new DicRef();
        busyPeople.setDrType("busy_people");
        busyPeople.setContent("1.A");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(busyPeople);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("1.A", kanji.getReferences().getBusyPeople());
    }

    @Test
    void enrich_shouldParse_kodanshaCompactDictionaryReference() {
        final DicRef kodanshaCompact = new DicRef();
        kodanshaCompact.setDrType("kodansha_compact");
        kodanshaCompact.setContent("319");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(kodanshaCompact);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("319", kanji.getReferences().getKodanshaCompact());
    }

    @Test
    void enrich_shouldParse_manietteDictionaryReference() {
        final DicRef maniette = new DicRef();
        maniette.setDrType("maniette");
        maniette.setContent("2045");
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(maniette);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiDicNumberComponent.enrich(kanji, dicNumber);

        assertEquals("2045", kanji.getReferences().getManiette());
    }
}