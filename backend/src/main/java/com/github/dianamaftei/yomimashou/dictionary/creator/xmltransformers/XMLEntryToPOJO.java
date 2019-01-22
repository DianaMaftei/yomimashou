package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.DictionaryEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

@Component
public abstract class XMLEntryToPOJO {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLEntryToPOJO.class);

    @Value("${file.path}")
    private String filePath;

    protected String dictionarySource;
    protected String fileName;

    public void processEntries() {
        try {
            Optional<Object> unmarshalledFile = unmarshalFile(dictionarySource);
            unmarshalledFile.ifPresent(dictionaryFile -> {
                List<? extends DictionaryEntry> dictionaryEntries = getEntries(dictionaryFile);
                saveToFile(dictionaryEntries);
                saveToDB(dictionaryEntries);
            });
        } catch (JAXBException e) {
            LOGGER.error("could not process entries from XML", e);
        }
    }

    private Optional<Object> unmarshalFile(String url) throws JAXBException {
        try (InputStream is = new URL(url).openStream();
             InputStream gis = new GZIPInputStream(is)) {
            JAXBContext jaxbContext = JAXBContext.newInstance(getClassForJaxb());

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Object unmarshal = jaxbUnmarshaller.unmarshal(gis);
            return Optional.of(unmarshal);

        } catch (IOException e) {
            LOGGER.error("could not unmarshal file: " + url, e);
        }

        return Optional.empty();
    }

    void writeToFile(Set<String> entries, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath + File.separator + "dictionaries" + File.separator + filename)))) {
            writer.write(entries.stream().collect(Collectors.joining("|")));
        } catch (IOException e) {
            LOGGER.error("could not write entries to file " + filename, e);
        }
    }

    abstract List<? extends DictionaryEntry> getEntries(Object dictionaryFile);

    abstract Class getClassForJaxb();

    abstract void saveToFile(List<? extends DictionaryEntry> entries);

    abstract void saveToDB(List<? extends DictionaryEntry> entries);

    void setDictionarySource(String dictionarySource) {
        this.dictionarySource = dictionarySource;
    }
}
