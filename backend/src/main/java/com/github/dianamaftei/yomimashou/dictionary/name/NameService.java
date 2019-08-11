package com.github.dianamaftei.yomimashou.dictionary.name;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NameService {

  private final NameRepository nameRepository;

  @Autowired
  public NameService(final NameRepository nameRepository) {
    this.nameRepository = nameRepository;
  }

  public Page<Name> getByReadingElemOrKanjiElem(final String[] words, final Pageable pageable) {
    return nameRepository.findDistinctByKanjiInOrReadingIn(words, words, pageable);
  }
}
