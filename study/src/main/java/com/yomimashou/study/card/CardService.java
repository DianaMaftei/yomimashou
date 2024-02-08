package com.yomimashou.study.card;

import com.yomimashou.study.BLValidationException;
import com.yomimashou.study.card.spacedrepetition.PracticeAlgorithm;
import com.yomimashou.study.deck.Deck;
import com.yomimashou.study.deck.DeckService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CardService {

    private static final String ELLIPSIS = "...";
    public static final int MAX_EXPLANATION_LENGTH = 100;

    @Autowired
    private DeckService deckService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PracticeAlgorithm practiceAlgorithm;

    public Card findById(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new BLValidationException("Invalid card id"));
    }

    public List<Card> findAllDueByDeckId(Long deckId) {
        return cardRepository.findAllByDeckIdAndNextPracticeBefore(deckId, LocalDateTime.now());
    }

    public Card calculateNextReview(Long cardId, int easeOfAnswer) {
        Card card = findById(cardId);
        card = practiceAlgorithm.calculateNextReview(card, easeOfAnswer);
        return cardRepository.save(card);
    }

    public Card addToExistingDeck(final Long deckId, final Card card) {
        Deck deck = deckService.findById(deckId);
        return addCardToDeck(deck, card);
    }

    public Card addToNewDeck(final String deckName, final Card card) {
        if (StringUtils.isBlank(deckName)) {
            throw new BLValidationException("Invalid deck name");
        }
        Deck deck = new Deck(deckName);
        deck = deckService.save(deck);
        return addCardToDeck(deck, card);
    }

    private Card addCardToDeck(final Deck deck, final Card card) {
        card.setDeck(deck);
        if (!StringUtils.isBlank(card.getExplanation()) && card.getExplanation().length() > MAX_EXPLANATION_LENGTH) {
            card.setExplanation(trimToDesiredLength(card.getExplanation(), MAX_EXPLANATION_LENGTH));
        }
        return cardRepository.save(card);
    }

    private String trimToDesiredLength(String text, int desiredLength) {
        if (StringUtils.isBlank(text)) {
            return StringUtils.EMPTY;
        }
        String trimmedText = text.substring(0, desiredLength);
        return trimmedText + ELLIPSIS;
    }

    public void removeFromDeck(final Long cardId) {
        Card card = findById(cardId);
        cardRepository.delete(card);
    }

    public List<Card> findAllInDeck(Long deckId) {
        Deck deck = deckService.findById(deckId);
        return cardRepository.findAllByDeck(deck);
    }

    public void deleteAll(List<Card> cards) {
        cardRepository.deleteAll(cards);
    }
}
