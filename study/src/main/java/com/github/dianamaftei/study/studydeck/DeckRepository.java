package com.github.dianamaftei.study.studydeck;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DeckRepository extends PagingAndSortingRepository<Deck, String> {

  @Query(value = "{}", fields = "{cards : 0}")
  Page<Deck> findAllAndExcludeCards(Pageable pageable);

}
