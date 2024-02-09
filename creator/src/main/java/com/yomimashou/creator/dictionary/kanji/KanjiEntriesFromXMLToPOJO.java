package com.yomimashou.creator.dictionary.kanji;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.appscommon.model.KanjiReferences;
import com.yomimashou.appscommon.service.KanjiCategoriesService;
import com.yomimashou.creator.dictionary.DictionaryEntry;
import com.yomimashou.creator.dictionary.XMLEntryToPOJO;
import com.yomimashou.creator.dictionary.kanji.kanjicomponents.KanjiComponent;
import com.yomimashou.creator.dictionary.kanji.kanjicomponents.KanjiComponentType;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Character;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Kanjidic2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KanjiEntriesFromXMLToPOJO extends XMLEntryToPOJO {

    private static final String TAB = "\t";
    private final KanjiCategoriesService kanjiCategoriesService;
    private final KanjiRepository kanjiRepository;
    private final String rtkFilePath;
    private Reader reader;
    private Map<String, RtkKanji> rtkKanjiMap;
    private List<KanjiComponent> kanjiComponentsEnrichers;

    @Autowired
    public KanjiEntriesFromXMLToPOJO(final KanjiCategoriesService kanjiCategoriesService,
                                     final KanjiRepository kanjiRepository,
                                     @Value("${path.dictionary.kanji}") final String kanjiDictionaryPath,
                                     @Value("${path.rtk}") final String rtkFilePath,
                                     final List<KanjiComponent> kanjiComponentsEnrichers) {
        this.kanjiRepository = kanjiRepository;
        this.kanjiCategoriesService = kanjiCategoriesService;
        this.inputFile = kanjiDictionaryPath;
        this.rtkFilePath = rtkFilePath;
        this.kanjiComponentsEnrichers = kanjiComponentsEnrichers;
    }

    @Override
    protected void persist(final List<? extends DictionaryEntry> characters) {
        kanjiRepository.saveAll(
                ((List<Character>) characters).parallelStream()
                        .filter(Objects::nonNull)
                        .map(this::buildKanjiEntry)
                        .collect(Collectors.toList())
        );
    }

    @Override
    protected List<Character> getEntries(final Object dictionaryFile) {
        return ((Kanjidic2) dictionaryFile).getCharacter();
    }

    @Override
    protected Class getClassForJaxb() {
        return Kanjidic2.class;
    }

    private Map<String, RtkKanji> loadRtkKanji() {
        final Map<String, RtkKanji> rtkKanjis = new HashMap<>();

        try (final BufferedReader bufferedReader = new BufferedReader(getReader(rtkFilePath))) {
            bufferedReader.lines()
                    .forEach(line -> {
                        final RtkKanji rtkKanji = new RtkKanji();
                        final String[] splitLine = line.split(TAB);
                        rtkKanji.setKanji(splitLine[0]);
                        if (splitLine.length > 1) {
                            rtkKanji.setComponents(splitLine[1]);
                        }
                        if (splitLine.length > 2) {
                            rtkKanji.setKeyword(splitLine[2]);
                            rtkKanji.setStory1(splitLine[3]);
                            rtkKanji.setStory2(splitLine[4]);
                        }

                        rtkKanjis.put(rtkKanji.getKanji(), rtkKanji);
                    });

        } catch (final IOException e) {
            log.error("Could not get rtk info from file", e);
        }

        return rtkKanjis;
    }

    Kanji buildKanjiEntry(final Character character) {
        final Kanji kanjiEntry = new Kanji();
        kanjiEntry.setReferences(new KanjiReferences());

        final List<Object> kanjiComponents = character.getLiteralAndCodepointAndRadical();

        for (final Object component : kanjiComponents) {
            final KanjiComponentType kanjiComponentType = KanjiComponentType
                    .valueOf(component.getClass().getSimpleName().toUpperCase());

            kanjiComponentsEnrichers.stream()
                    .filter(enricher -> enricher.applies(kanjiComponentType))
                    .forEach(enricher -> enricher.enrich(kanjiEntry, component));
        }

        enrichKanjiEntryWithJLPTInfo(kanjiEntry);
        enrichKanjiEntryWithRtkInfo(kanjiEntry);

        return kanjiEntry;
    }

    private void enrichKanjiEntryWithJLPTInfo(final Kanji kanjiEntry) {
        String character = kanjiEntry.getCharacter();
        if (!StringUtils.isBlank(character)) {
            kanjiEntry.getReferences().setJlptNewLevel(kanjiCategoriesService.getJlptLevel(character.charAt(0)));
        }
    }

    private void enrichKanjiEntryWithRtkInfo(final Kanji kanjiEntry) {
        if (rtkKanjiMap == null) {
            rtkKanjiMap = loadRtkKanji();
        }

        final RtkKanji rtkKanji = rtkKanjiMap.get(kanjiEntry.getCharacter());
        if (rtkKanji != null) {
            kanjiEntry.setKeyword(rtkKanji.getKeyword());
            kanjiEntry.setComponents(rtkKanji.getComponents());
            kanjiEntry.setStory1(rtkKanji.getStory1());
            kanjiEntry.setStory2(rtkKanji.getStory2());
        }
    }

    void setRtkKanjiMap(final Map<String, RtkKanji> rtkKanjiMap) {
        this.rtkKanjiMap = rtkKanjiMap;
    }

    private Reader getReader(final String filePath) throws FileNotFoundException {
        if (reader != null) {
            return reader;
        }

        return new FileReader(filePath);
    }

    public void setReader(final Reader reader) {
        this.reader = reader;
    }

    public void setKanjiComponentsEnrichers(
            final List<KanjiComponent> kanjiComponentsEnrichers) {
        this.kanjiComponentsEnrichers = kanjiComponentsEnrichers;
    }
}
