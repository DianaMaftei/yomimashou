package com.yomimashou.creator;

import com.yomimashou.creator.dictionary.XMLEntryToPOJO;
import com.yomimashou.creator.examplesentence.ExampleSentencesCSVtoPOJO;
import com.yomimashou.creator.text.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class EntriesCreator {

    private final List<XMLEntryToPOJO> xmlEntryToPOJOList;
    private final ExampleSentencesCSVtoPOJO exampleSentencesCSVtoPOJO;
    private final List<Scraper> scraperList;

    public void run() {
        System.setProperty("jdk.xml.entityExpansionLimit", "0");
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");

        try {
            xmlEntryToPOJOList.forEach(XMLEntryToPOJO::processEntries);
            exampleSentencesCSVtoPOJO.saveSentencesFromFileToDB();
            scraperList.forEach(Scraper::createContent);
        } catch (final Exception e) {
            log.error("Could not create entries", e);
        } finally {
            log.info("Finished");
        }
    }
}
