import React from "react";

const Name = (props) => {

    let firstRow = [];

    if (props.kanji) {
        firstRow.push(<span key={props.kanji + props.kana} className="w-kanji">{props.kanji}</span>);
    }

    firstRow.push(<span key={props.kana + props.kana} className="w-kana">{props.kana}</span>);

    let def = "";
    if (props.def) {
        def = <p className="w-def">{props.def}</p>;
    }

    return (
        <div className={"w-word " + props.wordClassName}>
            <p> {firstRow}</p>
            {def}
        </div>
    );
};

export default Name;