package com.yomimashou.study.deck;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "deck")
public class Deck {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Size(max = 50)
  private String name;

  public Deck() {
  }

  public Deck(String deckName) {
    this.name = deckName;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Deck)) {
      return false;
    }
    Deck deck = (Deck) o;
    return id.equals(deck.id) && name.equals(deck.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
