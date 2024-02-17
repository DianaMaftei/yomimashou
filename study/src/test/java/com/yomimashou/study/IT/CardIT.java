package com.yomimashou.study.IT;

import com.yomimashou.study.BLValidationException;
import com.yomimashou.study.card.Card;
import com.yomimashou.study.card.CardItemOrigin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan("com.yomimashou")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CardIT {

    private static final String LOCALHOST = "http://localhost:";
    private static final String CARD_URL = "/api/study/card";
    private static final String DECK_NAME = "kanji";
    private String uri;
    private Card card1;
    private Card card2;

    @LocalServerPort
    private int port;

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @BeforeEach
    void setUp() {
        uri = LOCALHOST + port + CARD_URL;
        card1 = Card.builder().kanji("猫").kana("ねこ ビョウ").explanation("cat").cardItemOrigin(CardItemOrigin.KANJI).build();
        card2 = Card.builder().kanji("犬").kana("いぬ、 いぬ- ケン").explanation("dog").cardItemOrigin(CardItemOrigin.KANJI).build();
    }

    @Test
    void testAddCardToNewDeck() {
        // Add card1 to new deck
        String addCardUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/add")
                .queryParam("deckName", DECK_NAME)
                .encode()
                .toUriString();
        Card savedCard1 = testRestTemplate.postForEntity(addCardUri, card1, Card.class).getBody();

        // Assert card1 was saved and a deck was created
        assertEquals(card1.getKanji(), savedCard1.getKanji());
        assertNotNull(savedCard1.getDeck().getId());
        assertEquals(DECK_NAME, savedCard1.getDeck().getName());
    }

    @Test
    void testAddCardToExistingDeck() {
        // Add card1 to new deck
        String addCardUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/add")
                .queryParam("deckName", DECK_NAME)
                .encode()
                .toUriString();
        Card savedCard1 = testRestTemplate.postForEntity(addCardUri, card1, Card.class).getBody();

        // Assert card1 was saved and a deck was created
        assertEquals(card1.getKanji(), savedCard1.getKanji());
        assertNotNull(savedCard1.getDeck().getId());
        assertEquals(DECK_NAME, savedCard1.getDeck().getName());

        // Add card to existing deck (card1's deck)
        Card savedCard2 = testRestTemplate.postForEntity(uri + "/add/" + savedCard1.getDeck().getId(), card2, Card.class).getBody();

        // Assert card2 was saved and linked to previously created deck
        assertEquals(card2.getKanji(), savedCard2.getKanji());
        assertNotNull(savedCard2.getDeck().getId());
        assertEquals(savedCard1.getDeck().getId(), savedCard2.getDeck().getId());
    }

    @Test
    void testGetAllCardsInDeck() {
        // Add card to new deck
        String addCardUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/add")
                .queryParam("deckName", DECK_NAME)
                .encode()
                .toUriString();
        Card  savedCard = testRestTemplate.postForEntity(addCardUri, card1, Card.class).getBody();

        // Assert that a card has been created
        String getAllInDeckUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/")
                .queryParam("deckId", savedCard.getDeck().getId())
                .encode()
                .toUriString();
        Card[] cards = testRestTemplate.getForEntity(getAllInDeckUri, Card[].class).getBody();
        assertEquals(1, cards.length);
    }

    @Test
    void testReviewCard() {
        // Add card1 to new deck
        String addCardUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/add")
                .queryParam("deckName", DECK_NAME)
                .encode()
                .toUriString();
        Card savedCard1 = testRestTemplate.postForEntity(addCardUri, card1, Card.class).getBody();

        // Calculate next review for card 1
        String reviewUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/review")
                .queryParam("id", savedCard1.getId().toString())
                .queryParam("easeOfAnswer", "3")
                .encode()
                .toUriString();
        testRestTemplate.put(reviewUri, Card.class);
    }

    @Test
    void testGetAllCardsDue() {
        // Add card1 to new deck
        String addCardUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/add")
                .queryParam("deckName", DECK_NAME)
                .encode()
                .toUriString();
        Card savedCard1 = testRestTemplate.postForEntity(addCardUri, card1, Card.class).getBody();

        // Add card to existing deck (card1's deck)
        Card savedCard2 = testRestTemplate.postForEntity(uri + "/add/" + savedCard1.getDeck().getId(), card2, Card.class).getBody();

        // Review card 1
        String reviewUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/review")
                .queryParam("id", savedCard2.getId().toString())
                .queryParam("easeOfAnswer", "3")
                .encode()
                .toUriString();
        testRestTemplate.put(reviewUri, savedCard1, Card.class);

        // Get all cards due
        String getDueInDeckUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/due")
                .queryParam("deckId", savedCard2.getDeck().getId().toString())
                .encode()
                .toUriString();
        Card[] cardsDue = testRestTemplate.getForEntity(getDueInDeckUri, Card[].class).getBody();

        // Assert that one card is due, the one that we have not reviewed
        assertEquals(1, cardsDue.length);
        assertEquals(savedCard2.getId(), cardsDue[0].getId());
    }

    @Test
    void testRemoveCardFromDeck() {
        // Add card1 to new deck
        String addCardUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/add")
                .queryParam("deckName", DECK_NAME)
                .encode()
                .toUriString();
        Card savedCard = testRestTemplate.postForEntity(addCardUri, card1, Card.class).getBody();

        // Assert that a card has been created
        String getAllInDeckUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/")
                .queryParam("deckId", savedCard.getDeck().getId())
                .encode()
                .toUriString();
        Card[] cards = testRestTemplate.getForEntity(getAllInDeckUri, Card[].class).getBody();
        assertEquals(1, cards.length);

        // Remove a card from the deck
        testRestTemplate.delete(uri + "/" + savedCard.getId());

        // Assert that deck no longer exists
        Card[] newCards = testRestTemplate.getForEntity(getAllInDeckUri, Card[].class).getBody();
        assertEquals(1, cards.length);
    }

}
