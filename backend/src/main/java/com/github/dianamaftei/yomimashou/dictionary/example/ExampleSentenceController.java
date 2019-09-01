package com.github.dianamaftei.yomimashou.dictionary.example;

import com.github.dianamaftei.appscommon.model.ExampleSentence;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary/examples")
public class ExampleSentenceController {

  private final ExampleSentenceService exampleSentenceService;

  @Autowired
  public ExampleSentenceController(final ExampleSentenceService exampleSentenceService) {
    this.exampleSentenceService = exampleSentenceService;
  }

  @GetMapping
  public List<ExampleSentence> get(@RequestParam("searchItem") final String searchItem) {
    if (searchItem != null && searchItem.length() > 0) {
      return exampleSentenceService.get(searchItem.split(","));
    }
    return Collections.emptyList();
  }
}
