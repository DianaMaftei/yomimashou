package com.yomimashou.study.card;

import com.yomimashou.study.deck.Deck;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CardRepository extends PagingAndSortingRepository<Card, Long> {

    List<Card> findAllByDeckIdAndNextPracticeBefore(Long deckId, LocalDateTime nextPractice);

    List<Card> findAllByDeck(Deck deck);
    void deleteAllByDeckId(Long deckId);
}
