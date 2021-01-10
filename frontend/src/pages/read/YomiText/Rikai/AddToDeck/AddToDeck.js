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
import SearchType from "../SearchType";
import PopupType from "../PopupType";

class AddToDeck extends Component {
  constructor(props) {
    super(props);
    this.state = {
      added: false,
      decks: null,
      deckId: '',
      deckName: null,
      item: {
        ...this.props.item
      }
    };

      axios.get(studyApiUrl + '/api/deck/names').then(decks => {
          this.setState({...this.state, decks: decks.data})
      })
  }

  handleDeckChange(ev) {
    this.setState({...this.state, deckId: ev.target.value})
    this.props.toggleOutsideClickHandler();
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
    let deckId = this.state.deckId;
    let deckName = this.state.deckName;

    const url = deckId != 'NEW' ? studyApiUrl + '/api/deck/' + deckId + "/addCard" :
        studyApiUrl + '/api/deck/addCard?deckName=' + deckName;

    axios.post(url, {
      kana: "" + this.state.item.kana,
      kanji: "" + this.state.item.kanji,
      explanation: "" + this.state.item.meanings
    });
    this.setState({...this.state, deckId: '', deckName: '', decks: null, added: true});
  }

  handleBackBtn() {
    if(this.props.item.type === SearchType.WORD) {
      this.props.changePopup(PopupType.WORD);
      } else if (this.props.item.type === SearchType.KANJI) {
      this.props.changePopup(PopupType.KANJI);
      }
  }

  handleInputChange(ev, input) {
    let item = this.state.item;
    item[input] = ev.target.value;

    this.setState({
      ...this.state,
      input: item
    })
  }

  render() {
    let kanaKanjiInvalid = (!this.state.item.kana || this.state.item.kana.length === 0) &&
                           (!this.state.item.kanji || this.state.item.kanji.length === 0);
    let deckInvalid = !this.state.deckId || (this.state.deckId === 'NEW' && !this.state.deckName);

    return (
        <div id="rikai-window" style={this.props.style} className="elevation-lg">
          <div className="rikai-display">
            <div className="add-item-top">
              <span onClick={this.handleBackBtn.bind(this)}>
                <BackButton  />
              </span>
            </div>

            <div className="add-item-fields">

              <div id="item-kanji-side" className={kanaKanjiInvalid ? "error" : ""}>
                <TextField
                    error={kanaKanjiInvalid}
                    value={this.state.item.kanji || ''}
                    onChange={(ev) => this.handleInputChange(ev, "kanji")}
                    margin="dense"
                    label="Kanji" variant="outlined"/>
              </div>

              <div id="item-kana-side" className={kanaKanjiInvalid ? "error" : ""}>
                <TextField
                           error={kanaKanjiInvalid}
                           value={this.state.item.kana || ''}
                           onChange={(ev) => this.handleInputChange(ev, "kana")}
                           margin="dense"
                           label="Kana" variant="outlined"/>
              </div>

              <div id="item-meaning-side" className={!this.state.item.meanings || !this.state.item.meanings.length === 0 ? "error" : ""}>
                <TextField
                           required
                           error={!this.state.item.meanings || !this.state.item.meanings.length === 0}
                           value={this.state.item.meanings || ''}
                           onChange={(ev) => this.handleInputChange(ev, "meanings")}
                           margin="dense"
                           label="Meaning" variant="outlined"/>
              </div>

            </div>

            <div className="add-item-decks">
              <FormControl margin="dense" variant="outlined" id="deck-select">
                <InputLabel id="demo-simple-select-outlined-label">Choose a deck</InputLabel>
                <Select
                    value={this.state.deckId}
                    onChange={this.handleDeckChange.bind(this)}
                    style={{width: 200}}
                    onOpen={this.props.toggleOutsideClickHandler}
                    label="Choose a deck"
                >
                  <MenuItem style={{textAlign: 'center', display: 'flex'}} key="NEW" value="NEW">New deck...</MenuItem>
                  {this.state.decks && this.state.decks.map(deck =>
                        <MenuItem style={{textAlign: 'center', display: 'flex'}}  key={deck.id}
                                  value={deck.id}>{deck.name}</MenuItem>
                  )}

                </Select>
              </FormControl>

              {
                (this.state.deckId === "NEW") &&
                    <div id="new-deck-name">
                      <TextField id="deck-name-required"
                                 value={this.state.deckName || ''}
                                 onChange={this.setDeckName.bind(this)}
                                 margin="dense"
                                 label="New deck name" variant="outlined"/>
                    </div>
              }

            </div>

            <button disabled={kanaKanjiInvalid || !this.state.item.meanings || deckInvalid} className="btn add-btn"
                  onClick={this.saveItem.bind(this)}>Add</button>

              {this.state.added && <span>Item was added to deck</span>}
          </div>
        </div>
    );
  }
}

export default AddToDeck;
