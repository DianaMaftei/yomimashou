package com.github.dianamaftei.study.studydeck.deck;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DeckRepository extends PagingAndSortingRepository<Deck, String> {

  @Query(value = "{}", fields = "{cards : 0}")
  Page<Deck> findAllAndExcludeCards(Pageable pageable);

  @Query(value = "{}", fields = "{cards : 0}")
  List<Deck> getAllDeckNames();
}
