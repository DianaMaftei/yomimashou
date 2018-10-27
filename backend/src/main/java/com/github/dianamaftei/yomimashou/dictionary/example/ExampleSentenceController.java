package com.github.dianamaftei.yomimashou.dictionary.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/dictionary/examples")
@CrossOrigin
public class ExampleSentenceController {

    private final ExampleSentenceService exampleSentenceService;

    @Autowired
    public ExampleSentenceController(ExampleSentenceService exampleSentenceService) {
        this.exampleSentenceService = exampleSentenceService;
    }

    @GetMapping
    public List<ExampleSentence> get(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return exampleSentenceService.get(searchItem.split(","));
        }
        return Collections.emptyList();
    }
}
