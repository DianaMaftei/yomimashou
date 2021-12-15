import React from "react";
import WordListLimited from "./WordListLimited";
import WordListFull from "./WordListFull";

class WordList extends React.Component {

    constructor(props) {
        super(props);
    }

    componentWillUpdate(nextProps, nextState) {
        if (this.props.resultList !== nextProps.resultList) {
            this.setState({limit: true});
        }
    }

    showMoreResults() {
        const searchResult = this.props.searchResult;
        this.props.showMore();
    }

    render() {
        if (!this.props.resultList) {
            return <div/>;
        } else if (this.props.last) {
            return WordListFull(this.props.resultList, this.props.showExamples)
        } else if (!this.props.last) {
            return WordListLimited(this.props.resultList, this.props.showExamples, this.showMoreResults.bind(this))
        }
    }
}

export default WordList;
