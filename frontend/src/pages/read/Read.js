import React from 'react';
import { Link } from 'react-router-dom';
import { connect } from "react-redux";
import YomiText from '../../containers/YomiText/YomiText';
import "./read.css";
import axios from "axios/index";
import apiUrl from "../../AppUrl";

const mapStateToProps = (state) => ({
    text: state.add.text
});

const mapDispatchToProps = (dispatch) => ({
    resetText: () => {
        dispatch({
            type: 'RESET_TEXT'
        });
    },
    getWordsForText: (text) => {
        dispatch({
            type: 'PARSE_TEXT_WORDS',
            payload: axios.post(apiUrl + '/api/text/parse/words', text)
        })
    },
    getTextById: id => {
        dispatch({
            type: 'GET_TEXT_BY_ID',
            payload: axios.get(apiUrl + '/api/text/' + id)
        });
    },
    getNamesForText: (text) => {
        dispatch({
            type: 'PARSE_TEXT_NAMES',
            payload: axios.post(apiUrl + '/api/text/parse/names', text)
        })
    }
});

export class Read extends React.Component {

    componentDidMount() {
        const { id } = this.props.match.params;
        if(id) {
            this.props.getTextById(id);
        } else {
            if(!this.props.text.content) {
                this.props.history.push('/')
            }
        }
    }

    componentDidUpdate(prevProps, prevState) {
        if(!this.props.text.content) return;

        if (this.props.text.content !== prevProps.text.content) {
            this.props.getWordsForText(this.props.text.content.replace(/<br>/g, ""));
            // this.props.getNamesForText(this.props.text.content);
        }
    }

    shouldComponentUpdate(nextProps, nextState) {
        return this.props.text !== nextProps.text;
    };

    render() {
        return (
            <div id="read-page">
                <div id="user-options">
                    <h4>Things you can do:</h4>
                    <ul>
                        <li>Hover over the text to detect words in the dictionary and click to see the
                            definition.
                        </li>
                        <li>Press 'S' to switch between available dictionaries (words / kanji).</li>
                        <li>Click 'Ex' on word definitions to see example sentences.</li>
                    </ul>
                </div>
                <YomiText text={this.props.text}/>
                <div id="reset-btn">
                    <Link to="/add">
                        <button onClick={this.props.resetText}> Try a different text</button>
                    </Link>
                </div>
            </div>
        )
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Read);