package com.yomimashou.study.IT;

import com.yomimashou.study.card.Card;
import com.yomimashou.study.card.CardItemOrigin;
import com.yomimashou.study.card.CardTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CardIT {

    private static final String LOCALHOST = "http://localhost:";
    private static final String CARD_URL = "/api/study/card";
    public static final String DECK_NAME = "kanji";

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void crudWholeFlow() {
        // Arrange
        String uri = LOCALHOST + port + CARD_URL;
        Card card1 = CardTestHelper.buildCard("猫", "ねこ ビョウ", "cat", CardItemOrigin.KANJI);
        Card card2 = CardTestHelper.buildCard("犬", "いぬ、 いぬ- ケン", "dog", CardItemOrigin.KANJI);

        // Add card to new deck
        String addCardUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/add")
                .queryParam("deckName", DECK_NAME)
                .encode()
                .toUriString();
        Card savedCard1 = restTemplate.postForEntity(addCardUri, card1, Card.class).getBody();

        // Assert card1 was saved and a deck was created
        assertEquals(card1.getKanji(), savedCard1.getKanji());
        assertNotNull(savedCard1.getDeck().getId());
        assertEquals(DECK_NAME, savedCard1.getDeck().getName());

        // Add card to existing deck (card1's deck)
        Card savedCard2 = restTemplate.postForEntity(uri + "/add/" + savedCard1.getDeck().getId(), card2, Card.class).getBody();

        // Assert card2 was saved and linked to previously created deck
        assertEquals(card2.getKanji(), savedCard2.getKanji());
        assertNotNull(savedCard2.getDeck().getId());
        assertEquals(savedCard1.getDeck().getId(), savedCard2.getDeck().getId());

        // Assert that so far two cards have been created and
        String getAllInDeckUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/")
                .queryParam("deckId", savedCard1.getDeck().getId())
                .encode()
                .toUriString();
        Card[] cards = restTemplate.getForEntity(getAllInDeckUri, Card[].class).getBody();
        assertEquals(2, cards.length);

        // Calculate next review for card 1
        String reviewUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/review")
                .queryParam("id", savedCard1.getId().toString())
                .queryParam("easeOfAnswer", "3")
                .encode()
                .toUriString();
        restTemplate.put(reviewUri, Card.class);

        // Get all cards due
        String getDueInDeckUri = UriComponentsBuilder.fromHttpUrl(uri)
                .path("/due")
                .queryParam("deckId", savedCard1.getDeck().getId().toString())
                .encode()
                .toUriString();
        Card[] cardsDue = restTemplate.getForEntity(getDueInDeckUri, Card[].class).getBody();

        // Assert that one card is due, the one that we have not reviewed
        assertEquals(1, cardsDue.length);
        assertEquals(savedCard2.getId(), cardsDue[0].getId());

        // Remove a card from the deck
        restTemplate.delete(uri + "/" + savedCard1.getId());

        // Assert that only one card remains in the deck now
        Card[] remainingCards = restTemplate.getForEntity(getAllInDeckUri, Card[].class).getBody();
        assertEquals(1, remainingCards.length);
    }
}
