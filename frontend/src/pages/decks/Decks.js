import React from 'react';
import {connect} from "react-redux";
import {withRouter} from 'react-router-dom'
import axios from "axios";
import {studyApiUrl} from "../../AppUrl";
import Header from "../../components/header/Header";
import Deck from "./deck/Deck";

const mapStateToProps = (state) => ({
    decks: state.decks.decks
});

const mapDispatchToProps = (dispatch) => ({
    getDecks: () => {
        dispatch({
            type: 'GET_DECKS',
            payload: axios.get(studyApiUrl + '/api/study/deck')
        });
    }
});

class Decks extends React.Component {

    constructor(props) {
        super(props);
        props.getDecks();
    }

    edit(deckId) {
        this.props.history.push('/deck/' + deckId);
    }

    delete(deckId) {
        axios.delete(studyApiUrl + '/api/study/deck/' + deckId)
            .then(() => {
                this.props.getDecks();
            });
    }

    render() {
        if (!this.props.decks) {
            return <div/>;
        }

        let decks = this.props.decks;

        return (
            <div id="decks-page" className="text-center">
                <div id="app-header">
                    <Header leftIcon="menu" centerText="My decks" fontSize={32}/>
                </div>
                <div className="text-center">
                    {decks.map((deck, index) => (
                        <Deck key={"deck-" + index} deck={deck} onEdit={this.edit.bind(this)} onDelete={this.delete.bind(this)}/>
                    ))}

                    {
                        !decks || decks.length === 0 && <h4>You don't have any decks created. Go read some texts and add items to practice.</h4>
                    }
                </div>
            </div>
        );
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Decks));
