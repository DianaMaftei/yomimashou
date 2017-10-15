import React from "react";


class Word extends React.Component {

    showExamples() {
        this.props.showExamples({kanji: this.props.kanji, kana: this.props.kana});
    }

    render() {

        let firstRow = [];

        if (this.props.kanji) {
            firstRow.push(<span key={this.props.kanji} className="w-kanji">{this.props.kanji}</span>);
        }

        firstRow.push(<span key={this.props.kana + this.props.conj}><span className="w-kana">{this.props.kana}</span><span
            style={{float: 'right', cursor: 'pointer', color: 'blue'}} onClick={this.showExamples.bind(this)}>Ex</span></span>);

        if (this.props.conj) {
            firstRow.push(<span key={this.props.conj} className="w-conj">{this.props.conj}</span>);
        }

        let def = "";
        if (this.props.def) {
            def = <p className="w-def">{this.props.def}</p>;
        }

        return (
            <div className={"w-word "+ this.props.wordClassName}>
                {firstRow}
                {def}
            </div>
        );

    }
}

export default Word;