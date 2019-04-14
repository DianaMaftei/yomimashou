import React from "react";
import TTS from "../../../../../`common/TTS/TTS";

export default ({ exampleClassName, example }) => (
    <div className={"example " + exampleClassName}>
        <p>{example.sentence}</p>
        <TTS text={example.sentence}/>
        <p>{example.meaning}</p>
    </div>
);