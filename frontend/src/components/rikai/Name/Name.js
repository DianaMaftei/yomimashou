import React from "react";

export default ({ kanji, kana, def, wordClassName }) => {

    let firstRow = [];

    if (kanji) {
        firstRow.push(<span key={kanji + kana} className="w-kanji">{kanji}</span>);
    }

    firstRow.push(<span key={kana + kana} className="w-kana">{kana}</span>);

    let definition = "";
    if (def) {
        definition = <p className="w-def">{def}</p>;
    }

    return (
        <div className={"w-word " + wordClassName}>
            <p> {firstRow}</p>
            {definition}
        </div>
    );
};