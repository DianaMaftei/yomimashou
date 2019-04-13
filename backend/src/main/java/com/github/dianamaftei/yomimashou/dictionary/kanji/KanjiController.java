package com.github.dianamaftei.yomimashou.dictionary.kanji;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;

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

    @ResponseBody
    @GetMapping(value = "/svg/{kanji}", produces = MediaType.APPLICATION_SVG_XML)
    public byte[] getStrokesSVG(@PathVariable String kanji) {
        return kanjiService.getStrokesSVG(kanji);
    }
}
