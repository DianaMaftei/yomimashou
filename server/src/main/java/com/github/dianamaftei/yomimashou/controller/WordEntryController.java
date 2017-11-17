package com.github.dianamaftei.yomimashou.controller;

import com.github.dianamaftei.yomimashou.model.WordEntry;
import com.github.dianamaftei.yomimashou.service.WordEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/words")
public class WordEntryController {

    private final WordEntryService wordEntryService;

    @Autowired
    public WordEntryController(WordEntryService wordEntryService) {
        this.wordEntryService = wordEntryService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public WordEntry getById(@PathVariable Long id) {

        return wordEntryService.get(id);
    }
}
