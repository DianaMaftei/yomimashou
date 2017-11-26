package com.github.dianamaftei.yomimashou.creator;

import com.github.dianamaftei.yomimashou.model.KanjiEntry;
import com.github.dianamaftei.yomimashou.model.KanjiReferences;
import com.github.dianamaftei.yomimashou.model.WordEntry;
import com.github.dianamaftei.yomimashou.model.WordMeaning;
import com.github.dianamaftei.yomimashou.model.xmlOriginalModels.JMdict.*;
import com.github.dianamaftei.yomimashou.model.xmlOriginalModels.JMnedict.JMnedict;
import com.github.dianamaftei.yomimashou.model.xmlOriginalModels.Kanjidic.Character;
import com.github.dianamaftei.yomimashou.model.xmlOriginalModels.Kanjidic.*;
import com.github.dianamaftei.yomimashou.repository.KanjiEntryRepository;
import com.github.dianamaftei.yomimashou.repository.WordEntryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.github.dianamaftei.yomimashou.model.QKanjiEntry.kanjiEntry;

/*
    run fetchDictionariesScript.sh to get the xml files TODO - run from inside this class
    check out xsd file comments for explanation of entity structure
*/

@Component
public class DictionaryXMLtoPOJO {

    private final JPAQueryFactory jpaQueryFactory;
    private final WordEntryRepository wordEntryRepository;
    private final KanjiEntryRepository kanjiEntryRepository;

    @Autowired
    public DictionaryXMLtoPOJO(JPAQueryFactory jpaQueryFactory, WordEntryRepository wordEntryRepository, KanjiEntryRepository kanjiEntryRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.wordEntryRepository = wordEntryRepository;
        this.kanjiEntryRepository = kanjiEntryRepository;
    }

    public void run() {
        System.setProperty("jdk.xml.entityExpansionLimit", "0");

        try {
            fillWordTableFromXml();
            fillKanjiTableFromXml();
//          fillNameTableFromXml();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object unmarshalFile(String fileName, Class jClass) throws JAXBException {
        File file = new File("src\\main\\resources\\dictionaryXMLData\\" + fileName);
        JAXBContext jaxbContext = JAXBContext.newInstance(jClass);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return jaxbUnmarshaller.unmarshal(file);
    }


    /* WORD ENTRY METHODS */
    private void fillWordTableFromXml() {
        try {
            JMdict jmDict = (JMdict) unmarshalFile("JMdict_e.xml", JMdict.class);
            List<Entry> dictionaryEntries = jmDict.getEntry();

            // limit to a sample of 100 for testing purposes
            int limit = 100;
            for (Entry entry : dictionaryEntries) {
                if (limit > 0) {
                    WordEntry wordEntry = getWordEntry(entry);
                    wordEntryRepository.save(wordEntry);
                    limit--;
                }
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private WordEntry getWordEntry(Entry entry) {
        WordEntry wordEntry = new WordEntry();

        wordEntry.setKanjiElements(getKanjiElements(entry));
        wordEntry.setReadingElements(getReadingElements(entry));
        wordEntry.setMeanings(getMeanings(entry));

        return wordEntry;
    }

    private String getKanjiElements(Entry entry) {
        List<String> kanjiElements = new ArrayList<>();

        for (KEle kanjiEl : entry.getKEle()) {
            kanjiElements.add(kanjiEl.getKeb());
        }

        return String.join("/", kanjiElements);
    }

    private String getReadingElements(Entry entry) {
        List<String> readingElements = new ArrayList<>();

        for (REle readingEl : entry.getREle()) {
            readingElements.add(readingEl.getReb());
        }

        return String.join("/", readingElements);
    }

    private List<WordMeaning> getMeanings(Entry entry) {
        List<WordMeaning> meanings = new ArrayList<>();

        for (Sense sense : entry.getSense()) {
            WordMeaning meaning = new WordMeaning();
            meaning.setPartOfSpeech(String.join("/", sense.getPos()));
            meaning.setFieldOfApplication(String.join("/", sense.getField()));
            meaning.setAntonym(String.join("/", sense.getAnt()));
            List<String> glosses = new ArrayList<>();
            for (Gloss gloss : sense.getGloss()) {
                for (Serializable item : gloss.getContent())
                    glosses.add(item.toString());
            }
            meaning.setGlosses(String.join("/", glosses));

            meanings.add(meaning);
        }

        return meanings;
    }


    /* KANJI ENTRY METHODS */

    private void fillKanjiTableFromXml() {
        try {
            Kanjidic2 kanjiDict = (Kanjidic2) unmarshalFile("kanjidic2.xml", Kanjidic2.class);
            List<Character> characters = kanjiDict.getCharacter();

            int limit = 100;
            for (Character character : characters) {
                if (limit > 0) {
                    KanjiEntry kanjiEntry = getKanjiEntry(character);
                    kanjiEntryRepository.save(kanjiEntry);
                    limit--;
                }
            }

//            addKanjiVariants(characters);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private KanjiEntry getKanjiEntry(Character character) {
        KanjiEntry kanjiEntry = new KanjiEntry();
        kanjiEntry.setReferences(new KanjiReferences());

        List<Object> kanjiComponents = character.getLiteralAndCodepointAndRadical();

        for (Object component : kanjiComponents) {
            KanjiComponentClass kanjiComponentClass = KanjiComponentClass.valueOf(component.getClass().getSimpleName().toUpperCase());

            switch (kanjiComponentClass) {
                case STRING:
                    kanjiEntry.setKanji((String) component);
                    break;
                case CODEPOINT:
                    List<String> codepoints = new ArrayList<>();
                    for (CpValue cpValue : ((Codepoint) component).getCpValue()) {
                        codepoints.add(cpValue.getContent() + ";" + cpValue.getCpType());
                    }

                    kanjiEntry.setCodepoint(String.join("/", codepoints));
                    break;
                case RADICAL:
                    // only one main radical
                    kanjiEntry.setRadical(((Radical) component).getRadValue().get(0).getContent());
                    break;
                case MISC:
                    Misc misc = (Misc) component;
                    if (misc.getGrade() != null) {
                        kanjiEntry.setGrade(Integer.valueOf(misc.getGrade()));
                    }
                    //  the first is the accepted count, the rest are common miscounts
                    if (misc.getStrokeCount() != null && misc.getStrokeCount().get(0) != null) {
                        kanjiEntry.setStrokeCount(Integer.valueOf(misc.getStrokeCount().get(0)));
                    }

                    if (misc.getFreq() != null) {
                        kanjiEntry.setFrequency(Integer.valueOf(misc.getFreq()));
                    }

                    kanjiEntry.getReferences().setJlptOldLevel(misc.getJlpt());
                    break;
                case DICNUMBER:
                    addListReferences(kanjiEntry.getReferences(), (DicNumber) component);
                    break;
                case QUERYCODE:
                    QueryCode queryCode = (QueryCode) component;
                    for (QCode qCode : queryCode.getQCode()) {
                        if ("skip".equals(qCode.getQcType())) {
                            kanjiEntry.setSkipCode(qCode.getContent());
                        }
                    }
                    break;
                case READINGMEANING:
                    addReadingsAndMeanings(kanjiEntry, (ReadingMeaning) component);
                    break;
            }
        }

        return kanjiEntry;
    }

    @Transactional
    private void addKanjiVariants(List<Character> characters) {
        for (Character character : characters) {
            List<Object> kanjiInfo = character.getLiteralAndCodepointAndRadical();

            String kanjiCharacter = null;
            Misc misc = null;

            for (Object info : kanjiInfo) {
                if (info instanceof String) {
                    kanjiCharacter = (String) info;
                } else if (info instanceof Misc) {
                    misc = (Misc) info;
                }
            }

            if (kanjiCharacter != null && misc != null) {
                KanjiEntry kanjiEntry = kanjiEntryRepository.findByKanji(kanjiCharacter);
                if (kanjiEntry != null) {
                    kanjiEntry.setVariant(getKanjiVariant(misc));
                    kanjiEntryRepository.save(kanjiEntry);
                    return;
                }
            }
        }
    }

    private String getKanjiVariant(Misc misc) {
        List<String> variants = new ArrayList<>();
        String kanjiVariant = null;

        if (misc.getVariant() != null && misc.getVariant().size() > 0) {
            JPAQuery<KanjiEntry> query = jpaQueryFactory.selectFrom(kanjiEntry);
            BooleanBuilder builder = new BooleanBuilder();

            for (Variant variant : misc.getVariant()) {
                builder.or((kanjiEntry.codepoint.like('%' + variant.getContent() + ";" + variant.getVarType() + '%')));
            }

            List<KanjiEntry> kanjiList = query.where(builder).fetch();
            for (KanjiEntry kanjiEntry : kanjiList) {
                variants.add(kanjiEntry.getKanji());
            }
            kanjiVariant = String.join("/", variants);
        }
        return kanjiVariant;
    }

    private void addReadingsAndMeanings(KanjiEntry kanjiEntry, ReadingMeaning component) {
        List<String> onReadings = new ArrayList<>();
        List<String> kunReadings = new ArrayList<>();
        List<String> meanings = new ArrayList<>();

        for (Rmgroup rmgroup : component.getRmgroup()) {

            for (Reading reading : rmgroup.getReading()) {
                if ("ja_on".equals(reading.getRType())) {
                    onReadings.add(reading.getContent());
                    continue;
                }

                if ("ja_kun".equals(reading.getRType())) {
                    kunReadings.add(reading.getContent());
                }
            }

            for (Meaning meaning : rmgroup.getMeaning()) {
                if (meaning.getMLang() == null) {
                    meanings.add(meaning.getContent());
                }
            }

        }

        kanjiEntry.setOnReading(String.join("/", onReadings));
        kanjiEntry.setKunReading(String.join("/", kunReadings));
        kanjiEntry.setMeaning(String.join("/", meanings));
    }

    private void addListReferences(KanjiReferences references, DicNumber component) {
        List<DicRef> dicRefs = component.getDicRef();

        for (DicRef dicRef : dicRefs) {
            switch (dicRef.getDrType()) {
                case "nelson_c":
                    references.setNelsonC(dicRef.getContent());
                    break;
                case "nelson_n":
                    references.setNelsonN(dicRef.getContent());
                    break;
                case "halpern_njecd":
                    references.setHalpernNjecd(dicRef.getContent());
                    break;
                case "halpern_kkd":
                    references.setHalpernKkd(dicRef.getContent());
                    break;
                case "halpern_kkld":
                    references.setHalpernKkld(dicRef.getContent());
                    break;
                case "halpern_kkld_2ed":
                    references.setHalpernKkld2ed(dicRef.getContent());
                    break;
                case "heisig":
                    references.setHeisig(dicRef.getContent());
                    break;
                case "heisig6":
                    references.setHeisig6(dicRef.getContent());
                    break;
                case "gakken":
                    references.setGakken(dicRef.getContent());
                    break;
                case "oneill_names":
                    references.setOneillNames(dicRef.getContent());
                    break;
                case "oneill_kk":
                    references.setOneillKk(dicRef.getContent());
                    break;
                case "moro":
                    references.setMoro(dicRef.getContent());
                    break;
                case "henshall":
                    references.setHenshall(dicRef.getContent());
                    break;
                case "henshall3":
                    references.setHenshall3(dicRef.getContent());
                    break;
                case "sh_kk":
                    references.setShKk(dicRef.getContent());
                    break;
                case "sh_kk2":
                    references.setShKk2(dicRef.getContent());
                    break;
                case "jf_cards":
                    references.setJfCards(dicRef.getContent());
                    break;
                case "tutt_cards":
                    references.setTuttCards(dicRef.getContent());
                    break;
                case "crowley":
                    references.setCrowley(dicRef.getContent());
                    break;
                case "kanji_in_context":
                    references.setKanjiInContext(dicRef.getContent());
                    break;
                case "busy_people":
                    references.setBusyPeople(dicRef.getContent());
                    break;
                case "kodansha_compact":
                    references.setKodanshaCompact(dicRef.getContent());
                    break;
                case "maniette":
                    references.setManiette(dicRef.getContent());
                    break;
            }
        }
    }

    private void fillNameTableFromXml() {
        try {
            JMnedict nameDict = (JMnedict) unmarshalFile("JMnedict.xml", JMnedict.class);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private enum KanjiComponentClass {
        STRING, CODEPOINT, RADICAL, MISC, DICNUMBER, QUERYCODE, READINGMEANING
    }
}
