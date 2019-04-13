import React from "react";
import Editor from 'react-pell';

import {Button, TextField} from "@material-ui/core/umd/material-ui.development";

export default ({title, setTitle, removePlaceholder, editorContent, setText}) => (
    <div>
        <h1 style={{textAlign: 'center'}}>Add a text to read</h1>
        <TextField required fullWidth id="title-required" label="Title" value={title || ''}
                   onChange={setTitle} margin="normal"/>

        <input accept="image/*" id="outlined-button-file" multiple type="file" style={{display: 'none'}}/>
        <label htmlFor="outlined-button-file">
            <Button variant="outlined" component="span" disabled>
                Upload image
            </Button>
        </label>

        <h6><a href="https://anatolt.ru/t/del-timestamp-srt.html" target="_blank">Subtitles?</a></h6>
        <div onClick={removePlaceholder}>
            <Editor defaultContent={editorContent} actions={[]} actionBarClass="my-custom-class"
                    onChange={setText} onPaste={setText}/>
        </div>
    </div>
);