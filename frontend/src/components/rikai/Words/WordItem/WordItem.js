import React from "react";
import WordItemWithKanjiAndShortDef from "./WordItemWithKanjiAndShortDef";
import WordItemWithKanjiAndLongDef from "./WordItemWithKanjiAndLongDef";
import WordItemWithKanaAndShortDef from "./WordItemWithKanaAndShortDef";
import WordItemWithKanaAndLongDef from "./WordItemWithKanaAndLongDef";


class WordItem extends React.Component {

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
        if (this.props.kanji) {
            if (this.state.showShortDef && this.props.shortDef) {
                return WordItemWithKanjiAndShortDef(this.props.kanji, this.props.kana, this.props.grammar, this.props.shortDef, this.showExamples.bind(this), this.props.wordClassName, this.showLongDef.bind(this));
            }
            return WordItemWithKanjiAndLongDef(this.props.kanji, this.props.kana, this.props.grammar, this.props.longDef, this.showExamples.bind(this), this.props.wordClassName);
        } else if (this.state.showShortDef && this.props.shortDef) {
            return WordItemWithKanaAndShortDef(this.props.kana, this.props.grammar, this.props.shortDef, this.showExamples.bind(this), this.props.wordClassName, this.showLongDef.bind(this));
        } else {
            return WordItemWithKanaAndLongDef(this.props.kana, this.props.grammar, this.props.longDef, this.showExamples.bind(this), this.props.wordClassName);
        }
    }
}


export default WordItem;