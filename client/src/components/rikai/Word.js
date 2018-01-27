import React from "react";


class Word extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            showShortDef: true
        };
    }

    showExamples() {
        this.props.showExamples({ kanji: this.props.kanji, kana: this.props.kana });
    }

    showLongDef(ev) {
        ev.stopPropagation();

        this.setState({ showShortDef: false })
    }

    render() {
        let firstRow = [];
        let secondRow = [];
        let examplesIncluded = false;

        if (this.props.kanji) {
            let kanji = [];
            kanji.push(this.props.kanji.map((item, index) => {
                return <span className="w-kanji" key={item + index}>{item}</span>
            }));
            firstRow.push(<div key={this.props.kanji}>{kanji}<span className="example-btn"
                                                                   onClick={this.showExamples.bind(this)}>Ex</span>
            </div>);
            examplesIncluded = true;
        }

        let kana = [];
        kana.push(this.props.kana.map((item, index) => {
            return <span className="w-kana" key={item + index}>{item}</span>
        }));
        secondRow.push(<span key={this.props.kana + this.props.grammar}>{kana}</span>);

        if (!examplesIncluded) {
            secondRow.push(<span key={this.props.kanji + this.props.kana + "ex"} className="example-btn"
                                 onClick={this.showExamples.bind(this)}>Ex</span>);
        }

        if (this.props.grammar) {
            secondRow.push(<span key={this.props.grammar} className="w-grammar">{this.props.grammar}</span>);
        }

        let def;
        if (this.state.showShortDef && this.props.shortDef) {
            def = <p className="w-def shortDef">{this.props.shortDef} <span id="ellipsis"
                                                                            onClick={this.showLongDef.bind(this)}> ...</span>
            </p>;
        } else {
            def = <p className="w-def longDef">{this.props.longDef}</p>;
        }

        return (
            <div className={"w-word " + this.props.wordClassName}>
                {firstRow}
                {secondRow}
                {def}
            </div>
        );

    }
}

export default Word;