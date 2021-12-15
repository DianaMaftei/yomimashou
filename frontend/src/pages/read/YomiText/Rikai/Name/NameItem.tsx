import React from 'react';


const NameItem = ({kanji, kana, def, type, wordClassName}: NameItemProps) => {
    if(!kanji && !kana && !def && !type) {
        return <div/>;
    }

    return (
        <div className={'w-word ' + wordClassName}>
            <p>
                {kanji && <span key={kanji + kana} className="w-kanji">{kanji}</span>}
                {kana && <span key={kana + kana} className="w-kana">{kana}</span>}
            </p>
            <p>
                {def && <span className="w-def">{def}</span>}
                {type && <span className="w-type">{type}</span>}
            </p>
        </div>
    );
};

type NameItemProps = {
    kanji: string
    kana: string
    def: string
    type: string
    wordClassName: string
}

export default NameItem;
