package com.yomimashou.study.deck;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/study/deck")
@AllArgsConstructor
public class DeckController {

    //TODO !!!!!!!!! ensure that only the user whose decks these belong to can access them

    private final DeckService deckService;

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
