import React from "react";

class Example extends React.Component {

    render() {
        return (
            <div className={"example "+ this.props.exampleClassName}>
                <p>{this.props.example}</p>
            </div>
        );
    }
}

export default Example;