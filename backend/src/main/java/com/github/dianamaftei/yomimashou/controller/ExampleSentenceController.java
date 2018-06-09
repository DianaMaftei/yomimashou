package com.github.dianamaftei.yomimashou.controller;

import com.github.dianamaftei.yomimashou.model.ExampleSentence;
import com.github.dianamaftei.yomimashou.service.ExampleSentenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/example")
public class ExampleSentenceController {

    private final ExampleSentenceService exampleSentenceService;

    @Autowired
    public ExampleSentenceController(ExampleSentenceService exampleSentenceService) {
        this.exampleSentenceService = exampleSentenceService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ExampleSentence> get(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return exampleSentenceService.get(searchItem);
        }
        return Collections.emptyList();
    }
}
