import React, {Component} from 'react';
import {
  FormControl,
  MenuItem,
  Select,
  TextField
} from "@material-ui/core";
import axios from "axios";
import {studyApiUrl} from "../../../../../AppUrl";
import BackButton from "../../../../`common/buttons/backBtn/BackButton";
import "./addToDeck.css";
import InputLabel from "@material-ui/core/InputLabel";

class AddToDeck extends Component {
  constructor(props) {
    super(props);
    this.state = {
      added: false,
      decks: null,
      deckId: '',
      deckName: null
    };

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
        <div id="rikai-window" style={this.props.style} className="elevation-lg">
          <div className="rikai-display">
            <BackButton/>

            <div className="add-item-fields">
              <TextField id="item-kanji-side"
                         // value={this.props.item.kanji || ''}
                         onChange={this.setDeckName.bind(this)}
                         margin="dense"
                         label="Kanji" variant="outlined"/>

              <TextField id="item-kana-side"
                         value={this.props.item.kana || ''}
                         onChange={this.setDeckName.bind(this)}
                         margin="dense"
                         label="Kana" variant="outlined"/>

              <TextField id="item-meaning-side"
                         value={this.props.item.meaning || ''}
                         onChange={this.setDeckName.bind(this)}
                         margin="dense"
                         label="Meaning" variant="outlined"/>
            </div>

            <div className="add-item-decks">
              {this.state.decks &&

              <FormControl margin="dense" variant="outlined">
                <InputLabel id="demo-simple-select-outlined-label">Choose a deck</InputLabel>
                <Select
                    value={this.state.deckId}
                    onChange={this.handleDeckChange.bind(this)}
                    style={{width: 200}}
                >

                  {this.state.decks.map(deck =>
                      <MenuItem key={deck.id}
                                value={deck.id}>{deck.name}</MenuItem>)}

                </Select>
              </FormControl>
              }
              <div>or</div>

              <TextField id="deck-name-required"
                         value={this.state.deckName || ''}
                         onChange={this.setDeckName.bind(this)}
                         margin="dense"
                         label="New deck name" variant="outlined"/>
            </div>



            <div className="btn add-btn"
                  onClick={this.saveItem.bind(this)}>Add</div>

              {this.state.added && <span>Item was added to deck</span>}
          </div>
        </div>
    );
  }
}

export default AddToDeck;