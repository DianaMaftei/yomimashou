import React from 'react';
import NameItem from './NameItem';


export default (results) => (
    <div id="name-list-full">
        {results.map((result, index) => {
            return <NameItem wordClassName={(index % 2 === 0) ? 'definition-light' : 'definition-dark'}
                             key={result.reading + result.kanji + result.translations}
                             kanji={result.kanji}
                             kana={result.reading}
                             def={result.translations}
                             type={result.type}/>
        })}
    </div>
);
