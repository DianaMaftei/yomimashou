import React, {useEffect} from 'react';
import {useDispatch, useSelector} from "react-redux";
import {withRouter} from 'react-router-dom'
import axios from "axios";
import {studyApiUrl} from "../../AppUrl";
import Header from "../../components/header/Header";
import Deck from "./deck/Deck";

const Decks = ({history}) => {
    const dispatch = useDispatch();
    const decks = useSelector(state => state.decks.decks);

    const fetchDecks = () => dispatch({
        type: 'GET_DECKS',
        payload: axios.get(studyApiUrl + '/api/study/deck')
    });

    useEffect(fetchDecks, [dispatch])

    const deleteDeck = (deckId) => {
        axios.delete(studyApiUrl + '/api/study/deck/' + deckId)
            .then(fetchDecks);
    }

    if (!decks) {
        return <div/>;
    }

    return (
        <div id="decks-page" className="text-center">
            <div id="app-header">
                <Header leftIcon="menu" centerText="My decks" fontSize={32}/>
            </div>
            <div className="text-center">
                {decks.map((deck, index) => <Deck key={"deck-" + index} deck={deck}
                                                  onEdit={(deckId) => history.push('/deck/' + deckId)}
                                                  onDelete={deleteDeck}/>)}

                {
                    !decks || decks.length === 0 &&
                    <h4>You don't have any decks created. Go read some texts and add items to practice.</h4>
                }
            </div>
        </div>
    );
}

export default withRouter(Decks);
