package com.yomimashou.creator.dictionary.name;

import com.yomimashou.appscommon.model.Name;
import com.yomimashou.creator.dictionary.DictionaryEntry;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.Entry;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.JMnedict;
import com.yomimashou.creator.dictionary.XMLEntryToPOJO;
import com.yomimashou.creator.dictionary.name.namecomponents.NameComponent;
import com.yomimashou.creator.dictionary.name.namecomponents.NameComponentType;
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
  private List<NameComponent> nameComponentsEnrichers;

  @Autowired
  public NameEntriesFromXMLToPOJO(final NameRepository nameRepository,
      @Value("${path.dictionary.name}") final String nameDictionaryPath,
      @Value("${path.name.entries}") final String nameEntriesPath,
      final List<NameComponent> nameComponentsEnrichers) {
    this.nameRepository = nameRepository;
    this.inputFile = nameDictionaryPath;
    this.outputFile = nameEntriesPath;
    this.nameComponentsEnrichers = nameComponentsEnrichers;
  }

  @Override
  protected void persist(final List<? extends DictionaryEntry> dictionaryEntries) {
    final List<Name> names = ((List<Entry>) dictionaryEntries).parallelStream()
        .filter(Objects::nonNull)
        .map(this::buildNameEntry)
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

  Name buildNameEntry(final Entry name) {
    final Name nameEntry = new Name();
    final List<Object> entSeqOrKEleOrREleOrTrans = name.getEntSeqOrKEleOrREleOrTrans();
    for (final Object component : entSeqOrKEleOrREleOrTrans) {
      final NameComponentType nameComponentType = NameComponentType
          .valueOf(component.getClass().getSimpleName().toUpperCase());

      nameComponentsEnrichers.stream()
          .filter(enricher -> enricher.applies(nameComponentType))
          .forEach(enricher -> enricher.enrich(nameEntry, component));
    }

    return nameEntry;
  }

  public List<NameComponent> getNameComponentsEnrichers() {
    return nameComponentsEnrichers;
  }

  public void setNameComponentsEnrichers(
      final List<NameComponent> nameComponentsEnrichers) {
    this.nameComponentsEnrichers = nameComponentsEnrichers;
  }
}
