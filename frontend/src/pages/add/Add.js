import React from 'react';
import "./add.css";

import { Divider } from "@material-ui/core/umd/material-ui.development";
import axios from "axios/index";
import {apiUrl} from "../../AppUrl";
import { connect } from "react-redux";
import { withRouter } from 'react-router-dom'
import ActionButtons from "./ActionButtons";
import Tags from "./Tags";
import Series from "./Series";
import Text from "./text/Text";

const mapStateToProps = (state) => ({
    text: state.add.text,
    tagInput: state.add.tagInput,
    textImage: state.add.textImage
});

const stripRubyAndFormatting = html => {
    let doc = new DOMParser().parseFromString(html, "text/html");

    let rubyStart = /(?=<ruby)(.*?)(?:><rb>)/g;
    let rubyEnd = /(<\/rb><rt)(.*?)(<\/ruby>)/g;
    let text = doc.body.innerHTML.replace(rubyStart, '');
    text = text.replace(rubyEnd, '');

    text = text.replace(/<\/p><p/g, '</p>\n<p');

    return text.replace(/\n/g, '<br>');
};

const mapDispatchToProps = (dispatch) => ({
    setTextToEmptyString: () => {
        dispatch({
            type: 'SET_TEXT',
            text: {
                content: ''
            }
        });
    }, resetText: () => {
        dispatch({
            type: 'RESET_TEXT'
        });
    }, setText: (html) => {
        dispatch({
            type: 'SET_TEXT',
            text: {
                content: stripRubyAndFormatting(html)
            }
        });
    }, setTitle: (event) => {
        dispatch({
            type: 'SET_TEXT_TITLE',
            title: event.target.value
        });
    }, setTags: (tags) => {
        dispatch({
            type: 'SET_TEXT_TAGS',
            tags
        });
    }, setTagInput: (tag) => {
        dispatch({
            type: 'SET_TAG_INPUT',
            tag
        });
    }
});

class Add extends React.Component {

    constructor(props) {
        super(props);

        this.showTextPlaceholder = true;
    }

    componentWillMount() {
        let username = localStorage.getItem('username');
        if (!username) {
            this.props.history.push('/');
        }
    }

    componentDidMount() {
        this.props.resetText();
    }


    disableAddBtn() {
        let textHasNoTitle = !this.props.text.title || this.props.text.title.length === 0;
        let textHasNoContent = !this.props.text.content || this.props.text.content.trim().length === 0;
        return textHasNoTitle || textHasNoContent;
    }

    addTag() {
        if (!this.props.text.tags) {
            this.props.text.tags = [];
        }
        if (this.props.tagInput && this.props.tagInput.trim().length > 0) {
            this.props.text.tags.push(this.props.tagInput);
            this.props.setTags(this.props.text.tags);
            this.props.setTagInput("");
        }
    }

    updateTag(event) {
        this.props.setTagInput(event.target.value);
    }

    deleteTag(index) {
        this.props.text.tags.splice(index, 1);
        this.props.setTags(this.props.text.tags);
    }

    submitText() {
        let inputText = this.props.text;
        const username = localStorage.getItem('username');
        const token = localStorage.getItem('token');

        let doc = new DOMParser().parseFromString(this.props.text.content, "text/html");
        doc.body.innerHTML = doc.body.innerHTML.replace(/<\/p><p/g, '</p>\n<p');
        let newtext = doc.body.innerText.replace(/\n/g, '<br>');
        this.props.setText(newtext);

        inputText.content = newtext;

        let data = new FormData();
        data.append('file', this.props.textImage);
        data.append('text', new Blob([JSON.stringify(inputText)], {type: "application/json"}));

        axios.post(apiUrl + '/api/text', data, { headers: {Authorization: token}}).then((response) => {
            this.props.history.push('/read/' + response.data.id);
        });
    }

    removePlaceholder() {
        if (this.showTextPlaceholder) {
            this.props.setTextToEmptyString();
            this.showTextPlaceholder = false;
        }
    }

    render() {
        let editorContent = this.props.text.content != null ?
            this.props.text.content : '<p id="default-content">Paste your text here</p>';

        return (
            <div id="add-page">
                <Text title={this.props.text.title} setTitle={this.props.setTitle.bind(this)}
                      removePlaceholder={this.removePlaceholder.bind(this)} editorContent={editorContent}
                      setText={this.props.setText.bind(this)}/>

                <div className="add-action-footer">
                    <Tags tagInput={this.props.tagInput} updateTag={this.updateTag.bind(this)}
                          addTag={this.addTag.bind(this)} tags={this.props.text.tags}
                          deleteTag={this.deleteTag.bind(this)}/>
                    <Series/>
                    <Divider/>
                    <ActionButtons disableAddBtn={this.disableAddBtn()} submitText={this.submitText.bind(this)}/>
                </div>
            </div>
        );
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Add));