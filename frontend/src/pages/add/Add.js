import React from 'react';
import 'react-trumbowyg/dist/trumbowyg.min.css';
import "./add.css";
import Trumbowyg from 'react-trumbowyg'

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

const modules = {
    toolbar: [
        [{ 'header': [1, 2, false] }],
        ['bold', 'italic', 'underline', 'strike', 'blockquote'],
        ['clean']
    ]
};

const mapStateToProps = (state) => ({
    text: state.add.text,
    tagInput: state.add.tagInput
});

const mapDispatchToProps = (dispatch) => ({
    setText: (content, delta, source, editor) => {
        dispatch({
            type: 'SET_TEXT',
            text: {
                plain: editor.getText(),
                formatted: content
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

    constructor() {
        super();
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

        axios.post(apiUrl + '/api/text', text, { headers: { Authorization: token } })
            .then(resp => console.log(resp.data));

        // axios.post(apiUrl + '/api/text/parse/names', text)

    }

    render() {
        return (
            <div id="add-page">
                <h1 style={{ textAlign: 'center' }}>Add a text to read</h1>
                <TextField
                    required
                    fullWidth
                    id="title-required"
                    label="Title"
                    defaultValue={this.props.text.title || ''}
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
                    <Button variant="outlined" component="span">
                        Upload image
                    </Button>
                </label>

                <ReactQuill
                    value={this.props.text.formatted || ''}
                    modules={modules}
                    onChange={this.props.setText}
                    onPaste={this.props.setText}
                />

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