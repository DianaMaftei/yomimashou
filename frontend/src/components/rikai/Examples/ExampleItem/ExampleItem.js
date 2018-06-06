import React from "react";

export default ({ exampleClassName, example }) => (
    <div className={"example " + exampleClassName}>
        <p>{example}</p>
    </div>
);