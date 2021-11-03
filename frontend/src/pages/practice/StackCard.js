import React from "react";
import * as PropTypes from "prop-types";

const StackCard = ({index, style, card, bgColors}) => {
    const backgroundColor = bgColors[index];
    
    return (
        <div className={"card stack__item " + (index === 0 ? "stack__item--current" : "")}
             style={style} data-cardid={card.id}>
            <div className="card-inner">
                <li className="card-front" style={{backgroundColor: backgroundColor}}>
                    <h1>{card.kanji}</h1>
                    <h3>{card.kana}</h3>
                    <h6>{card.repetitions + " repetitions"}</h6>
                </li>
                <li className="card-back" style={{backgroundColor: backgroundColor}}>
                    <h3>{card.explanation}</h3>
                </li>
            </div>
        </div>
    );
}

StackCard.propTypes = {
    card: PropTypes.object.isRequired,
    index: PropTypes.number.isRequired,
    style: PropTypes.shape({
        transform: PropTypes.string,
        pointerEvents: PropTypes.string,
        opacity: PropTypes.any,
        zIndex: PropTypes.number
    }),
    bgColors: PropTypes.arrayOf(PropTypes.any)
};

export default StackCard;