package com.yomimashou.study.studydeck;

import com.yomimashou.study.BLValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/deck")
public class DeckController {

  //TODO !!!!!!!!! ensure that only the user whose decks these belong to can access them

  @Autowired
  private DeckService deckService;

  @GetMapping()
  public Page<Deck> viewAllDecks(final Pageable pageable) {
    //TODO should only return name and number of cards - statistics - how many due, what percentage done
    return deckService.findAll(pageable);
  }

  @GetMapping("/{id}")
  public Deck get(@PathVariable String id) {
    return deckService.findById(id).orElse(null);
  }

  @PostMapping()
  public Deck save(@RequestParam String deckName, @RequestBody List<Card> cards) {
    if (StringUtils.isBlank(deckName)) {
      throw new BLValidationException("Deck requires a name");
    }
    return deckService.save(deckName, cards);
  }

  @PutMapping("/{id}")
  public Deck update(@PathVariable String id, @RequestBody Deck deck) {
    if (!id.equals(deck.getId())) {
      throw new BLValidationException("invalid id");
    }
    return deckService.update(deck);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable String id) {
    deckService.delete(id);
  }

  @PostMapping("/{id}/addCard")
  public Deck addCardToExistingDeck(@PathVariable String id, @RequestBody Card card) {
    if (StringUtils.isBlank(id) || "null".equals(id)) {
      throw new BLValidationException("invalid id");
    }
    return deckService.addCardToExistingDeck(id, card);
  }

  @PostMapping("/addCard")
  public Deck addCardToNewDeck(@RequestParam("deckName") String deckName, @RequestBody Card card) {
    if (StringUtils.isBlank(deckName) || "null".equals(deckName)) {
      throw new BLValidationException("invalid deck name");
    }
    return deckService.addCardToNewDeck(deckName, card);
  }

  @PostMapping("/{id}/card/delete/{cardId}")
  public void removeCardFromDeck(@PathVariable String id, @PathVariable String cardId) {
    deckService.removeCardFromDeck(id, cardId);
  }

  @GetMapping("/names")
  public List<Deck> getAllDeckNames() {
    return deckService.getAllDeckNames();
  }

  // TODO export deck
  // TODO generate mp3

}
