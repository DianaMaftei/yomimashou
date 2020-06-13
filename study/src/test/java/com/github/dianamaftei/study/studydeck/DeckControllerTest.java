package com.github.dianamaftei.study.studydeck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dianamaftei.study.BLValidationException;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class DeckControllerTest {

  private static final String API_URL = "/api/deck";
  private static final String DECK_ID_URL = API_URL + "/{deckId}";
  private static final String ADD_CARD_EXISTING_DECK_URL = DECK_ID_URL + "/addCard";
  private static final String ADD_CARD_NEW_DECK_URL = API_URL + "/addCard";

  private static final String DECK_NAME = "deck name";
  private static final String KANJI = "日";
  private static final String KANA = "ひ、 -び、 -か ニチ、 ジツ";
  private static final String EXPLANATION = "day, sun, Japan, counter for days";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MongoTemplate mongoTemplate;

  @BeforeEach
  void setUp() {
    mongoTemplate.remove(new Query(), Deck.class);
    mongoTemplate.remove(new Query(), Card.class);
  }

  @Test
  void viewDecksReturnsAllTheDecks() throws Exception {
    for (int i = 0; i < 5; i++) {
      insertADeckInDb(DECK_NAME + i);
    }

    MvcResult mvcResult = mockMvc.perform(get(API_URL)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    String resultContent = mvcResult.getResponse().getContentAsString();

    assertThat(resultContent).isNotNull();
    assertThat(resultContent.indexOf("\"numberOfElements\":5")).isGreaterThan(0);
    assertThat(resultContent.indexOf("\"first\":true")).isGreaterThan(0);
    assertThat(resultContent.indexOf("\"last\":true")).isGreaterThan(0);
  }

  @Test
  void viewDecksPaginatesCorrectly() throws Exception {
    for (int i = 0; i < 10; i++) {
      insertADeckInDb(DECK_NAME + i);
    }

    MvcResult mvcResult = mockMvc.perform(get(API_URL)
        .param("page", "1")
        .param("size", "3")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    String resultContent = mvcResult.getResponse().getContentAsString();

    assertThat(resultContent.indexOf("\"numberOfElements\":3")).isGreaterThan(0);
    assertThat(resultContent.indexOf("\"first\":false")).isGreaterThan(0);
  }

  @Test
  void viewDecksSortsCorrectlyByNameDesc() throws Exception {
    for (int i = 0; i < 10; i++) {
      mockMvc.perform(post(API_URL)
          .content(asJsonString(buildDeckWithCards(DECK_NAME + i)))
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.APPLICATION_JSON));
    }

    MvcResult mvcResult = mockMvc.perform(get(API_URL)
        .param("sort", "name,desc")
        .param("page", "0")
        .param("size", "1")

        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    String resultContent = mvcResult.getResponse().getContentAsString();

    assertThat(resultContent).isNotNull();
    assertThat(resultContent.indexOf("\"name\":\"deck name9\"")).isGreaterThan(0);
  }

  @Test
  void createDeck() throws Exception {
    Deck deck = insertADeckInDb(DECK_NAME);
    assertThat(deck).isNotNull();
    assertEquals(DECK_NAME, deck.getName());
  }

  @Test
  void createDeckThrowsExceptionIfNameIsNotUnique() throws Exception {
    NestedServletException exception = Assertions.assertThrows(NestedServletException.class, () -> {
      insertADeckInDb(DECK_NAME);
      insertADeckInDb(DECK_NAME);
    });

    assertEquals(DuplicateKeyException.class, exception.getCause().getClass());
  }

  @Test
  void createDeckThrowsExceptionIfNameIsMissing() throws Exception {
    NestedServletException exception = Assertions.assertThrows(NestedServletException.class, () -> {
      insertADeckInDb(null);
    });

    assertEquals(DuplicateKeyException.class, exception.getCause().getClass());
  }

  @Test
  void viewDeck() throws Exception {
    // create a deck
    String suffix = "_BOO";
    Deck createdDeck = insertADeckInDb(DECK_NAME + suffix);

    MvcResult mvcGetResult = mockMvc.perform(get(DECK_ID_URL, createdDeck.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    String getResultContent = mvcGetResult.getResponse().getContentAsString();
    Deck fetchedDeck = (Deck) asObject(getResultContent, Deck.class);

    assertTrue(fetchedDeck.getName().contains(suffix));
  }

  @Test
  void editDeck() throws Exception {
    // create a deck
    String suffix = "_BOO";
    Deck createdDeck = insertADeckInDb(suffix);

    // change the deck name
    String newName = "new deck name";
    createdDeck.setName(newName);

    // update deck
    MvcResult mvcPutResult = mockMvc.perform(put(DECK_ID_URL, createdDeck.getId())
        .content(asJsonString(createdDeck))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    String putResultContent = mvcPutResult.getResponse().getContentAsString();
    Deck editedDeck = (Deck) asObject(putResultContent, Deck.class);

    assertEquals(newName, editedDeck.getName());
    assertFalse(editedDeck.getName().contains(suffix));
  }

  @Test
  void deleteDeckDeletesTheDeckWithTheGivenId() throws Exception {
    // create a deck
    Deck createdDeck = insertADeckInDb(DECK_NAME);

    //check that the deck is in the db
    MvcResult mvcGetResult = mockMvc.perform(get(DECK_ID_URL, createdDeck.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    assertTrue(StringUtils.isNotBlank(mvcGetResult.getResponse().getContentAsString()));

    // delete the deck
    mockMvc.perform(delete(DECK_ID_URL, createdDeck.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    // check that the deck is no longer in the db
    MvcResult mvcGetResultAfterDelete = mockMvc
        .perform(get(DECK_ID_URL, createdDeck.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    assertTrue(StringUtils.isBlank(mvcGetResultAfterDelete.getResponse().getContentAsString()));

    //TODO check that the cards are also deleted
  }

  @Test
  void addCardToExistingDeckShouldSuccessfullyAddCardToIt() throws Exception {
    // create a deck with two pre existing cards
    Deck createdDeck = insertADeckInDb(DECK_NAME);
    assertEquals(2, createdDeck.getCards().size());

    // create a new card
    Card card = buildCard(KANJI, KANA, EXPLANATION, CardItemOrigin.KANJI);

    // add card to deck
    MvcResult mvcResult = mockMvc.perform(post(ADD_CARD_EXISTING_DECK_URL, createdDeck.getId())
        .content(asJsonString(card))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    String getResultContent = mvcResult.getResponse().getContentAsString();
    Deck fetchedDeck = (Deck) asObject(getResultContent, Deck.class);

    // check that the card has been added
    assertEquals(3, fetchedDeck.getCards().size());
    Optional<Card> addedCard = fetchedDeck.getCards()
        .stream()
        .filter(deckCard -> KANJI.equals(deckCard.getKanji()))
        .findFirst();

    assertThat(addedCard).isNotEmpty();
  }

  @Test
  void addCardToNewDeckShouldCreateDeckAndSuccessfullyAddCardToIt() throws Exception {
    // create a new card to add to deck
    Card card = buildCard(KANJI, KANA, EXPLANATION, CardItemOrigin.KANJI);

    // add card to deck
    MvcResult mvcResult = mockMvc.perform(post(ADD_CARD_NEW_DECK_URL)
        .param("deckName", DECK_NAME)
        .content(asJsonString(card))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    String getResultContent = mvcResult.getResponse().getContentAsString();
    Deck fetchedDeck = (Deck) asObject(getResultContent, Deck.class);

    // check that the card has been added
    assertEquals(1, fetchedDeck.getCards().size());
    Optional<Card> addedCard = fetchedDeck.getCards()
        .stream()
        .filter(deckCard -> KANJI.equals(deckCard.getKanji()))
        .findFirst();

    assertThat(addedCard).isNotEmpty();
  }

  @Test
  void addCardToExistingDeckShouldThrowExceptionIfDeckDoesNotBelongToUser() throws Exception {
    //new list or existing list?
    // create a deck
    Deck createdDeck = insertADeckInDb(DECK_NAME);

    throw new RuntimeException("to implement");
  }

  @Test
  void addCardToExistingDeckShouldThrowExceptionIfDeckDoesNotExist() throws Exception {
    NestedServletException exception = Assertions.assertThrows(NestedServletException.class, () -> {
      // create a new card
      Card card = buildCard(KANJI, KANA, EXPLANATION, CardItemOrigin.KANJI);

      // add card to deck, giving incorrect id
      mockMvc.perform(post(ADD_CARD_EXISTING_DECK_URL, "bogusId")
          .content(asJsonString(card))
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest());
    });

    assertEquals(BLValidationException.class, exception.getCause().getClass());
  }

  @Test
  void addCardToNewDeckShouldThrowExceptionIfNewDeckNameIsNotUnique() throws Exception {
    NestedServletException exception = Assertions.assertThrows(NestedServletException.class, () -> {
      // create a new card
      Card card = buildCard(KANJI, KANA, EXPLANATION, CardItemOrigin.KANJI);

      // add card to new deck
      mockMvc.perform(post(ADD_CARD_NEW_DECK_URL)
          .param("deckName", DECK_NAME)
          .content(asJsonString(card))
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());

      // add card to new deck, giving same deck name
      mockMvc.perform(post(ADD_CARD_NEW_DECK_URL)
          .param("deckName", DECK_NAME)
          .content(asJsonString(card))
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest());
    });

    assertEquals(BLValidationException.class, exception.getCause().getClass());
  }

  @Test
  void removeCardFromDeckRemovesTheCardWithTheGivenId() throws Exception {
    // create a deck
    Deck createdDeck = insertADeckInDb(DECK_NAME);

    assertEquals(2, createdDeck.getCards().size());

    // remove a card
    mockMvc.perform(post(DECK_ID_URL + "/card/delete/{cardId}",
        createdDeck.getId(), createdDeck.getCards().get(0).getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // check the deck no longer contains the card
    MvcResult mvcGetResult = mockMvc.perform(get(DECK_ID_URL, createdDeck.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    String getResultContent = mvcGetResult.getResponse().getContentAsString();
    Deck editedDeck = (Deck) asObject(getResultContent, Deck.class);

    assertEquals(1, editedDeck.getCards().size());
  }

  private Deck insertADeckInDb(final String deckName) throws Exception {
    MvcResult mvcPostResult = mockMvc.perform(post(API_URL)
        .content(asJsonString(buildDeckWithCards(deckName)))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    String postResultContent = mvcPostResult.getResponse().getContentAsString();
    return (Deck) asObject(postResultContent, Deck.class);
  }

  private Deck buildDeckWithCards(final String deckName) {
    Deck deck = new Deck();
    deck.setName(deckName);
    Card card1 = buildCard(deckName + "_" + "猫", "ねこ ビョウ", "cat", CardItemOrigin.KANJI);
    Card card2 = buildCard(deckName + "_" + "犬", "いぬ、 いぬ- ケン", "dog", CardItemOrigin.KANJI);
    deck.setCards(Arrays.asList(card1, card2));
    return deck;
  }

  private Card buildCard(String kanji, String kana, String explanation, CardItemOrigin cardItemOrigin) {
    Card card = new Card();
    card.setKanji(kanji);
    card.setKana(kana);
    card.setExplanation(explanation);
    card.setCardItemOrigin(cardItemOrigin);
    return card;
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static Object asObject(final String objAsString, final Class objClass) {
    try {
      return new ObjectMapper().readValue(objAsString, objClass);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
