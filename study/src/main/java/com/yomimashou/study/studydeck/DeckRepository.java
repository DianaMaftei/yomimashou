package com.yomimashou.study.studydeck;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DeckRepository extends PagingAndSortingRepository<Deck, String> {

  @Query(value = "{}", fields = "{cards : 0}")
  List<Deck> getAllDeckNames();
}
