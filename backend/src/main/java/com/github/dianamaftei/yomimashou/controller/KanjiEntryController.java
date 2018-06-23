package com.github.dianamaftei.yomimashou.controller;

import com.github.dianamaftei.yomimashou.model.KanjiEntry;
import com.github.dianamaftei.yomimashou.service.KanjiEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.ws.rs.core.MediaType;
@RestController
@RequestMapping("/api/kanji")
public class KanjiEntryController {

    private final KanjiEntryService kanjiEntryService;

    @Autowired
    public KanjiEntryController(KanjiEntryService kanjiEntryService) {
        this.kanjiEntryService = kanjiEntryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public KanjiEntry get(@RequestParam("searchItem") String searchItem) {
        return kanjiEntryService.get(searchItem);
    }

    @ResponseBody
    @GetMapping(value = "/svg/{kanji}", produces = MediaType.APPLICATION_SVG_XML)
    public byte[] getKanjiPath(@PathVariable String kanji) {
        return kanjiEntryService.getKanjiSVG(kanji);
    }
}
