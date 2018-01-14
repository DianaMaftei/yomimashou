import React from "react";


class Word extends React.Component {

    showExamples() {
        this.props.showExamples({kanji: this.props.kanji, kana: this.props.kana});
    }

    render() {

        let firstRow = [];
        let secondRow = [];

        if (this.props.kanji) {
            firstRow.push(<div key={this.props.kanji}><span className="w-kanji">{this.props.kanji}</span><span className="example-btn"  onClick={this.showExamples.bind(this)}>Ex</span></div>);
        }

        secondRow.push(<span key={this.props.kana + this.props.conj}><span className="w-kana">{this.props.kana}</span></span>);

        if (this.props.conj) {
            secondRow.push(<span key={this.props.conj} className="w-conj">{this.props.conj}</span>);
        }

        let def = "";
        if (this.props.def) {
            def = <p className="w-def">{this.props.def}</p>;
        }

        return (
            <div className={"w-word "+ this.props.wordClassName}>
                {firstRow}
                {secondRow}
                {def}
            </div>
        );

    }
}

export default Word;