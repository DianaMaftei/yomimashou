import React from "react";
import NameItem from "./NameItem";

export default (results, limit, showMoreResults) => (
    <div id="name-list-limited">
        {results.filter((result, index) => {
            return index < limit;
        }).map((result, index) => {
            return <NameItem wordClassName={(index % 2 === 0) ? 'definition-light' : 'definition-dark'}
                             key={result.reading + result.kanji + result.translations}
                             kanji={result.kanji}
                             kana={result.reading}
                             def={result.translations}
                             type={result.type}/>
        })}

        <div className="more-btn">
            <span id="more-btn" onClick={showMoreResults}>&#9660; More</span>
        </div>
    </div>
);
