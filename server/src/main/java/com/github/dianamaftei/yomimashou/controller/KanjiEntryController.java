package com.github.dianamaftei.yomimashou.controller;

import com.github.dianamaftei.yomimashou.model.KanjiEntry;
import com.github.dianamaftei.yomimashou.service.KanjiEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/kanji")
public class KanjiEntryController {

    private final KanjiEntryService kanjiEntryService;

    @Autowired
    public KanjiEntryController(KanjiEntryService kanjiEntryService) {
        this.kanjiEntryService = kanjiEntryService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public KanjiEntry getById(@PathVariable Long id) {
        return kanjiEntryService.get(id);
    }
}
