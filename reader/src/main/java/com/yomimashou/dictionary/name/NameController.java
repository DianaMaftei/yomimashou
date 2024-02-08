package com.yomimashou.dictionary.name;

import com.yomimashou.appscommon.model.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary/names")
public class NameController {

  private final NameService nameService;

  @Autowired
  public NameController(final NameService nameService) {
    this.nameService = nameService;
  }

  @GetMapping
  public Page<Name> get(@RequestParam("searchItem") final String searchItem,
      final Pageable pageable) {
    if (searchItem.length() > 0) {
      return nameService.getByReadingElemOrKanjiElem(searchItem.split(","), pageable);
    }
    return Page.empty();
  }
}
