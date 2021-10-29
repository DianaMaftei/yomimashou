package com.yomimashou.study.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(MockitoExtension.class)
class DeckControllerTest {

  private static final String API_URL_DECK = "/api/study/deck";
  private MockMvc mvc;

  @Mock
  private DeckService deckService;

  @InjectMocks
  private DeckController deckController;

  @Captor
  ArgumentCaptor<Deck> deckCaptor;

  @BeforeEach
  void setup() {
    mvc = MockMvcBuilders.standaloneSetup(deckController).build();
  }

  @Test
  void getAllShouldReturnAllDecks() throws Exception {
    // Arrange
    List<Deck> deckList = Arrays.asList(new Deck("deck1"), new Deck("deck2"));
    when(deckService.getAll()).thenReturn(deckList);

    // Act
    final MockHttpServletResponse response = mvc
        .perform(get(API_URL_DECK)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    // Assert
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString().indexOf("name\":\"deck1")).isGreaterThan(0);
    assertThat(response.getContentAsString().indexOf("name\":\"deck2")).isGreaterThan(0);
  }

  @Test
  void getShouldReturnDeckWithGivenId() throws Exception {
    // Arrange
    Long deckId = 1L;
    Deck deck = new Deck("deck1");
    deck.setId(deckId);
    when(deckService.findById(deckId)).thenReturn(deck);

    // Act
    final MockHttpServletResponse response = mvc
        .perform(get(API_URL_DECK + "/" + deckId)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    // Assert
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString().indexOf("name\":\"deck1")).isGreaterThan(0);
  }

  @Test
  void updateShouldThrowExceptionWhenIdInPathDoesNotMatchDeckId() throws Exception {
    // Arrange
    Long deckId = 1L;
    Deck deck = new Deck("deck1");
    deck.setId(2L);

    // Act
    final MockHttpServletResponse response = mvc
        .perform(put(API_URL_DECK + "/" + deckId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(deck)))
        .andReturn().getResponse();

    // Assert
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void updateShouldSaveTheDeck() throws Exception {
    // Arrange
    Long deckId = 1L;
    Deck deck = new Deck("deck1");
    deck.setId(deckId);

    // Act
    mvc.perform(put(API_URL_DECK + "/" + deckId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(deck)))
        .andReturn().getResponse();

    // Assert
    verify(deckService).save(deckCaptor.capture());
    Deck deckCaptorValue = deckCaptor.getValue();
    assertEquals(deck.getId(), deckCaptorValue.getId());
    assertEquals(deck.getName(), deckCaptorValue.getName());
  }

  @Test
  void deleteShouldDeleteTheDeck() throws Exception {
    // Arrange
    Long deckId = 1L;

    // Act
    mvc.perform(delete(API_URL_DECK + "/" + deckId))
        .andReturn().getResponse();

    // Assert
    verify(deckService).delete(deckId);
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}