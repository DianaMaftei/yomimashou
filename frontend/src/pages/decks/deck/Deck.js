import React from 'react';
import "./deck.scss";
import FilterIcon from 'mdi-react/FilterIcon';
import EditIcon from 'mdi-react/EditIcon';
import DeleteIcon from 'mdi-react/DeleteIcon';
import colors from "../../../style/colorConstants";
import DeckMasteryIndicator from "./deck-mastery-indicator/DeckMasteryIndicator";
import {Link} from "react-router-dom";

const getFontSize = (textLength) => {
    const baseSize = 16
    if (textLength >= baseSize) {
        textLength = baseSize - 5
    }
    const fontSize = baseSize - textLength
    return `${fontSize}vw`
}

const Deck = ({deck, onEdit, onDelete}) => {

    var nameStyle= {
        fontSize : getFontSize(deck.name.length)
    }

    return (
        <div className="deck-chart">
            <div className="deck">
                {deck && deck.mastery && <DeckMasteryIndicator/>}
                <ul >
                    <li className="deck-card card-1">
                        { deck && deck.totalCards && <div className="deck-card-top">
                            <FilterIcon color={colors.yomiWhite} size="30"/>
                            <span>57</span>
                        </div>}
                        <Link to={"/practice/" + deck.id} className="deck-card-body" >
                            <div style={nameStyle}>{deck.name}</div>
                        </Link>
                        <div className="deck-card-bottom">
                            <EditIcon color={colors.yomiWhite} size="30" onClick={() => onEdit(deck.id)}/>
                            <DeleteIcon color={colors.yomiWhite} size="30" onClick={() => onDelete(deck.id)}/>
                        </div>
                    </li>
                    <li className="deck-card card-2"/>
                    <li className="deck-card card-3"/>
                </ul>
            </div>
        </div>
    );
};

export default Deck;
