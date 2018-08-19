package com.github.dianamaftei.yomimashou.dictionary.creator;

import com.github.dianamaftei.yomimashou.dictionary.creator.XMLToPOJOTransformers.XMLEntryToPOJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntriesCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntriesCreator.class);

    private final List<XMLEntryToPOJO> xmlEntryToPOJOList;
    private final ExampleSentencesCSVtoPOJO exampleSentencesCSVtoPOJO;

    @Autowired
    public EntriesCreator(List<XMLEntryToPOJO> xmlEntryToPOJOList, ExampleSentencesCSVtoPOJO exampleSentencesCSVtoPOJO) {
        this.xmlEntryToPOJOList = xmlEntryToPOJOList;
        this.exampleSentencesCSVtoPOJO = exampleSentencesCSVtoPOJO;
    }

    public void run() {
        System.setProperty("jdk.xml.entityExpansionLimit", "0");

        try {
            xmlEntryToPOJOList.forEach(XMLEntryToPOJO::processEntries);
//            exampleSentencesCSVtoPOJO.saveSentencesFromFileToDB();
        } catch (Exception e) {
            LOGGER.error("could not create entries", e);
        }
    }
}
