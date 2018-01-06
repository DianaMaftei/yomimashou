import React from "react";
import Word from "./Word";

class WordList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            limit: true
        };
    }

    componentWillUpdate(nextProps, nextState) {
        if(this.props.resultList !== nextProps.resultList) {
            this.setState({limit: true});
        }
    }

    showMoreResults() {
        this.setState({limit: false});
    }

    render() {
        if (!this.props.resultList) {
            return <div></div>;
        }

        let limitedResults = [];
        let moreResults = [];

        for(let i = 0; i < this.props.resultList.length; i++) {
            let result = this.props.resultList[i];

            if(i < this.props.limit) {
                limitedResults.push(<Word wordClassName={(i % 2 === 0) ? 'definition-light' : 'definition-dark'}
                                          key={result.kana + result.kanji + result.conj + result.def} kanji={result.kanji}
                                          kana={result.kana} conj={result.conj} def={result.def} showExamples={this.props.showExamples}/>)
            } else {
                moreResults.push(<Word wordClassName={(i % 2 === 0) ? 'definition-light' : 'definition-dark'}
                                       key={result.kana + result.kanji + result.conj + result.def} kanji={result.kanji}
                                       kana={result.kana} conj={result.conj} def={result.def} showExamples={this.props.showExamples}/>);
            }
        }

        const { limit } = this.state;

        return (
            <div>
                {limitedResults}
                {(moreResults.length > 0 && limit) ? <div className="more-btn" ><span id="more-btn" onClick={this.showMoreResults.bind(this)}>&#9660; More</span></div> : ''}
                {!limit ? <div>{moreResults}</div> : <div></div>}
            </div>
        );
    }
}

export default WordList;
