package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.DictionaryEntry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.Entry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.JMnedict;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.KEle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.NameType;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.REle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.Trans;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.TransDet;
import com.github.dianamaftei.yomimashou.dictionary.name.Name;
import com.github.dianamaftei.yomimashou.dictionary.name.NameRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NameEntriesFromXMLToPOJO extends XMLEntryToPOJO {

    private static final Logger LOGGER = LoggerFactory.getLogger(NameEntriesFromXMLToPOJO.class);

    private final NameRepository nameRepository;

    @Autowired
    public NameEntriesFromXMLToPOJO(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
        this.dictionarySource = "http://ftp.monash.edu/pub/nihongo/JMnedict.xml.gz";
        this.fileName = "nameEntries.txt";
    }

    @Override
    void saveToDb(List<? extends DictionaryEntry> entries) {
        ((List<Entry>) entries).parallelStream().filter(Objects::nonNull).forEach(name -> {
            Name nameEntry = buildNameEntry(name);
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
                    .map(this::buildNameEntry)
                    .map(nameEntry -> Arrays.asList(nameEntry.getKanji(), nameEntry.getReading()))
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());

            writeToFile(nameEntries, fileName);
        } catch (Exception e) {
            LOGGER.error("Could not save to file: " + fileName, e);
        }
    }

    Name buildNameEntry(Entry name) {
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
                    List<String> nameTypeList = translationElement.getNameType()
                        .stream().map(NameType::getvalue).collect(Collectors.toList());
                    List<String> transList = translationElement.getTransDet()
                        .stream().map(TransDet::getvalue).collect(Collectors.toList());

                    nameEntry.setType(String.join("|", nameTypeList));
                    nameEntry.setTranslations(String.join("|", transList));
                    break;
            }
        }

        return nameEntry;
    }
}
