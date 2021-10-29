package com.yomimashou.study.card;

import com.yomimashou.study.BLValidationException;
import com.yomimashou.study.card.spacedrepetition.PracticeAlgorithm;
import com.yomimashou.study.deck.Deck;
import com.yomimashou.study.deck.DeckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private DeckService deckService;

    @Mock
    private PracticeAlgorithm practiceAlgorithm;

    @InjectMocks
    private CardService cardService;

    private Card card;
    private Deck deck;

    @BeforeEach
    void setUp() {
        card = CardTestHelper.buildCard("猫", "ねこ ビョウ", "cat", CardItemOrigin.KANJI);
        deck = new Deck("deck");
        deck.setId(2L);
    }

    @Test
    void findByIdShouldReturnCardIfItExists() {
        // Arrange
        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        // Act
        Card actualCard = cardService.findById(card.getId());

        // Assert
        assertEquals(card, actualCard);
    }

    @Test
    void findByIdShouldThrowExceptionIfIdIsInvalid() {
        // Arrange
        when(cardRepository.findById(card.getId())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BLValidationException.class, () -> {
            cardService.findById(card.getId());
        });
    }

    @Test
    void findAllDueByDeckIdShouldReturnAllCardsInDeckDueForReview() {
        // Arrange
        Long deckId = 2L;
        card.setNextPractice(LocalDateTime.now().minusDays(1L));
        when(cardRepository.findAllByDeckIdAndNextPracticeBefore(eq(deckId), any(LocalDateTime.class))).thenReturn(Arrays.asList(card));

        // Act
        List<Card> allCardsDueInDeck = cardService.findAllDueByDeckId(deckId);

        // Assert
        assertEquals(1, allCardsDueInDeck.size());
        assertEquals(card, allCardsDueInDeck.get(0));
    }

    @Test
    void calculateNextReviewShouldThrowExceptionIfCardIdIsInvalid() {
        // Arrange
        when(cardRepository.findById(card.getId())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BLValidationException.class, () -> {
            cardService.calculateNextReview(card.getId(), 1);
        });
    }

    @Test
    void calculateNextReviewShouldComputeNextReviewTimeAndUpdateCard() {
        // Arrange
        int easeOfAnswer = 1;
        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));
        Card cardWithComputedReview = new Card();
        BeanUtils.copyProperties(card, cardWithComputedReview);
        cardWithComputedReview.setNextPractice(LocalDateTime.now());
        when(practiceAlgorithm.calculateNextReview(card, easeOfAnswer)).thenReturn(cardWithComputedReview);
        when(cardRepository.save(cardWithComputedReview)).thenReturn(cardWithComputedReview);

        // Act
        Card updatedCard = cardService.calculateNextReview(this.card.getId(), easeOfAnswer);

        // Assert
        assertEquals(cardWithComputedReview, updatedCard);
    }

    @Test
    void addToExistingDeckAddsTheCardToTheExistingDeck() {
        // Arrange
        when(deckService.findById(deck.getId())).thenReturn(deck);
        when(cardRepository.save(card)).thenReturn(card);

        // Act
        Card actualCard = cardService.addToExistingDeck(deck.getId(), this.card);

        // Assert
        assertEquals(this.card, actualCard);
    }

    @Test
    void addToExistingDeckTrimsExplanationIfTooLong() {
        // Arrange
        card.setExplanation("111111111112222222222233333333333444444444445555555555556666666666777777777778888888888899999999999000000000001234567890");
        Long deckId = 2L;
        Deck deck = new Deck("deck");
        deck.setId(deckId);
        when(deckService.findById(deckId)).thenReturn(deck);
        when(cardRepository.save(card)).thenReturn(card);

        // Act
        Card actualCard = cardService.addToExistingDeck(deckId, this.card);

        // Assert
        assertEquals(this.card, actualCard);
        assertEquals("1111111111122222222222333333333334444444444455555555555566666666667777777777788888888888999999999990...", actualCard.getExplanation());
    }

    @Test
    void addToNewDeckThrowsExceptionIfDeckNameIsBlank() {
        // Act and Assert
        assertThrows(BLValidationException.class, () -> {
            cardService.addToNewDeck(" ", card);
        });
    }

    @Test
    void addToNewDeckThrowsExceptionIfDeckNameIsNull() {
        // Act and Assert
        assertThrows(BLValidationException.class, () -> {
            cardService.addToNewDeck(null, card);
        });
    }

    @Test
    void addToNewDeckShouldCreateANewDeckAndAddCardToIt() {
        // Arrange
        when(deckService.save(any(Deck.class))).thenReturn(deck);
        when(cardRepository.save(card)).thenReturn(card);

        // Act
        Card actualCard = cardService.addToNewDeck(deck.getName(), this.card);

        // Assert
        assertEquals(this.card, actualCard);
        assertEquals(actualCard.getDeck().getName(), deck.getName());
    }

    @Test
    void removeFromDeckShouldThrowExceptionIfCardIdDoesNotExist() {
        // Arrange
        when(cardRepository.findById(card.getId())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BLValidationException.class, () -> {
            cardService.removeFromDeck(card.getId());
        });
    }

    @Test
    void removeFromDeckShouldDeleteTheCard() {
        // Arrange
        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        // Act
        cardService.removeFromDeck(card.getId());

        // Assert
        verify(cardRepository).delete(card);
    }

    @Test
    void findAllInDeck() {
        // Arrange
        when(deckService.findById(deck.getId())).thenReturn(deck);
        when(cardRepository.findAllByDeck(deck)).thenReturn(Collections.singletonList(card));

        // Act
        List<Card> cardsInDeck = cardService.findAllInDeck(deck.getId());

        // Assert
        assertEquals(1, cardsInDeck.size());
        assertEquals(card, cardsInDeck.get(0));
    }

    @Test
    void deleteAll() {
        // Arrange
        List<Card> cardsToDelete = Collections.singletonList(card);

        // Act
        cardService.deleteAll(cardsToDelete);

        // Arrange
        verify(cardRepository).deleteAll(cardsToDelete);
    }
}
