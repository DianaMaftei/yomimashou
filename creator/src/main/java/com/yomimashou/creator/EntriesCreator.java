package com.yomimashou.creator;

import com.yomimashou.creator.examplesentence.ExampleSentencesCSVtoPOJO;
import com.yomimashou.creator.scraper.Scraper;
import com.yomimashou.creator.xmltransformers.XMLEntryToPOJO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntriesCreator {

  private static final Logger LOGGER = LoggerFactory.getLogger(EntriesCreator.class);

  private final List<XMLEntryToPOJO> xmlEntryToPOJOList;
  private final ExampleSentencesCSVtoPOJO exampleSentencesCSVtoPOJO;
  private final List<Scraper> scraperList;

  @Autowired
  public EntriesCreator(final List<XMLEntryToPOJO> xmlEntryToPOJOList,
                        final ExampleSentencesCSVtoPOJO exampleSentencesCSVtoPOJO, List<Scraper> scraperList) {
    this.xmlEntryToPOJOList = xmlEntryToPOJOList;
    this.exampleSentencesCSVtoPOJO = exampleSentencesCSVtoPOJO;
    this.scraperList = scraperList;
  }

  public void run() {
    System.setProperty("jdk.xml.entityExpansionLimit", "0");
    System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");

    try {
      xmlEntryToPOJOList.forEach(XMLEntryToPOJO::processEntries);
      exampleSentencesCSVtoPOJO.saveSentencesFromFileToDB();
      scraperList.forEach(Scraper::createContent);

    } catch (final Exception e) {
      LOGGER.error("could not create entries", e);
    } finally {
      LOGGER.info("Finished");
    }
  }
}
