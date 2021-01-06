package com.github.dianamaftei.yomimashou.dictionary.example;

import com.github.dianamaftei.appscommon.model.ExampleSentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/dictionary/examples")
public class ExampleSentenceController {

  private final ExampleSentenceService exampleSentenceService;

  @Autowired
  public ExampleSentenceController(final ExampleSentenceService exampleSentenceService) {
    this.exampleSentenceService = exampleSentenceService;
  }

  @GetMapping
  public List<ExampleSentence> get(@RequestParam("searchItem") final String searchItem, Pageable pageable) {
    if (searchItem.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing search item");
    }

    return exampleSentenceService.get(searchItem.split(","), pageable);
  }
}
