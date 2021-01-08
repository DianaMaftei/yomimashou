package com.yomimashou.study.studydeck;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@JsonInclude(Include.NON_NULL)
public class Deck {

  @Id
  private String id;

  @Size(max = 50)
  private String name;

  private List<String> cards;

  public Deck() {
  }

  public Deck(String deckName, List<String> cards) {
    this.name = deckName;
    this.cards = cards;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public List<String> getCards() {
    return cards;
  }

  public void setCards(final List<String> cards) {
    this.cards = cards;
  }
}
