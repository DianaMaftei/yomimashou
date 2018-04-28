import React from "react";
import Example from "./Example";

class ExampleList extends React.Component {

    render() {
        if (!this.props.resultList) {
            return <div></div>;
        }

        let examples = [];

        for (let i = 0; i < this.props.resultList.length; i++) {
            let result = this.props.resultList[i];
            examples.push(<Example exampleClassName={(i % 2 === 0) ? 'definition-light' : 'definition-dark'}
                                   key={result} example={result}/>);
        }

        if(examples.length === 0) examples.push(<span key={"noResult"}>No examples found.</span>);
        return (
            <div>
                {examples}
            </div>
        );
    }
}

export default ExampleList;