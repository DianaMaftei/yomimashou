import React from 'react';
import { connect } from "react-redux";
import YomiText from './YomiText/YomiText';
import "./read.css";
import axios from "axios/index";
import {apiUrl} from "../../AppUrl";
import { isAuthenticated, withHeaders } from "../../auth/auth";
import Header from "../`common/header/Header";

const mapStateToProps = (state) => ({
    text: state.add.text
});

const mapDispatchToProps = (dispatch) => ({
    resetDictionaries: () => {
        dispatch({
            type: 'RESET_DICTIONARIES'
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
    },
    toggleTextActionsMenu: () => {
        dispatch({
            type: 'TOGGLE_TEXT_ACTIONS_MENU'
        })
    }
});

export class Read extends React.Component {

    componentDidMount() {
        const { id } = this.props.match.params;
        if (id) {
            if(isAuthenticated()) {
                axios.post(apiUrl + '/api/users/textStatus?progressStatus=OPEN&textId=' +id, {}, withHeaders());
            }

            this.props.getTextById(id);
        } else {
            if (!this.props.text || !this.props.text.content) {
                this.props.history.push('/')
            }
        }

        if (!this.props.text.content) return;
        this.props.getWordsForText(this.props.text.title + " " + this.props.text.content.replace(/<br>/g, ""));
        // this.props.getNamesForText(this.props.text.content);

        this.showTextActions = false;
    }

    componentWillUnmount() {
        this.props.resetDictionaries();
    }

    componentDidUpdate(prevProps, prevState) {
        if (!this.props.text.content) return;

        if (this.props.text.content !== prevProps.text.content) {
            this.props.getWordsForText(this.props.text.title + " " + this.props.text.content.replace(/<br>/g, ""));
            // this.props.getNamesForText(this.props.text.content);
        }
    }

    shouldComponentUpdate(nextProps, nextState) {
        return this.props.text !== nextProps.text;
    };

    render() {
        return (
            <div id="read-page">
                <div id="app-header">
                    <Header leftIcon="menu" rightIcon="more_vert" centerText={this.props.text.title} onRightIconClick={this.props.toggleTextActionsMenu}/>
                </div>
                <YomiText text={this.props.text} id={this.props.match.params.id}/>
            </div>
        )
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Read);