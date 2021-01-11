import React from 'react';
import {Button, Tab, Tabs, TextField} from "@material-ui/core";
import Editor from "react-pell";


class TextSource extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            subtitles: ''
        };
    }

    stripUnnecessaryNumbers (text) {
        const regex = /^[0-9]*(?=\n)/gm;
        let strippedText = text.replace(regex, '');
        strippedText = strippedText.replace(/^\n+/gm, '\r\n')
        return strippedText.trim();
    }

    onSelectFile(e, onChangeText) {
        if (e.target.files && e.target.files.length > 0) {
            const reader = new FileReader();
            reader.addEventListener("load", () =>
                this.setSubsText(reader.result, onChangeText)
            )
            reader.readAsText(e.target.files[0]);
        }
    }

    setSubsText(text, onChangeText) {
        const strippedText = this.stripUnnecessaryNumbers(text);
        this.setState({...this.state, subtitles: strippedText})
        onChangeText(strippedText);
    }

    render() {
        let {setTabValue, defaultContent, onChangeText, onEditorClick, tabValue} = this.props;

        return (
            <div id="text-source">
                <Tabs value={tabValue} onChange={(event, newValue) => setTabValue(newValue)}
                      variant="scrollable" scrollButtons="off" className="text-source-tabs">
                    <Tab label="EDITOR" id="tab-0" aria-controls="tab-content-0" selected="true"/>
                    <Tab label="OCR" id="tab-1" aria-controls="tab-content-1"/>
                    <Tab label="MANGA" id="tab-2" aria-controls="tab-content-2"/>
                    <Tab label="SUBS" id="tab-3" aria-controls="tab-content-3"/>
                </Tabs>

                {/*EDITOR*/}
                <div id="tab-content-0" aria-labelledby="tab-0" hidden={tabValue !== 0}>
                    <div onClick={onEditorClick}>
                        <Editor defaultContent={defaultContent} actions={[]} actionBarClass="my-custom-class"
                                onChange={onChangeText}
                                onPaste={onChangeText}/>
                    </div>
                </div>
                {/*OCR*/}
                <div id="tab-content-1" aria-labelledby="tab-1" hidden={tabValue !== 1}>
                    <h1>OCR</h1>
                </div>
                {/*MANGA*/}
                <div id="tab-content-2" aria-labelledby="tab-2" hidden={tabValue !== 2}>
                    <h1>MANGA</h1>
                </div>
                {/*SUBS*/}
                <div id="tab-content-3" aria-labelledby="tab-3" hidden={tabValue !== 3}>
                    <input accept=".srt" id="outlined-button-subs" name="subs" type="file" style={{display: 'none'}}
                           onChange={(event) => this.onSelectFile.bind(this)(event, onChangeText)}/>
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
                        value={this.state.subtitles}
                        onChange={(event) => this.setSubsText(event.target.value, onChangeText)}
                        style={{width: '100%'}}
                    />
                </div>
            </div>
        );
    }
}

export default TextSource;
