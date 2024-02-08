import React from 'react';
import TTS from '../../../../../../components/tts/TTS';


class WordItem extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      showShortDef: true,
    };
  }

  showExamples() {
    this.props.showExamples({kanji: this.props.kanji, kana: this.props.kana});
  }

  showLongDef(ev) {
    ev.stopPropagation();

    this.setState({...this.state, showShortDef: false})
  }

  render() {

    let kanji = this.props.kanji.map((item, index) => {
      return <span className="w-kanji" key={item + index}>{item}</span>
    });

    let kana = this.props.kana.map((item, index) => {
      return <span className="w-kana" key={item + index}>{item}</span>
    });

    let definition = this.state.showShortDef && this.props.shortDef
        ? this.props.shortDef : this.props.longDef;

    return (
        <div id="word-item" className={"w-word " + this.props.wordClassName}>
          <div key={this.props.kanji}>
            <span>{kanji}</span>
            <span className="example-btn"
                  onClick={this.showExamples.bind(this)}>Ex</span>
          </div>
          <span key={this.props.kana + this.props.grammar}>{kana}</span>
          <span key={this.props.grammar}
                className="w-grammar">{this.props.grammar}</span>
          <p className="w-def">
            {definition}
            {
              this.state.showShortDef && this.props.shortDef &&
              <span id="ellipsis"
                    onClick={this.showLongDef.bind(this)}> ...</span>
            }
          </p>
          <TTS text={this.props.kana.join()}/>
        </div>
    )
  }
}

export default WordItem;
