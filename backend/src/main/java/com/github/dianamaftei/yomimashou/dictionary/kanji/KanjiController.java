package com.github.dianamaftei.yomimashou.dictionary.kanji;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary/kanji")
public class KanjiController {

    private final KanjiService kanjiService;

    @Autowired
    public KanjiController(KanjiService kanjiService) {
        this.kanjiService = kanjiService;
    }

    @GetMapping
    public Kanji get(@RequestParam("searchItem") String searchItem) {
        return kanjiService.get(searchItem);
    }

    @GetMapping(value = "/svg/{kanji}", produces = "image/svg+xml")
    public byte[] getStrokesSVG(@PathVariable String kanji) {
        return kanjiService.getStrokesSVG(kanji);
    }
}
