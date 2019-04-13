package com.github.dianamaftei.yomimashou.dictionary.name;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary/names")
public class NameController {

  private final NameService nameService;

  @Autowired
  public NameController(NameService nameService) {
    this.nameService = nameService;
  }

  @GetMapping
  public List<Name> get(@RequestParam("searchItem") String searchItem) {
    if (searchItem != null && searchItem.length() > 0) {
      return nameService.get(searchItem.split(","));
    }
    return Collections.emptyList();
  }
}
