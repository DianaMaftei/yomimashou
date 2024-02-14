package com.yomimashou.creator.dictionary.name;

import com.yomimashou.appscommon.model.Name;
import com.yomimashou.creator.config.CreatorProperties;
import com.yomimashou.creator.dictionary.DictionaryEntry;
import com.yomimashou.creator.dictionary.DictionaryService;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.Entry;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.JMnedict;
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
public class NameDictionaryService extends DictionaryService {


    private CreatorProperties creatorProperties;
    private NameRepository nameRepository;
    private NameParser nameParser;

    @PostConstruct
    private void postConstruct() {
        this.inputFile = creatorProperties.getNameDictionaryPath();
        this.outputFile = creatorProperties.getNameEntriesPath();
    }

    @Override
    protected void persist(final List<? extends DictionaryEntry> dictionaryEntries) {
        final List<Name> names = ((List<Entry>) dictionaryEntries).parallelStream()
                .filter(Objects::nonNull)
                .map(nameParser::parse)
                .collect(Collectors.toList());

        final Set<String> nameKanjiAndReading = names.parallelStream()
                .map(nameEntry -> Arrays.asList(nameEntry.getKanji(), nameEntry.getReading()))
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        nameRepository.saveAll(names);
        writeToFile(nameKanjiAndReading, outputFile);
    }

    @Override
    protected List<Entry> getEntries(final Object dictionaryFile) {
        return ((JMnedict) dictionaryFile).getEntry();
    }

    @Override
    protected Class getClassForJaxb() {
        return JMnedict.class;
    }


}
