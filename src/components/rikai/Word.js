import React from "react";

const Word = (props) => {

    let firstRow = [];

    if (props.kanji) {
        firstRow.push(<span key={props.kanji} className="w-kanji">{props.kanji}</span>);
    }

    firstRow.push(<span key={props.kana} className="w-kana">{props.kana}</span>);

    if (props.conj) {
        firstRow.push(<span key={props.conj} className="w-conj">{props.conj}</span>);
    }

    let def = "";
    if (props.def) {
        def = <p className="w-def">{props.def}</p>;
    }

    return (
        <div className={"w-word "+ props.wordClassName}>
            {firstRow}
            {def}
        </div>
    );
};

export default Word;