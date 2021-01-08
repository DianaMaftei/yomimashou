package com.yomimashou.study.studydeck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public void delete(Card card) {
        cardRepository.delete(card);
    }

    public Optional<Card> findById(String cardId) {
        return cardRepository.findById(cardId);
    }

    public List<Card> saveAll(List<Card> cards) {
        return mapIterableToList(cardRepository.saveAll(cards));
    }

    public List<Card> findAllById(List<String> cardIds) {
        return mapIterableToList(cardRepository.findAllById(cardIds));
    }

    private List<Card> mapIterableToList(Iterable<Card> cards) {
        List<Card> cardList = new ArrayList<>();
        cards.forEach(cardList::add);
        return cardList;
    }

    public void deleteById(String cardId) {
        cardRepository.deleteById(cardId);
    }

    public List<Card> findAllDueById(List<String> cardIds) {
        return cardRepository.findAllByIdInAndNextPracticeBefore(cardIds, LocalDateTime.now());
    }
}
