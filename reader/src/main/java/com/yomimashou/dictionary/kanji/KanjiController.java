package com.yomimashou.dictionary.kanji;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary/kanji")
public class KanjiController {

    private final KanjiService kanjiService;

    @Autowired
    public KanjiController(final KanjiService kanjiService) {
        this.kanjiService = kanjiService;
    }

  @GetMapping(value = "/svg/{kanji}", produces = "image/svg+xml")
    public byte[] getStrokesSVG(@PathVariable final String kanji) {
        return kanjiService.getStrokesSVG(kanji);
    }
}
