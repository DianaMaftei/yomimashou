package com.yomimashou.study.deck;

import com.yomimashou.study.BLValidationException;
import com.yomimashou.study.card.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeckServiceTest {
    @InjectMocks
    private DeckService deckService;
    @Mock
    private DeckRepository deckRepository;
    @Mock
    private CardRepository cardRepository;


    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = Deck.builder().name("deck2").id(2L).build();
    }

    @Test
    void getAllShouldReturnAllDecks() {
        // Arrange
        List<Deck> decks = Collections.singletonList(deck);
        when(deckRepository.findAll()).thenReturn(decks);

        // Act
        List<Deck> actualDecks = deckService.getAll();

        // Assert
        assertEquals(decks, actualDecks);
    }

    @Test
    void saveShouldSaveTheDeck() {
        // Arrange
        when(deckRepository.save(deck)).thenReturn(deck);

        // Act
        Deck savedDeck = deckService.save(deck);

        // Assert
        assertEquals(deck, savedDeck);
    }

    @Test
    void findByIdShouldThrowExceptionIfDeckIdIsInvalid() {
        // Arrange
        when(deckRepository.findById(deck.getId())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BLValidationException.class, () -> {
            deckService.findById(deck.getId());
        });
    }

    @Test
    void findByIdShouldReturnDeckWithGivenId() {
        // Arrange
        when(deckRepository.findById(deck.getId())).thenReturn(Optional.of(deck));

        // Act
        Deck foundDeck = deckService.findById(deck.getId());

        // Assert
        assertEquals(deck, foundDeck);
    }

    @Test
    void deleteShouldRemoveDeckAndItsCards() {
        // Act
        deckService.delete(deck.getId());

        // Assert
        verify(cardRepository).deleteAllByDeckId(deck.getId());
        verify(deckRepository).deleteById(deck.getId());
    }
}
