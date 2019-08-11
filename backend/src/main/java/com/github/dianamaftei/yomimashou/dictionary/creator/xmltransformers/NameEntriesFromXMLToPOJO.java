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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NameEntriesFromXMLToPOJO extends XMLEntryToPOJO {

  private static final Logger LOGGER = LoggerFactory.getLogger(NameEntriesFromXMLToPOJO.class);

  private final NameRepository nameRepository;

  @Autowired
  public NameEntriesFromXMLToPOJO(final NameRepository nameRepository,
      @Value("${path.dictionary.name}") final String nameDictionaryPath,
      @Value("${path.name.entries}") final String nameEntriesPath) {
    this.nameRepository = nameRepository;
    this.inputFile = nameDictionaryPath;
    this.outputFile = nameEntriesPath;
  }

  @Override
  void saveToDb(final List<? extends DictionaryEntry> entries) {
    ((List<Entry>) entries).parallelStream()
        .filter(Objects::nonNull)
        .map(this::buildNameEntry)
        .forEach(nameRepository::save);
  }

  @Override
  List<Entry> getEntries(final Object dictionaryFile) {
    return ((JMnedict) dictionaryFile).getEntry();
  }

  @Override
  Class getClassForJaxb() {
    return JMnedict.class;
  }

  @Override
  void saveToFile(final List<? extends DictionaryEntry> entries) {
    try {
      final Set<String> nameEntries = ((List<Entry>) entries).parallelStream()
          .filter(Objects::nonNull)
          .map(this::buildNameEntry)
          .map(nameEntry -> Arrays.asList(nameEntry.getKanji(), nameEntry.getReading()))
          .flatMap(List::stream)
          .filter(Objects::nonNull)
          .collect(Collectors.toSet());

      writeToFile(nameEntries, outputFile);
    } catch (final Exception e) {
      LOGGER.error("Could not save to file: " + outputFile, e);
    }
  }

  Name buildNameEntry(final Entry name) {
    final Name nameEntry = new Name();
    final List<Object> entSeqOrKEleOrREleOrTrans = name.getEntSeqOrKEleOrREleOrTrans();
    for (final Object component : entSeqOrKEleOrREleOrTrans) {
      final String componentClass = component.getClass().getSimpleName();

      switch (componentClass) {
        case "KEle":
          final KEle kanjiElement = (KEle) component;
          nameEntry.setKanji(kanjiElement.getKeb());
          break;
        case "REle":
          final REle readingElement = (REle) component;
          nameEntry.setReading(readingElement.getReb());
          break;
        case "Trans":
          final Trans translationElement = (Trans) component;
          final List<String> nameTypeList = translationElement.getNameType()
              .stream().map(NameType::getvalue).collect(Collectors.toList());
          final List<String> transList = translationElement.getTransDet()
              .stream().map(TransDet::getvalue).collect(Collectors.toList());

          nameEntry.setType(String.join("|", nameTypeList));
          nameEntry.setTranslations(String.join("|", transList));
          break;
      }
    }

    return nameEntry;
  }
}
