package com.github.dianamaftei.yomimashou.dictionary.name;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/names")
@CrossOrigin
public class NameController {

    private final NameService nameService;

    @Autowired
    public NameController(NameService nameService) {
        this.nameService = nameService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Name> get(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return nameService.get(searchItem.split(","));
        }
        return Collections.emptyList();
    }
}
