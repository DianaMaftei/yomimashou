import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { withRouter } from 'react-router-dom';
import Header from '../../components/header/Header';
import Deck from './deck/Deck';
import { getDecksAction } from './decksActions';
import { deleteDeck } from '../../service/DeckService';
import { History } from 'history';


const Decks = ({history}: DecksProps) => {
    const dispatch = useDispatch();
    const decks = useSelector(state => state.decks.decks);

    const fetchDecks = () => dispatch(getDecksAction());

    useEffect(fetchDecks, [dispatch]);

    const onDelete = (deckId: number) => {
        //TODO use action
        deleteDeck(deckId).then(fetchDecks);
    };

    if(!decks) {
        return <div/>;
    }

    return (
        <div id="decks-page" className="text-center">
            <div id="app-header">
                <Header leftIcon="menu" centerText="My decks" fontSize={32}/>
            </div>
            <div className="text-center">
                {decks.map((deck, index) => <Deck key={'deck-' + index} deck={deck}
                                                  onEdit={(deckId: number) => history.push('/deck/' + deckId)}
                                                  onDelete={onDelete}/>)}

                {
                    !decks || decks.length === 0 &&
                    <h4>You don't have any decks created. Go read some texts and add items to practice.</h4>
                }
            </div>
        </div>
    );
};

type DecksProps = {
    history: History
}

export default withRouter(Decks);
