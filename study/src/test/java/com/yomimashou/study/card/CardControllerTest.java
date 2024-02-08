package com.yomimashou.study.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yomimashou.study.deck.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    private static final String API_URL_CARD = "/api/study/card";
    private MockMvc mvc;
    private Charset utf8 = Charset.forName("UTF-8");

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    private Card card;

    @BeforeEach
    void setup() {
        card = CardTestHelper.buildCard("猫", "ねこ ビョウ", "cat", CardItemOrigin.KANJI);
        card.setId(1L);
        mvc = MockMvcBuilders.standaloneSetup(cardController).build();
    }

    @Test
    void getAllInDeckShouldReturnAllCardsBelongingToGivenDeck() throws Exception {
        // Arrange
        Long deckId = 1L;
        Deck deck = new Deck("deck");
        deck.setId(deckId);
        card.setDeck(deck);
        Card card2 = CardTestHelper.buildCard("犬", "いぬ、 いぬ- ケン", "dog", CardItemOrigin.KANJI);
        card2.setDeck(deck);
        List<Card> cardsInDeck = Arrays.asList(card, card2);
        when(cardService.findAllInDeck(deckId)).thenReturn(cardsInDeck);

        // Act
        final MockHttpServletResponse response = mvc
                .perform(get(API_URL_CARD)
                        .param("deckId", deckId.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(utf8).indexOf("kanji\":\"猫")).isGreaterThan(0);
        assertThat(response.getContentAsString(utf8).indexOf("kanji\":\"犬")).isGreaterThan(0);
    }

    @Test
    void calculateNextReviewShouldCalculateWhenTheCardShouldComeUpNextForReview() throws Exception {
        // Arrange
        Integer easeOfAnswer = 1;
        card.setNextPractice(LocalDateTime.of(2021, 2, 1, 4, 7, 22));
        when(cardService.calculateNextReview(card.getId(), 1)).thenReturn(card);

        // Act
        final MockHttpServletResponse response = mvc
                .perform(put(API_URL_CARD + "/review")
                        .param("id", card.getId().toString())
                        .param("easeOfAnswer", easeOfAnswer.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(utf8).indexOf("nextPractice\":[2021,2,1,4,7,22]")).isGreaterThan(0);
    }

    @Test
    void getDueInDeckShouldReturnAllCardsBelongingToGivenDeckThatAreDueForReview() throws Exception {
        // Arrange
        Long deckId = 1L;
        Deck deck = new Deck("deck");
        deck.setId(deckId);
        card.setDeck(deck);
        card.setNextPractice(LocalDateTime.now());
        List<Card> cardsDueForReview = Collections.singletonList(card);
        when(cardService.findAllDueByDeckId(deckId)).thenReturn(cardsDueForReview);

        // Act
        final MockHttpServletResponse response = mvc
                .perform(get(API_URL_CARD + "/due")
                        .param("deckId", deckId.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(utf8).indexOf("kanji\":\"猫")).isGreaterThan(0);
    }

    @Test
    void addToNewDeckShouldAddCardToNewDeck() throws Exception {
        // Arrange
        String deckName = "deckName";
        when(cardService.addToNewDeck(eq(deckName), any(Card.class))).thenReturn(card);

        // Act
        final MockHttpServletResponse response = mvc
                .perform(post(API_URL_CARD + "/add")
                        .param("deckName", deckName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(card)))
                .andReturn().getResponse();

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(utf8).indexOf("kanji\":\"猫")).isGreaterThan(0);
    }

    @Test
    void addToExistingDeckShouldAddCardToExistingDeck() throws Exception {
        // Arrange
        Long deckId = 1L;
        when(cardService.addToExistingDeck(eq(deckId), any(Card.class))).thenReturn(card);

        // Act
        final MockHttpServletResponse response = mvc
                .perform(post(API_URL_CARD + "/add/" + deckId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(card)))
                .andReturn().getResponse();

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(utf8).indexOf("kanji\":\"猫")).isGreaterThan(0);
    }

    @Test
    void removeFromDeckShouldRemoveCardFromTheDeck() throws Exception {
        // Act
        mvc.perform(delete(API_URL_CARD + "/" + card.getId()))
                .andReturn().getResponse();

        // Assert
        verify(cardService).removeFromDeck(card.getId());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
