import {Button, TextField} from "@material-ui/core";
import * as PropTypes from "prop-types";
import React from "react";

const onSelectFile = (e, onChangeText) => {
    if (e.target.files && e.target.files.length > 0) {
        const reader = new FileReader();
        reader.addEventListener("load", () =>
            setSubsText(reader.result, onChangeText)
        )
        reader.readAsText(e.target.files[0]);
    }
}

const stripUnnecessaryNumbers = (text) => {
    const regex = /^[0-9]*(?=\n)/gm;
    let strippedText = text.replace(regex, '');
    strippedText = strippedText.replace(/^\n+/gm, '\r\n')
    return strippedText.trim();
}

const setSubsText = (text, onChangeText, setSubs) => {
    const strippedText = stripUnnecessaryNumbers(text);
    setSubs(strippedText);
    onChangeText(strippedText);
}

const Subtitles = ({onChangeText, value, setSubs}) => {
    return <div id="tab-content-3" aria-labelledby="tab-3">
        <input accept=".srt" id="outlined-button-subs" name="subs" type="file" style={{display: "none"}}
               onChange={(e) => onSelectFile(e, onChangeText)}/>
        <label htmlFor="outlined-button-subs">
            <Button variant="outlined" component="span">
                Upload subtitles
            </Button>
        </label>
        <TextField
            id="outlined-multiline-static"
            label="Subtitles"
            multiline
            rows={15}
            variant="outlined"
            value={value}
            onChange={(e) => setSubsText(e.target.value, onChangeText, setSubs)}
            style={{width: "100%"}}
        />
    </div>;
}

Subtitles.propTypes = {
    tabValue: PropTypes.number,
    onChangeText: PropTypes.func,
    value: PropTypes.string,
    setSubs: PropTypes.func
};

export default Subtitles;
