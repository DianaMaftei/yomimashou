package com.yomimashou.creator.dictionary.word;

import com.yomimashou.appscommon.model.Word;
import com.yomimashou.creator.config.CreatorProperties;
import com.yomimashou.creator.dictionary.DictionaryEntry;
import com.yomimashou.creator.dictionary.DictionaryService;
import com.yomimashou.creator.dictionary.word.jmdictXMLmodels.Entry;
import com.yomimashou.creator.dictionary.word.jmdictXMLmodels.JMdict;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class WordDictionaryService extends DictionaryService {

    private WordRepository wordRepository;
    private WordParser wordParser;
    private CreatorProperties creatorProperties;

    @PostConstruct
    private void postConstruct() {
        this.inputFile = creatorProperties.getWordDictionaryPath();
        this.outputFile = creatorProperties.getWordEntriesPath();
    }

    @Override
    protected void persist(final List<? extends DictionaryEntry> entries) {
        final List<Word> words = ((List<Entry>) entries).parallelStream()
                .filter(Objects::nonNull)
                .map(wordParser::parse)
                .collect(Collectors.toList());

        final Set<String> wordKanjiAndReadings = words.parallelStream()
                .filter(entry -> entry.getKanjiElements() != null || entry.getReadingElements() != null)
                .map(entry -> Arrays.asList(
                        entry.getKanjiElements(),
                        entry.getReadingElements())
                ).flatMap(List::stream)
                .flatMap(Set::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        wordRepository.saveAll(words);
        writeToFile(wordKanjiAndReadings, outputFile);
    }

    @Override
    protected List<Entry> getEntries(final Object dictionaryFile) {
        return ((JMdict) dictionaryFile).getEntry();
    }

    @Override
    protected Class getClassForJaxb() {
        return JMdict.class;
    }
}
