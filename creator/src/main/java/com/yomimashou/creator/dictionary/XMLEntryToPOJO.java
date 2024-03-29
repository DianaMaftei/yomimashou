package com.yomimashou.creator.dictionary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public abstract class XMLEntryToPOJO {

  private static final Logger LOGGER = LoggerFactory.getLogger(XMLEntryToPOJO.class);

  protected String inputFile;
  protected String outputFile;

  public void processEntries() {
    try {
      final Optional<Object> unmarshalledFile = unmarshalFile(inputFile);
      unmarshalledFile.ifPresent(dictionaryFile -> {
        final List<? extends DictionaryEntry> dictionaryEntries = getEntries(dictionaryFile);
        persist(dictionaryEntries);
      });
    } catch (final JAXBException e) {
      LOGGER.error("could not process entries from XML", e);
    }
  }

  protected abstract void persist(final List<? extends DictionaryEntry> dictionaryEntries);

  private Optional<Object> unmarshalFile(final String file) throws JAXBException {
    try (final InputStream is = new FileInputStream(file);
        final InputStream gis = new GZIPInputStream(is)) {
      final JAXBContext jaxbContext = JAXBContext.newInstance(getClassForJaxb());

      final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      final Object unmarshal = jaxbUnmarshaller.unmarshal(gis);
      return Optional.of(unmarshal);

    } catch (final IOException e) {
      LOGGER.error("could not unmarshal file: " + file, e);
    }

    return Optional.empty();
  }

  public void writeToFile(final Set<String> entries, final String filename) {
    try (final BufferedWriter writer = new BufferedWriter(new FileWriter(
        new File(filename)))) {
      writer.write(entries.stream().collect(Collectors.joining("|")));
    } catch (final IOException e) {
      LOGGER.error("could not write entries to file " + filename, e);
    }
  }

  protected abstract List<? extends DictionaryEntry> getEntries(Object dictionaryFile);

  protected abstract Class getClassForJaxb();
}
