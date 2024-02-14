package com.yomimashou.creator.dictionary;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.zip.GZIPInputStream;

@Slf4j
public abstract class DictionaryService {

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
            log.error("could not process entries from XML", e);
        }
    }

    protected abstract void persist(final List<? extends DictionaryEntry> dictionaryEntries);

    private Optional<Object> unmarshalFile(final String file) throws JAXBException {
        try (final InputStream is = new FileInputStream(file); final InputStream gis = new GZIPInputStream(is)) {
            final JAXBContext jaxbContext = JAXBContext.newInstance(getClassForJaxb());

            final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            final Object unmarshal = jaxbUnmarshaller.unmarshal(gis);
            return Optional.of(unmarshal);

        } catch (final IOException e) {
            log.error("could not unmarshal file: " + file, e);
        }

        return Optional.empty();
    }

    public void writeToFile(final Set<String> entries, final String filename) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)))) {
            writer.write(String.join("|", entries));
        } catch (final IOException e) {
            log.error("could not write entries to file " + filename, e);
        }
    }

    protected abstract List<? extends DictionaryEntry> getEntries(Object dictionaryFile);

    protected abstract Class getClassForJaxb();
}
