package com.github.dianamaftei.yomimashou.dictionary.creator;

import com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.XMLEntryToPOJO;
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

  @Autowired
  public EntriesCreator(final List<XMLEntryToPOJO> xmlEntryToPOJOList,
      final ExampleSentencesCSVtoPOJO exampleSentencesCSVtoPOJO) {
    this.xmlEntryToPOJOList = xmlEntryToPOJOList;
    this.exampleSentencesCSVtoPOJO = exampleSentencesCSVtoPOJO;
  }

  public void run() {
    System.setProperty("jdk.xml.entityExpansionLimit", "0");
    System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");

    try {
      xmlEntryToPOJOList.forEach(XMLEntryToPOJO::processEntries);
      exampleSentencesCSVtoPOJO.saveSentencesFromFileToDB();
    } catch (final Exception e) {
      LOGGER.error("could not create entries", e);
    }
  }
}
