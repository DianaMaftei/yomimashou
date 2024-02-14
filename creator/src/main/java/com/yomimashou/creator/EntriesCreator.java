package com.yomimashou.creator;

import com.yomimashou.creator.dictionary.DictionaryService;
import com.yomimashou.creator.examplesentence.ExampleSentencesDictionary;
import com.yomimashou.creator.text.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class EntriesCreator {

    private final List<DictionaryService> dictionaryServiceList;
    private final ExampleSentencesDictionary exampleSentencesDictionary;
    private final List<Scraper> scraperList;

    public void run() {
        System.setProperty("jdk.xml.entityExpansionLimit", "0");
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");

        try {
            dictionaryServiceList.forEach(DictionaryService::processEntries);
            exampleSentencesDictionary.saveSentencesFromFileToDB();
            scraperList.forEach(Scraper::createContent);
        } catch (final Exception e) {
            log.error("Could not create entries", e);
        } finally {
            log.info("Finished");
        }
    }
}
