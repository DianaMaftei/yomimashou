import React from 'react';
import "./add.css";
import Editor from 'react-pell';

import {
    Button,
    Checkbox,
    Chip,
    Divider,
    FormControl,
    FormControlLabel,
    MenuItem,
    Select,
    Switch,
    TextField
} from "@material-ui/core/umd/material-ui.development";
import axios from "axios/index";
import apiUrl from "../../AppUrl";
import { connect } from "react-redux";
import { Link } from "react-router-dom/umd/react-router-dom";

const mapStateToProps = (state) => ({
    text: state.add.text,
    tagInput: state.add.tagInput
});

const stripRubyAndFormatting = html => {
    let doc = new DOMParser().parseFromString(html, "text/html");

    let rubyStart = /(?=<ruby)(.*?)(?:>)/g;
    let rubyEnd = /(<rt)(.*?)(<\/ruby>)/g;
    doc.body.innerHTML = doc.body.innerHTML.replace(rubyStart, '');
    doc.body.innerHTML = doc.body.innerHTML.replace(rubyEnd, '');

    doc.body.innerHTML = doc.body.innerHTML.replace(/<\/p><p/g, '</p>\n<p');

    return doc.body.innerHTML.replace(/\n/g, '<br>');
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

export class Add extends React.Component {

    constructor(props) {
        super(props);

        this.showTextPlaceholder = true;
    }

    componentDidMount() {
        this.props.resetText();
    }

    shouldComponentUpdate(nextProps, nextState) {
        return this.props.text !== nextProps.text;
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
        let text = this.props.text;
        const username = localStorage.getItem('username');
        const token = localStorage.getItem('token');
        text.user = {
            username: username
        };

        let doc = new DOMParser().parseFromString(this.props.text.content, "text/html");
        doc.body.innerHTML = doc.body.innerHTML.replace(/<\/p><p/g, '</p>\n<p');
        let newtext = doc.body.innerText.replace(/\n/g, '<br>');
        this.props.setText(newtext);

        text.content = newtext;

        axios.post(apiUrl + '/api/text', text, { headers: { Authorization: token } });
    }

    removePlaceholder() {
        if (this.showTextPlaceholder) {
            this.props.setTextToEmptyString();
            this.showTextPlaceholder = false;
        }
    }

    render() {
        let editorContent = this.props.text.content != null ? this.props.text.content : '<p id="default-content">Paste your text here</p>';

        return (
            <div id="add-page">
                <h1 style={{ textAlign: 'center' }}>Add a text to read</h1>
                <TextField
                    required
                    fullWidth
                    id="title-required"
                    label="Title"
                    value={this.props.text.title || ''}
                    onChange={this.props.setTitle}
                    margin="normal"
                />
                <input
                    accept="image/*"
                    id="outlined-button-file"
                    multiple
                    type="file"
                    style={{ display: 'none' }}
                />
                <label htmlFor="outlined-button-file">
                    <Button variant="outlined" component="span" disabled>
                        Upload image
                    </Button>
                </label>

                <div onClick={() => this.removePlaceholder()}>
                    <Editor
                        defaultContent={editorContent}
                        actions={[]}
                        actionBarClass="my-custom-class"
                        onChange={this.props.setText}
                        onPaste={this.props.setText}
                    />
                </div>

                <div className="add-action-footer">
                    <div>
                        <h6>Tags</h6>
                        <TextField
                            id="tags"
                            value={this.props.tagInput}
                            onChange={this.updateTag.bind(this)}
                            disabled
                        />
                        <Button variant="outlined" component="span" onClick={this.addTag.bind(this)} disabled>
                            Add
                        </Button>
                        <div className="chips-list">
                            {this.props.text.tags && this.props.text.tags.map((tag, index) =>
                                <Chip
                                    key={index}
                                    label={tag}
                                    variant="outlined"
                                    color="primary"
                                    onDelete={() => this.deleteTag(index)}
                                    className="tag-chip"
                                />
                            )}
                        </div>
                        <div className="add-text-series">
                            <FormControlLabel value="series" control={<Checkbox/>} label="Series" disabled/>
                            <div>
                                <FormControl disabled>
                                    <Select
                                        value="ceva"
                                        onChange={() => {
                                        }}
                                        style={{ width: 200 }}
                                    >
                                        <MenuItem value="Ten">Ten</MenuItem>
                                        <MenuItem value="Twenty">Twenty</MenuItem>
                                        <MenuItem value="Thirty">Thirty</MenuItem>
                                    </Select>
                                </FormControl>
                            </div>
                        </div>
                    </div>
                    <Divider/>
                    <div className="add-action-buttons">
                        <Button variant="outlined" component="span" disabled> Preview </Button>
                        <div>
                            <Switch
                                checked={true}
                                onChange={(ceva) => {
                                    console.log(ceva)
                                }}
                                color="primary"
                                disabled
                            />
                            <span>public</span>
                        </div>
                        <Button variant="contained"
                                color="primary"
                                component="span"
                                disabled={this.disableAddBtn()}
                                onClick={this.submitText.bind(this)}
                        >
                            <Link to="/read">
                                Add & Read
                            </Link>
                        </Button>
                    </div>
                </div>
            </div>
        );
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Add);