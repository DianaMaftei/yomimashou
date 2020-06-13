import React, {Component} from 'react';
import {
  FormControl,
  MenuItem,
  Select,
  TextField
} from "@material-ui/core/umd/material-ui.development";
import axios from "axios";
import {studyApiUrl} from "../../../../../AppUrl";

class AddCard extends Component {
  constructor(props) {
    super(props);
    this.state = {
      added: false,
      decks: null,
      deckId: '',
      deckName: null
    };
  }

  getDeckNames(ev) {
    axios.get(studyApiUrl + '/api/deck/names').then(decks => {
      this.setState({...this.state, decks: decks.data})
    })
  }

  handleDeckChange(ev) {
    this.setState({...this.state, deckId: ev.target.value})
  }

  setDeckName(ev) {
    this.setState({
      ...this.state,
      deckName: ev.target.value
    })
  }

  AddToDeckButton() {
    if (!this.state.decks) {
      return <span className="add-list-btn"
                   onClick={this.getDeckNames.bind(this)}>Add to Deck</span>
    }
  }

  DecksDropDown() {
    if (this.state.decks && this.state.deckId != 'NEW') {
      return (
          <FormControl>
            <Select
                value={this.state.deckId}
                onChange={this.handleDeckChange.bind(this)}
                style={{width: 200}}
            >

              <MenuItem value="NEW">New Deck...</MenuItem>)

              {this.state.decks.map(deck =>
                  <MenuItem key={deck.id}
                            value={deck.id}>{deck.name}</MenuItem>)}

            </Select>
          </FormControl>
      )
    }
  }

  DeckNameInputField() {
    if (this.state.deckId === 'NEW') {
      return (
          <TextField required id="deck-name-required" label="Deck name"
                     value={this.state.deckName || ''}
                     onChange={this.setDeckName.bind(this)} margin="normal"/>
      )
    }
  }

  AddButton() {
    let deckId = this.state.deckId;
    if ((deckId === 'NEW' && this.state.deckName) || (deckId && deckId
        !== 'NEW')) {
      return <span className="btn btn-success"
                   onClick={this.saveItem.bind(this)}>Add</span>
    }
  }

  CancelButton() {
    if (this.state.decks) {
      return <span className="btn btn-dark"
                   onClick={this.resetState.bind(this)}>Cancel</span>
    }
  }

  resetState() {
    this.setState({...this.state, deckId: '', deckName: '', decks: null})
  }

  saveItem() {
    let deckId = this.state.deckId == 'NEW' ? null : this.state.deckId;
    let deckName = this.state.deckName;

    const url = deckId ? studyApiUrl + '/api/deck/' + deckId + "/addCard" :
        studyApiUrl + '/api/deck/addCard?deckName=' + deckName;

    axios.post(url, this.props.cardItem);
    this.setState({...this.state, deckId: '', deckName: '', decks: null, added: true});
  }

  render() {
    return (
        <div>
          {this.AddToDeckButton()}

          {this.DecksDropDown()}

          {this.DeckNameInputField()}

          {this.AddButton()}

          {this.CancelButton()}

          {this.state.added && <span>Item was added to deck</span>}
        </div>
    );
  }
}

export default AddCard;