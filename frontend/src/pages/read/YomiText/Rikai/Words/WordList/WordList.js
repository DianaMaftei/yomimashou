import React from "react";
import WordListLimited from "./WordListLimited";
import WordListFull from "./WordListFull";

class WordList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            limit: true
        };
    }

    componentWillUpdate(nextProps, nextState) {
        if (this.props.resultList !== nextProps.resultList) {
            this.setState({ limit: true });
        }
    }

    showMoreResults() {
        this.setState({ limit: false });
    }

    render() {
        if (!this.props.resultList) {
            return <div/>;
        }
        else if (!this.state.limit || this.props.resultList.length < this.props.limit) {
            return WordListFull(this.props.resultList, this.props.showExamples)
        }
        else if (this.state.limit) {
            return WordListLimited(this.props.resultList, this.props.limit, this.props.showExamples, this.showMoreResults.bind(this))
        }
    }
}

export default WordList;
