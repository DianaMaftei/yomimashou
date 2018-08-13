package com.github.dianamaftei.yomimashou.dictionary.creator.XMLToPOJOTransformers;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbGeneratedModels.DictionaryEntry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbGeneratedModels.Kanjidic.Character;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbGeneratedModels.Kanjidic.*;
import com.github.dianamaftei.yomimashou.dictionary.kanji.Kanji;
import com.github.dianamaftei.yomimashou.dictionary.kanji.KanjiReferences;
import com.github.dianamaftei.yomimashou.dictionary.kanji.KanjiRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.dianamaftei.yomimashou.model.QKanjiEntry.kanjiEntry;

@Component
public class KanjiEntriesFromXMLToPOJO extends XMLEntryToPOJO {

    private static final Logger LOGGER = LoggerFactory.getLogger(KanjiEntriesFromXMLToPOJO.class);

    private final JPAQueryFactory jpaQueryFactory;
    private final KanjiRepository kanjiRepository;

    @Autowired
    public KanjiEntriesFromXMLToPOJO(JPAQueryFactory jpaQueryFactory, KanjiRepository kanjiRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.kanjiRepository = kanjiRepository;
        this.dictionarySource = "http://ftp.monash.edu/pub/nihongo/kanjidic2.xml.gz";
    }

    @Override
    void fillDatabase(List<? extends DictionaryEntry> characters) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");

        ((List<Character>) characters).parallelStream().forEach(character -> {
            Kanji kanji = getKanjiEntry(character);
            kanjiRepository.save(kanji);
        });
        addKanjiVariants((List<Character>) characters);
    }

    @Override
    List<Character> getEntries(Object dictionaryFile) {
        return ((Kanjidic2) dictionaryFile).getCharacter();
    }

    @Override
    Class getClassForJaxb() {
        return Kanjidic2.class;
    }

    @Override
    void saveToFile(List<? extends DictionaryEntry> characters) {
        try {
            Set<String> kanjiEntries = ((List<Character>) characters).parallelStream()
                    .filter(Objects::nonNull)
                    .map(this::getKanjiEntry)
                    .map(Kanji::getKanji)
                    .collect(Collectors.toSet());

            writeToFile(kanjiEntries, "kanjiEntries.txt");
        } catch (Exception e) {
            LOGGER.error("Could not save to file kanjiEntries.txt", e);
        }
    }

    private Map<String, String> kanjiJLPTLevelMap(ClassPathResource resource) {
        Map<String, String> kanjiJLPTLevel = new HashMap<>();
        String line;
        String kanji;
        String level;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
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

    private Kanji getKanjiEntry(Character character) {
        Kanji kanjiEntry = new Kanji();
        kanjiEntry.setReferences(new KanjiReferences());

        List<Object> kanjiComponents = character.getLiteralAndCodepointAndRadical();
        ClassPathResource resource = new ClassPathResource("dictionaries" + File.separator + "kanjiByJLPTLevel.csv");

        Map<String, String> kanjiJLPTLevelMap = kanjiJLPTLevelMap(resource);

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
                Kanji kanji = kanjiRepository.findByKanji(kanjiCharacter);
                if (kanji != null) {
                    kanji.setVariant(getKanjiVariant(misc));
                    kanjiRepository.save(kanji);
                    return;
                }
            }
        }
    }

    private String getKanjiVariant(Misc misc) {
        List<String> variants = new ArrayList<>();
        String kanjiVariant = null;

        if (misc.getVariant() != null && !misc.getVariant().isEmpty()) {
            JPAQuery<Kanji> query = jpaQueryFactory.selectFrom(kanjiEntry);
            BooleanBuilder builder = new BooleanBuilder();

            for (Variant variant : misc.getVariant()) {
                builder.or((kanjiEntry.codepoint.like('%' + variant.getContent() + ";" + variant.getVarType() + '%')));
            }

            List<Kanji> kanjiList = query.where(builder).fetch();
            for (Kanji kanji : kanjiList) {
                variants.add(kanji.getKanji());
            }
            kanjiVariant = String.join("|", variants);
        }
        return kanjiVariant;
    }

    private void addReadingsAndMeanings(Kanji kanji, ReadingMeaning component) {
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

        kanji.setOnReading(String.join("|", onReadings));
        kanji.setKunReading(String.join("|", kunReadings));
        kanji.setMeaning(String.join("|", meanings));
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
                case SAKADE:
                    references.setSakade(dicRef.getContent());
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

    private enum KanjiComponentClass {
        STRING, CODEPOINT, RADICAL, MISC, DICNUMBER, QUERYCODE, READINGMEANING
    }

    private enum DictionaryType {
        NELSON_C, NELSON_N, HALPERN_NJECD, HALPERN_KKD, HALPERN_KKLD, HALPERN_KKLD_2ED, HEISIG, HEISIG6, GAKKEN, ONEILL_NAMES, ONEILL_KK, MORO, HENSHALL, HENSHALL3, SH_KK, SH_KK2, SAKADE, JF_CARDS, TUTT_CARDS, CROWLEY, KANJI_IN_CONTEXT, BUSY_PEOPLE, KODANSHA_COMPACT, MANIETTE
    }
}
