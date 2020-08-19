import React from 'react';
import "./deck.css";
import MaterialIcon from "material-icons-react";
import colors from "../../../style/colorConstants";
import DeckMasteryIndicator from "./deck-mastery-indicator/DeckMasteryIndicator";

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
                            <MaterialIcon icon="filter_none" color={colors.yomiWhite} size="tiny"/>
                            <span>57</span>
                        </div>}
                        <div className="deck-card-body" style={nameStyle}>{deck.name}</div>
                        <div className="deck-card-bottom">
                            <MaterialIcon icon="edit" color={colors.yomiWhite} size="small" onClick={() => onEdit(deck.id)}/>
                            <MaterialIcon icon="delete" color={colors.yomiWhite} size="small" onClick={() => onDelete(deck.id)}/>
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
