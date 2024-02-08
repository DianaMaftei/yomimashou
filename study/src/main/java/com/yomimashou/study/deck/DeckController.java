package com.yomimashou.study.deck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/study/deck")
public class DeckController {

    //TODO !!!!!!!!! ensure that only the user whose decks these belong to can access them

    @Autowired
    private DeckService deckService;

    @GetMapping()
    public List<Deck> getAll() {
        return deckService.getAll();
    }

    @GetMapping("/{id}")
    public Deck get(@PathVariable Long id) {
        return deckService.findById(id);
    }

    @PutMapping("/{id}")
    public Deck update(@PathVariable Long id, @RequestBody Deck deck) {
        if (!id.equals(deck.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        return deckService.save(deck);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deckService.delete(id);
    }

    // TODO export deck
    // TODO generate mp3

}
