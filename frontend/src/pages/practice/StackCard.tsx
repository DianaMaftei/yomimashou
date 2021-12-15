import React from 'react';


const StackCard = ({index, style, card, bgColors}: StackCardProps) => {
    const backgroundColor = bgColors[index];

    return (
        <div className={'card stack__item ' + (index === 0 ? 'stack__item--current' : '')}
             style={style} data-cardid={card.id}>
            <div className="card-inner">
                <li className="card-front" style={{backgroundColor: backgroundColor}}>
                    <h1>{card.kanji}</h1>
                    <h3>{card.kana}</h3>
                    <h6>{card.repetitions + ' repetitions'}</h6>
                </li>
                <li className="card-back" style={{backgroundColor: backgroundColor}}>
                    <h3>{card.explanation}</h3>
                </li>
            </div>
        </div>
    );
};

type StackCardProps = {
    card: object
    index: number
    style: {
        transform: string
        pointerEvents: string
        opacity: any
        zIndex: number
    },
    bgColors: Array<any>
}
export default StackCard;
