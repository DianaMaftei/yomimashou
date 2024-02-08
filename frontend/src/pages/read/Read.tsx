import LinearProgress from '@material-ui/core/LinearProgress';
import Slide from '@material-ui/core/Slide';
import Kuroshiro from 'kuroshiro';
import KuromojiAnalyzer from 'kuroshiro-analyzer-kuromoji/dist/kuroshiro-analyzer-kuromoji.min';
import React from 'react';
import { connect } from 'react-redux';
import Header from '../../components/header/Header';
import TextInfo from '../../components/textInfo/TextInfo';
import './read.scss';
import {
    analyzeTextAction,
    getTextByIdAction,
    parseTextWordsAction,
    resetDictionariesAction,
    setFuriganaTextAction,
    setFuriganaTitleAction,
    toggleTextActionsMenuAction
} from './readActions';
import { setKanjiLevelsAction } from './TextContainer/Rikai/popUpActions';
import { filterTextFuriganaByKanjiCategory } from './TextContainer/TextActions/furigana/FuriganaFilterByKanjiCategory';
import TextActions from './TextContainer/TextActions/TextActions';
import TextContainer from './TextContainer/TextContainer';


const mapStateToProps = (state) => ({
    text: state.readText.text,
    showTextActions: state.readText.showTextActions,
    kanjiLevels: state.config.kanjiLevels,
    words: state.readText.words,
    names: state.readText.names,
    analyzedText: state.readText.analyzedText
});

const mapDispatchToProps = (dispatch) => ({
    resetDictionaries: () => dispatch(resetDictionariesAction()),
    getWordsForText: (text) => dispatch(parseTextWordsAction(text)),
    analyzeText: (text) => dispatch(analyzeTextAction(text)),
    getTextById: id => dispatch(getTextByIdAction(id)),
    toggleTextActionsMenu: () => dispatch(toggleTextActionsMenuAction()),
    setKanjiLevels: (kanjiLevels) => dispatch(setKanjiLevelsAction(kanjiLevels)),
    setFuriganaText: text => dispatch(setFuriganaTextAction(text)),
    setFuriganaTitle: title => dispatch(setFuriganaTitleAction(title))
});

export class Read extends React.Component {

    constructor(props) {
        super(props);

        this.analyzer = new KuromojiAnalyzer({dictPath: '/static/kuromoji'});
        this.kuroshiro = new Kuroshiro();
        this.kuroshiro.init(this.analyzer);
    }

    componentDidMount() {
        const {id} = this.props.match.params;
        if (id) {
            // todo save locally the fact that the text was open and after 2 word click mark it as in progress in the db
            // if(isAuthenticated()) {
            //     axios.post(apiUrl + '/api/users/textStatus?progressStatus=OPEN&textId=' +id, {}, withHeaders());
            // }

            this.props.getTextById(id);
        } else {
            if (!this.props.text || !this.props.text.content) {
                this.props.history.push('/');
            }
        }

        if (!this.props.text.content) {
            return;
        }
    }

    componentWillUnmount() {
        this.props.resetDictionaries();
    }

    componentDidUpdate(prevProps, prevState) {
        if (!this.props.text.content) {
            return;
        }

        if (this.props.text.content !== prevProps.text.content) {
            console.log(this.props.text);
            this.props.analyzeText(this.props.text);
        }
    }

    handleFurigana = (event) => {

        let kanjiLevels = this.props.kanjiLevels;
        kanjiLevels[event.target.value] = event.target.checked;
        this.props.setKanjiLevels(kanjiLevels);

        let setFuriganaText = this.props.setFuriganaText;
        let setFuriganaTitle = this.props.setFuriganaTitle;

        let text = this.props.text.content;
        let title = this.props.text.title;

        this.kuroshiro.convert(text, {
            to: 'hiragana',
            mode: 'furigana'
        }).then(function (result) {
            setFuriganaText(filterTextFuriganaByKanjiCategory(result, kanjiLevels));
        });

        this.kuroshiro.convert(title, {
            to: 'hiragana',
            mode: 'furigana'
        }).then(function (result) {
            setFuriganaTitle(filterTextFuriganaByKanjiCategory(result, kanjiLevels));
        });
    };

    render() {
        return (
            <div id="read-page">
                <div id="app-header">
                    <Header leftIcon="menu" rightIcon="more_vert" centerText={this.props.text.title}
                            onRightIconClick={this.props.toggleTextActionsMenu}/>
                </div>
                <Slide direction="down" in={this.props.showTextActions} mountOnEnter unmountOnExit>
                    <div id="text-actions">
                        <TextActions textContent={this.props.text.content} handleFurigana={this.handleFurigana}
                                     kanjiLevels={this.props.kanjiLevels} textTitle={this.props.text.title}/>
                    </div>
                </Slide>
                <div id="yomi-text-info">
                    <TextInfo text={this.props.text}/>
                </div>
                {(!this.props.analyzedText.words || this.props.analyzedText.words.length === 0) && <LinearProgress id="linear-progress"/>}
                <TextContainer text={this.props.text} id={this.props.match.params.id} analyzedText={this.props.analyzedText}/>
            </div>
        );
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Read);
