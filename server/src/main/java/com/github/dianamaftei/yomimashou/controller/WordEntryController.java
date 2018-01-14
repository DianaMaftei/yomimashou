package com.github.dianamaftei.yomimashou.controller;

import com.github.dianamaftei.yomimashou.model.WordEntry;
import com.github.dianamaftei.yomimashou.service.WordEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/words")
public class WordEntryController {

    private final WordEntryService wordEntryService;

    @Autowired
    public WordEntryController(WordEntryService wordEntryService) {
        this.wordEntryService = wordEntryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<WordEntry> get(@RequestParam("word") String word) {
        if(word != null && word.length() > 0){
            return wordEntryService.get(word.split(","));
        }
        return null;
    }
}
