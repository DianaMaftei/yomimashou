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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static com.github.dianamaftei.yomimashou.model.QKanjiEntry.kanjiEntry;

@Component
public class DictionaryXMLtoPOJO {

    private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryXMLtoPOJO.class);
    private final JPAQueryFactory jpaQueryFactory;
    private final WordEntryRepository wordEntryRepository;
    private final KanjiEntryRepository kanjiEntryRepository;
    private final Map<String, String> partsOfSpeech;

    private static final String JMDICT_URL = "http://ftp.monash.edu/pub/nihongo/JMdict_e.gz";
    private static final String JMNEDICT_URL = "http://ftp.monash.edu/pub/nihongo/JMnedict.xml.gz";
    private static final String KANJIDICT_URL = "http://ftp.monash.edu/pub/nihongo/kanjidic2.xml.gz";

    private static final int HIGHEST_PRIORITY = 1;
    private static final int MODERATE_PRIORITY = 2;
    private static final int LOW_PRIORITY = 3;

    @Autowired
    public DictionaryXMLtoPOJO(JPAQueryFactory jpaQueryFactory, WordEntryRepository wordEntryRepository, KanjiEntryRepository kanjiEntryRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.wordEntryRepository = wordEntryRepository;
        this.kanjiEntryRepository = kanjiEntryRepository;
        this.partsOfSpeech = getListOfPartsOfSpeech();
    }

    public void run() {
        System.setProperty("jdk.xml.entityExpansionLimit", "0");

        try {
            processWordEntriesFromXML();
            processKanjiEntriesFromXML();
//            fillNameTableFromXml();

        } catch (Exception e) {
            LOGGER.error("could not process entries from xml", e);
        }
    }

    private Object unmarshalFile(String url, Class jClass) throws JAXBException {
        Object result = null;
        try (InputStream is = new URL(url).openStream(); InputStream gis = new GZIPInputStream(is)) {
            JAXBContext jaxbContext = JAXBContext.newInstance(jClass);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            result = jaxbUnmarshaller.unmarshal(gis);

        } catch (IOException e) {
            LOGGER.error("could not unmarshal file: " + url, e);
        }

        return result;
    }


    /* WORD ENTRY METHODS */
    private void processWordEntriesFromXML() {
        try {
            Object unmarshalledFile = unmarshalFile(JMDICT_URL, JMdict.class);
            if (unmarshalledFile != null) {
                JMdict jmDict = (JMdict) unmarshalledFile;
                List<Entry> dictionaryEntries = jmDict.getEntry();
                saveAllWordEntriesToFile(dictionaryEntries);
//                fillDatabaseWithWordEntries(dictionaryEntries);
            }
        } catch (JAXBException e) {
            LOGGER.error("could not process word entries from XML", e);
        }
    }

    private void saveAllWordEntriesToFile(List<Entry> dictionaryEntries) {
        Set<String> wordEntries = new TreeSet<>();

        dictionaryEntries.stream().parallel().forEach(entry -> {
            entry.getKEle().forEach(kanji -> wordEntries.add(kanji.getKeb()));
            entry.getREle().forEach(reading -> wordEntries.add(reading.getReb()));
        });

        File file = new File("frontend" + File.separator + "src" + File.separator + "data" + File.separator + "wordEntries.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(wordEntries.stream().collect(Collectors.joining("|")));
        } catch (IOException e) {
            LOGGER.error("could not save word entries to file", e);
        }
    }

    private void fillDatabaseWithWordEntries(List<Entry> dictionaryEntries) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");

        dictionaryEntries.stream().parallel().forEach(entry -> {
            WordEntry wordEntry = getWordEntry(entry);
            wordEntryRepository.save(wordEntry);
        });
    }

    private WordEntry getWordEntry(Entry entry) {
        WordEntry wordEntry = new WordEntry();

        wordEntry.setKanjiElements(getKanjiElements(entry));
        wordEntry.setReadingElements(getReadingElements(entry));
        wordEntry.setMeanings(getMeanings(entry));
        wordEntry.setPriority(getHighestPriority(entry));

        return wordEntry;
    }

    private Integer getHighestPriority(Entry entry) {
        Set<Integer> priorities = new HashSet<>();
        entry.getKEle().forEach(kEle -> {
            if (!kEle.getKePri().isEmpty()) {
                kEle.getKePri().forEach(priority -> priorities.add(getPriorityNumber(priority)));
            }
        });
        entry.getREle().forEach(rEle -> {
            if (!rEle.getRePri().isEmpty()) {
                rEle.getRePri().forEach(priority -> priorities.add(getPriorityNumber(priority)));
            }
        });

        if (priorities.isEmpty()) {
            return LOW_PRIORITY;
        }

        return Collections.min(priorities);
    }

    private int getPriorityNumber(String priority) {
        List<String> highestPriorities = Arrays.asList("news1", "ichi1", "spec1", "spec2", "gai1");

        if (highestPriorities.contains(priority)) {
            return HIGHEST_PRIORITY;
        }
        return MODERATE_PRIORITY;
    }

    private String getKanjiElements(Entry entry) {
        List<String> kanjiElements = new ArrayList<>();

        for (KEle kanjiEl : entry.getKEle()) {
            kanjiElements.add(kanjiEl.getKeb());
        }

        return String.join("|", kanjiElements);
    }

    private String getReadingElements(Entry entry) {
        List<String> readingElements = new ArrayList<>();

        for (REle readingEl : entry.getREle()) {
            readingElements.add(readingEl.getReb());
        }

        return String.join("|", readingElements);
    }

    private List<WordMeaning> getMeanings(Entry entry) {
        List<WordMeaning> meanings = new ArrayList<>();

        for (Sense sense : entry.getSense()) {
            WordMeaning meaning = new WordMeaning();
            meaning.setPartOfSpeech(String.join("|", sense.getPos().stream().map(partsOfSpeech::get).filter(Objects::nonNull).collect(Collectors.toList())));
            meaning.setFieldOfApplication(String.join("|", sense.getField()));
            meaning.setAntonym(String.join("|", sense.getAnt()));
            List<String> glosses = new ArrayList<>();
            for (Gloss gloss : sense.getGloss()) {
                for (Serializable item : gloss.getContent())
                    glosses.add(item.toString());
            }
            meaning.setGlosses(String.join("|", glosses));

            meanings.add(meaning);
        }

        return meanings;
    }


    /* KANJI ENTRY METHODS */

    private void processKanjiEntriesFromXML() {
        try {
            Object unmarshalledFile = unmarshalFile(KANJIDICT_URL, Kanjidic2.class);
            if (unmarshalledFile != null) {
                Kanjidic2 kanjiDict = (Kanjidic2) unmarshalledFile;
                List<Character> characters = kanjiDict.getCharacter();

                saveAllKanjiToFile(characters);
//                fillDatabaseWithKanji(characters);
            }
        } catch (JAXBException e) {
            LOGGER.error("could not process kanji entries from XML", e);
        }
    }

    private void fillDatabaseWithKanji(List<Character> characters) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");

        characters.stream().parallel().forEach(character -> {
            KanjiEntry kanjiEntry = getKanjiEntry(character);
            kanjiEntryRepository.save(kanjiEntry);
        });
        addKanjiVariants(characters);
    }


    private void saveAllKanjiToFile(List<Character> characters) {
        Set<String> kanjiEntries = new TreeSet<>();

        characters.stream().parallel().forEach(character -> {
            KanjiEntry kanjiEntry = getKanjiEntry(character);
            kanjiEntries.add(kanjiEntry.getKanji());
        });

        File file = new File("frontend" + File.separator + "src" + File.separator + "data" + File.separator + "kanjiEntries.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(kanjiEntries.stream().collect(Collectors.joining("|")));
        } catch (IOException e) {
            LOGGER.error("could not save kanji entries to file", e);
        }
    }

    private Map<String, String> kanjiJLPTLevelMap(String pathToFile) {
        Map<String, String> kanjiJLPTLevel = new HashMap<>();
        String line;
        String kanji;
        String level;
        try (BufferedReader reader = Files.newBufferedReader(new File(pathToFile).toPath(), Charset.forName("UTF-8"))) {
            while ((line = reader.readLine()) != null) {
                kanji = line.split("\t")[0];
                level = line.split("\t")[1];
                kanjiJLPTLevel.put(kanji, level);
            }
        } catch (IOException e) {
            LOGGER.error("Could not get kanji and JLPT levels", e);
        }

        return kanjiJLPTLevel;
    }

    private KanjiEntry getKanjiEntry(Character character) {
        KanjiEntry kanjiEntry = new KanjiEntry();
        kanjiEntry.setReferences(new KanjiReferences());

        List<Object> kanjiComponents = character.getLiteralAndCodepointAndRadical();
        Map<String, String> kanjiJLPTLevelMap = kanjiJLPTLevelMap("src/main/resources/kanjiByJLPTLevel.csv");

        String kanji = "";

        for (Object component : kanjiComponents) {
            KanjiComponentClass kanjiComponentClass = KanjiComponentClass.valueOf(component.getClass().getSimpleName().toUpperCase());

            switch (kanjiComponentClass) {
                case STRING:
                    kanji = (String) component;
                    kanjiEntry.setKanji(kanji);
                    break;
                case CODEPOINT:
                    List<String> codepoints = new ArrayList<>();
                    for (CpValue cpValue : ((Codepoint) component).getCpValue()) {
                        codepoints.add(cpValue.getContent() + ";" + cpValue.getCpType());
                    }

                    kanjiEntry.setCodepoint(String.join("|", codepoints));
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

        if (kanjiJLPTLevelMap.get(kanji) != null) {
            kanjiEntry.getReferences().setJlptNewLevel(kanjiJLPTLevelMap.get(kanji));
        }

        return kanjiEntry;
    }

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

    private Map<String, String> getListOfPartsOfSpeech() {
        Map<String, String> partsOfSpeechMap = new HashMap<>();
        partsOfSpeechMap.put("adjective (keiyoushi)", "adj-i");
        partsOfSpeechMap.put("adjectival nouns or quasi-adjectives (keiyodoshi)", "adj-na");
        partsOfSpeechMap.put("nouns which may take the genitive case particle 'no'", "adj-no");
        partsOfSpeechMap.put("pre-noun adjectival (rentaishi)", "adj-pn");
        partsOfSpeechMap.put("'taru' adjective", "adj-t");
        partsOfSpeechMap.put("noun or verb acting prenominally (other than the above)", "adj-f");
        partsOfSpeechMap.put("former adjective classification (being removed)", "adj");
        partsOfSpeechMap.put("adverb (fukushi)", "adv");
        partsOfSpeechMap.put("adverbial noun", "adv-n");
        partsOfSpeechMap.put("adverb taking the 'to' particle", "adv-to");
        partsOfSpeechMap.put("auxiliary", "aux");
        partsOfSpeechMap.put("auxiliary verb", "aux-v");
        partsOfSpeechMap.put("auxiliary adjective", "aux-adj");
        partsOfSpeechMap.put("conjunction", "conj");
        partsOfSpeechMap.put("counter", "ctr");
        partsOfSpeechMap.put("Expressions (phrases, clauses, etc.)", "exp");
        partsOfSpeechMap.put("idiomatic expression", "id");
        partsOfSpeechMap.put("interjection (kandoushi)", "int");
        partsOfSpeechMap.put("irregular verb", "iv");
        partsOfSpeechMap.put("noun (common) (futsuumeishi)", "n");
        partsOfSpeechMap.put("adverbial noun (fukushitekimeishi)", "n-adv");
        partsOfSpeechMap.put("noun, used as a prefix", "n-pref");
        partsOfSpeechMap.put("noun (temporal) (jisoumeishi)", "n-t");
        partsOfSpeechMap.put("numeric", "num");
        partsOfSpeechMap.put("pronoun", "pn");
        partsOfSpeechMap.put("prefix", "pref");
        partsOfSpeechMap.put("particle", "prt");
        partsOfSpeechMap.put("suffix", "suf");
        partsOfSpeechMap.put("Ichidan verb", "v1");
        partsOfSpeechMap.put("Godan verb (not completely classified)", "v5");
        partsOfSpeechMap.put("Godan verb - -aru special class", "v5aru");
        partsOfSpeechMap.put("Godan verb with 'bu' ending", "v5b");
        partsOfSpeechMap.put("Godan verb with 'gu' ending", "v5g");
        partsOfSpeechMap.put("Godan verb with 'ku' ending", "v5k");
        partsOfSpeechMap.put("Godan verb - iku/yuku special class", "v5k-s");
        partsOfSpeechMap.put("Godan verb with 'mu' ending", "v5m");
        partsOfSpeechMap.put("Godan verb with 'nu' ending", "v5n");
        partsOfSpeechMap.put("Godan verb with 'ru' ending", "v5r");
        partsOfSpeechMap.put("Godan verb with 'ru' ending (irregular verb)", "v5r-i");
        partsOfSpeechMap.put("Godan verb with 'su' ending", "v5s");
        partsOfSpeechMap.put("Godan verb with 'tsu' ending", "v5t");
        partsOfSpeechMap.put("Godan verb with 'u' ending", "v5u");
        partsOfSpeechMap.put("Godan verb with 'u' ending (special class)", "v5u-s");
        partsOfSpeechMap.put("Godan verb - uru old class verb (old form of Eru)", "v5uru");
        partsOfSpeechMap.put("Godan verb with 'zu' ending", "v5z");
        partsOfSpeechMap.put("Ichidan verb - zuru verb - (alternative form of -jiru verbs)", "vz");
        partsOfSpeechMap.put("intransitive verb", "vi");
        partsOfSpeechMap.put("kuru verb - special class", "vk");
        partsOfSpeechMap.put("irregular nu verb", "vn");
        partsOfSpeechMap.put("noun or participle which takes the aux. verb suru", "vs");
        partsOfSpeechMap.put("suru verb - irregular", "vs-i");
        partsOfSpeechMap.put("suru verb - special class", "vs-s");
        partsOfSpeechMap.put("transitive verb", "vt");
        return partsOfSpeechMap;
    }

    private String getKanjiVariant(Misc misc) {
        List<String> variants = new ArrayList<>();
        String kanjiVariant = null;

        if (misc.getVariant() != null && !misc.getVariant().isEmpty()) {
            JPAQuery<KanjiEntry> query = jpaQueryFactory.selectFrom(kanjiEntry);
            BooleanBuilder builder = new BooleanBuilder();

            for (Variant variant : misc.getVariant()) {
                builder.or((kanjiEntry.codepoint.like('%' + variant.getContent() + ";" + variant.getVarType() + '%')));
            }

            List<KanjiEntry> kanjiList = query.where(builder).fetch();
            for (KanjiEntry kanjiEntry : kanjiList) {
                variants.add(kanjiEntry.getKanji());
            }
            kanjiVariant = String.join("|", variants);
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

        kanjiEntry.setOnReading(String.join("|", onReadings));
        kanjiEntry.setKunReading(String.join("|", kunReadings));
        kanjiEntry.setMeaning(String.join("|", meanings));
    }

    private void addListReferences(KanjiReferences references, DicNumber component) {
        List<DicRef> dicRefs = component.getDicRef();

        for (DicRef dicRef : dicRefs) {
            switch (DictionaryType.valueOf(dicRef.getDrType().toUpperCase())) {
                case NELSON_C:
                    references.setNelsonC(dicRef.getContent());
                    break;
                case NELSON_N:
                    references.setNelsonN(dicRef.getContent());
                    break;
                case HALPERN_NJECD:
                    references.setHalpernNjecd(dicRef.getContent());
                    break;
                case HALPERN_KKD:
                    references.setHalpernKkd(dicRef.getContent());
                    break;
                case HALPERN_KKLD:
                    references.setHalpernKkld(dicRef.getContent());
                    break;
                case HALPERN_KKLD_2ED:
                    references.setHalpernKkld2ed(dicRef.getContent());
                    break;
                case HEISIG:
                    references.setHeisig(dicRef.getContent());
                    break;
                case HEISIG6:
                    references.setHeisig6(dicRef.getContent());
                    break;
                case GAKKEN:
                    references.setGakken(dicRef.getContent());
                    break;
                case ONEILL_NAMES:
                    references.setOneillNames(dicRef.getContent());
                    break;
                case ONEILL_KK:
                    references.setOneillKk(dicRef.getContent());
                    break;
                case MORO:
                    references.setMoro(dicRef.getContent());
                    break;
                case HENSHALL:
                    references.setHenshall(dicRef.getContent());
                    break;
                case HENSHALL3:
                    references.setHenshall3(dicRef.getContent());
                    break;
                case SH_KK:
                    references.setShKk(dicRef.getContent());
                    break;
                case SH_KK2:
                    references.setShKk2(dicRef.getContent());
                    break;
                case JF_CARDS:
                    references.setJfCards(dicRef.getContent());
                    break;
                case TUTT_CARDS:
                    references.setTuttCards(dicRef.getContent());
                    break;
                case CROWLEY:
                    references.setCrowley(dicRef.getContent());
                    break;
                case KANJI_IN_CONTEXT:
                    references.setKanjiInContext(dicRef.getContent());
                    break;
                case BUSY_PEOPLE:
                    references.setBusyPeople(dicRef.getContent());
                    break;
                case KODANSHA_COMPACT:
                    references.setKodanshaCompact(dicRef.getContent());
                    break;
                case MANIETTE:
                    references.setManiette(dicRef.getContent());
                    break;
            }
        }
    }

    /* NAME ENTRY METHODS */

    private void fillNameTableFromXml() {
        try {
            Object unmarshalledFile = unmarshalFile(JMNEDICT_URL, JMnedict.class);
            if (unmarshalledFile != null) {
                JMnedict nameDict = (JMnedict) unmarshalledFile;
            }

        } catch (JAXBException e) {
            LOGGER.error("could not process name entries from XML", e);
        }
    }

    private enum KanjiComponentClass {
        STRING, CODEPOINT, RADICAL, MISC, DICNUMBER, QUERYCODE, READINGMEANING
    }

    private enum DictionaryType {
        NELSON_C, NELSON_N, HALPERN_NJECD, HALPERN_KKD, HALPERN_KKLD, HALPERN_KKLD_2ED, HEISIG, HEISIG6, GAKKEN, ONEILL_NAMES, ONEILL_KK, MORO, HENSHALL, HENSHALL3, SH_KK, SH_KK2, SAKADE, JF_CARDS, TUTT_CARDS, CROWLEY, KANJI_IN_CONTEXT, BUSY_PEOPLE, KODANSHA_COMPACT, MANIETTE
    }
}
