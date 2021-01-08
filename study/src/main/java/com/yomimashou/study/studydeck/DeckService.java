package com.yomimashou.study.studydeck;


import com.yomimashou.study.BLValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DeckService {

  @Autowired
  private CardService cardService;

  @Autowired
  private DeckRepository deckRepository;

  public Deck save(final String deckName, final List<Card> cards) {
    List<Card> filteredCards = cards.stream().filter(card -> card.getKanji() != null).collect(Collectors.toList());
    List<String> savedCardIds = cardService.saveAll(filteredCards).stream().map(Card::getId).collect(Collectors.toList());
    Deck deck = new Deck(deckName, savedCardIds);
    return deckRepository.save(deck);
  }

  public Deck update(final Deck deck) {
    return deckRepository.save(deck);
  }

  private void validateSave(final Deck deck) {
    // TODO check mandatory fields
    // TODO check that deck name is unique for user - where is the reference to the user?
  }

  public Page<Deck> findAll(final Pageable pageable) {
    return deckRepository.findAll(pageable);
  }

  public Optional<Deck> findById(final String id) {
    return deckRepository.findById(id);
  }

  public void delete(final String id) {
    Optional<Deck> deckOptional = deckRepository.findById(id);

    if (!deckOptional.isPresent()) {
      throw new BLValidationException("invalid deck id");
    }
    Deck deck = deckOptional.get();

    if (deck.getCards() != null) {
      List<Card> cards = cardService.findAllById(deck.getCards());
      cards.forEach(item -> cardService.delete(item));
    }
    deckRepository.deleteById(id);
  }

  public void removeCardFromDeck(final String id, final String cardId) {
    Optional<Deck> deckOptional = deckRepository.findById(id);

    validateDelete(deckOptional, cardId);

    deckOptional.get().getCards().remove(cardId);
    deckRepository.save(deckOptional.get());
    cardService.deleteById(cardId);
  }

  private void validateDelete(Optional<Deck> deckOptional, String cardId) {
    if (!deckOptional.isPresent()) {
      throw new BLValidationException("invalid deck id");
    }

    List<String> cardsIds = deckOptional.get().getCards();
    if (cardsIds.isEmpty()) {
      throw new BLValidationException("deck has no cards");
    }

    Optional<String> cardOptional = cardsIds.stream().filter(cardId::equals).findFirst();
    if (!cardOptional.isPresent()) {
      throw new BLValidationException("deck does not contain card with given id");
    }

    cardService.findById(cardId).orElseThrow(() -> new BLValidationException("card with given id does not exist"));
  }

  public Deck addCardToExistingDeck(final String id, final Card card) {
    Deck deck = deckRepository.findById(id).orElseThrow(() -> new BLValidationException("deck with given id does not exist"));
    return addCardToDeck(deck, card);
  }

  public Deck addCardToNewDeck(final String deckName, final Card card) {
    Deck deck = new Deck();
    deck.setName(deckName);
    deck = update(deck);
    return addCardToDeck(deck, card);
  }

  private Deck addCardToDeck(Deck deck, final Card card) {
    cardService.save(card);

    if (deck.getCards() == null) {
      deck.setCards(new ArrayList<>());
    }

    deck.getCards().add(card.getId());
    return update(deck);
  }

  public List<Deck> getAllDeckNames() {
    return deckRepository.getAllDeckNames();
  }
}
