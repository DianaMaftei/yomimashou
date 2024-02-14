package com.yomimashou.creator.dictionary.kanji;

import com.yomimashou.creator.dictionary.DictionaryEntry;
import com.yomimashou.creator.dictionary.DictionaryService;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Character;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Kanjidic2;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class KanjiDictionaryService extends DictionaryService {

    private KanjiRepository kanjiRepository;
    private KanjiParser kanjiParser;

    @Override
    protected void persist(final List<? extends DictionaryEntry> characters) {
        kanjiRepository.saveAll(
                ((List<Character>) characters).parallelStream()
                        .filter(Objects::nonNull)
                        .map(kanjiParser::parse)
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
}
