package com.github.dianamaftei.yomimashou.dictionary.creator.XMLToPOJOTransformers;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbGeneratedModels.DictionaryEntry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbGeneratedModels.JMnedict.*;
import com.github.dianamaftei.yomimashou.dictionary.name.Name;
import com.github.dianamaftei.yomimashou.dictionary.name.NameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NameEntriesFromXMLToPOJO extends XMLEntryToPOJO {

    private static final Logger LOGGER = LoggerFactory.getLogger(NameEntriesFromXMLToPOJO.class);

    private final NameRepository nameRepository;

    @Autowired
    public NameEntriesFromXMLToPOJO(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
        this.dictionarySource = "http://ftp.monash.edu/pub/nihongo/JMnedict.xml.gz";
    }

    @Override
    void fillDatabase(List<? extends DictionaryEntry> entries) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
        ((List<Entry>) entries).parallelStream().filter(Objects::nonNull).forEach(name -> {
            Name nameEntry = getEntry(name);
            nameRepository.save(nameEntry);
        });
    }

    @Override
    List<Entry> getEntries(Object dictionaryFile) {
        return ((JMnedict) dictionaryFile).getEntry();
    }

    @Override
    Class getClassForJaxb() {
        return JMnedict.class;
    }

    @Override
    void saveToFile(List<? extends DictionaryEntry> entries) {
        try {
            Set<String> nameEntries = ((List<Entry>) entries).parallelStream()
                    .filter(Objects::nonNull)
                    .map(this::getEntry)
                    .map(nameEntry -> Arrays.asList(nameEntry.getKanji(), nameEntry.getReading()))
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());

            writeToFile(nameEntries, "nameEntries.txt");
        } catch (Exception e) {
            LOGGER.error("Could not save to file nameEntries.txt", e);
        }

    }

    private Name getEntry(Entry name) {
        Name nameEntry = new Name();
        List<Object> entSeqOrKEleOrREleOrTrans = name.getEntSeqOrKEleOrREleOrTrans();
        for (Object component : entSeqOrKEleOrREleOrTrans) {
            String componentClass = component.getClass().getSimpleName();

            switch (componentClass) {
                case "KEle":
                    KEle kanjiElement = (KEle) component;
                    nameEntry.setKanji(kanjiElement.getKeb());
                    break;
                case "REle":
                    REle readingElement = (REle) component;
                    nameEntry.setReading(readingElement.getReb());
                    break;
                case "Trans":
                    Trans translationElement = (Trans) component;
                    List<String> nameTypeList = translationElement.getNameType().stream().map(NameType::getvalue).collect(Collectors.toList());
                    List<String> transList = translationElement.getTransDet().stream().map(TransDet::getvalue).collect(Collectors.toList());

                    nameEntry.setType(String.join("|", nameTypeList));
                    nameEntry.setTranslations(String.join("|", transList));
                    break;
            }
        }

        return nameEntry;
    }
}
