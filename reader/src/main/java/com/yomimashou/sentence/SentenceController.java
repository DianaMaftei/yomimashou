package com.yomimashou.sentence;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sentence")
public class SentenceController {

  private final SentenceService sentenceService;

  @Autowired
  public SentenceController(SentenceService sentenceService) {
    this.sentenceService = sentenceService;
  }

  @PostMapping()
  public List<SentenceToken> analyze(@RequestBody String sentence) {
    return this.sentenceService.analyze(sentence);
  }
}
