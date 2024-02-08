package com.yomimashou.study.IT;

import com.yomimashou.study.deck.Deck;
import com.yomimashou.study.deck.DeckRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DeckIT {

    private static final String LOCALHOST = "http://localhost:";
    private static final String DECK_URL = "/api/study/deck";

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private DeckRepository deckRepository;

    @Test
    void crudWholeFlow() {
        // Arrange
        String uri = LOCALHOST + port + DECK_URL;
        String anotherDeckName = "another deck name";
        Deck deck1 = new Deck("words");
        deckRepository.save(deck1);
        Deck deck2 = new Deck("kanji");
        deckRepository.save(deck2);
        Deck deck3 = new Deck("sentences");
        deckRepository.save(deck3);

        // Fetch all decks
        Deck[] decks = restTemplate.getForEntity(uri, Deck[].class).getBody();

        // Assert that all existing decks have been fetched
        assertEquals(3, decks.length);

        // Fetch deck with given id
        Deck deck = restTemplate.getForEntity(uri + "/" + decks[0].getId(), Deck.class).getBody();

        // Assert that the fetched deck is the same as what was retrieved from the get all call
        assertEquals(decks[0], deck);

        // Update deck
        deck.setName(anotherDeckName);
        restTemplate.put(uri + "/" + deck.getId(), deck);

        // Assert that deck has been updated
        Deck updatedDeck = restTemplate.getForEntity(uri + "/" + deck.getId(), Deck.class).getBody();
        assertEquals(anotherDeckName, updatedDeck.getName());

        // Delete deck
        restTemplate.delete(uri + "/" + deck.getId());

        // Assert that the deck no longer exists
        ResponseEntity<Deck> entity = restTemplate.getForEntity(uri + "/" + deck.getId(), Deck.class);
        assertNull(entity.getBody().getId());
        assertNull(entity.getBody().getName());
    }
}
