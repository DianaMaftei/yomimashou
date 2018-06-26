package com.github.dianamaftei.yomimashou.controller;

import com.github.dianamaftei.yomimashou.model.NameEntry;
import com.github.dianamaftei.yomimashou.service.NameEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/names")
public class NameEntryController {

    private final NameEntryService nameEntryService;

    @Autowired
    public NameEntryController(NameEntryService nameEntryService) {
        this.nameEntryService = nameEntryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<NameEntry> get(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return nameEntryService.get(searchItem.split(","));
        }
        return Collections.emptyList();
    }
}
