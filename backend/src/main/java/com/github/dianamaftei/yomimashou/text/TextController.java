package com.github.dianamaftei.yomimashou.text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/text")
public class TextController {

    private final TextParserService textParserService;
    private final TextService textService;

    @Autowired
    public TextController(TextParserService textParserService, TextService textService) {
        this.textParserService = textParserService;
        this.textService = textService;
    }

    @PostMapping()
    public Text add(@RequestBody Text text) {
        return this.textService.add(text);
    }

    @GetMapping()
    public List<Text> getAll() {
        List<Text> texts = this.textService.getAll();
        texts.forEach(text -> text.setContent(null));
        return texts;
    }

    @GetMapping("/{id}")
    public Text getById(@PathVariable Long id) {
        return this.textService.getById(id);
    }

    @PostMapping(value = "/parse/words")
    public Set<String> getWords(@RequestBody String text) {
        if (text != null && text.length() > 0) {
            return textParserService.parseWords(text);
        }
        return Collections.emptySet();
    }

    @PostMapping(value = "/parse/names")
    public Set<String> getNames(@RequestBody String text) {
        if (text != null && text.length() > 0) {
            return textParserService.parseNames(text);
        }
        return Collections.emptySet();
    }
}
