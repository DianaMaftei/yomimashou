package com.github.dianamaftei.study.studydeck;


import com.github.dianamaftei.study.BLValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

  public Deck save(final Deck deck) {
    if (deck.getCards() != null) {
      deck.getCards().forEach(item -> cardService.save(item));
    }
    return deckRepository.save(deck);
  }

  private void validateSave(final Deck deck) {
    // TODO check mandatory fields
    // TODO check that deck name is unique for user - where is the reference to the user?
  }

  public Page<Deck> findAll(final Pageable pageable) {
    return deckRepository.findAllAndExcludeCards(pageable);
  }

  public Optional<Deck> findById(final String id) {
    return deckRepository.findById(id);
  }

  public void delete(final String id) {
    deckRepository.deleteById(id);
  }

  public void removeCardFromDeck(final String id, final String itemId) {
    Optional<Deck> deckOptional = deckRepository.findById(id);
    if (!deckOptional.isPresent()) {
      throw new BLValidationException("invalid deck id");
    }

    List<Card> cards = deckOptional.get().getCards();
    if (cards == null || cards.isEmpty()) {
      throw new BLValidationException("deck has no cards");
    }

    Optional<Card> cardOptional = cards.stream()
        .filter(item -> itemId.equals(item.getId())).findFirst();
    if (!cardOptional.isPresent()) {
      throw new BLValidationException("deck does not contain card with given id");
    }

    deckOptional.get().getCards().remove(cardOptional.get());
    deckRepository.save(deckOptional.get());
    cardService.delete(cardOptional.get());
  }


  public Deck addCardToExistingDeck(final String id, final Card card) {
    Optional<Deck> optionalDeck = deckRepository.findById(id);
    if (!optionalDeck.isPresent()) {
      throw new BLValidationException("deck with given id does not exist");
    }
    return addCardToDeck(optionalDeck.get(), card);
  }

  public Deck addCardToNewDeck(final String deckName, final Card card) {
    Deck deck = new Deck();
    deck.setName(deckName);
    deck.setCards(new ArrayList<>());
    deck = save(deck);
    return addCardToDeck(deck, card);
  }

  private Deck addCardToDeck(Deck deck, final Card card) {
    card.setDeckId(deck.getId());
    cardService.save(card);
    deck.getCards().add(card);
    return save(deck);
  }
}
