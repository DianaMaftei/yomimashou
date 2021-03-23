import React from 'react';
import {Button, Tab, Tabs, TextField} from "@material-ui/core";
import Editor from "react-pell";
import "./textSource.css";
import spinner from "../../read/YomiText/Rikai/spinner.svg";

class TextSource extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            subtitles: '',
            showLoader: false
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

    onSelectOcrImages(e, scanImages) {
        if (e.target.files && e.target.files.length > 0) {
            let formData  = new FormData();
            for (let i = 0; i < e.target.files.length; i++) {
                formData.append("file", e.target.files[i]);
            }
            const headers = {
                "Content-Type": "multipart/form-data",
            }
            scanImages(formData, headers);
            this.setState({...this.state, showLoader: true})
        }
    }

    formatText(text) {
        let paragraphs = text.split("。\n");
        return paragraphs.map((paragraph, index) => {
            let newParagraph = "<div>" + paragraph;
            newParagraph += index < paragraphs.length - 1 ? "。\n" + "</div><br/>" : "</div>";
            return newParagraph;
        }).join("");
    }

    render() {
        let {setTabValue, defaultContent, onChangeText, onEditorClick, tabValue, text, scanImages} = this.props;

        if( text && this.state.showLoader) {
            this.setState({...this.state, showLoader: false})
        }

        return (
            <div id="text-source">
                <Tabs value={tabValue} onChange={(event, newValue) => setTabValue(newValue)}
                      variant="scrollable" scrollButtons="off" className="text-source-tabs">
                    <Tab label="EDITOR" id="tab-0" aria-controls="tab-content-0" selected="true"/>
                    <Tab label="OCR" id="tab-1" aria-controls="tab-content-1"/>
                    <Tab label="MANGA" id="tab-2" aria-controls="tab-content-2"/>
                    <Tab label="SUBS" id="tab-3" aria-controls="tab-content-3"/>
                </Tabs>
                <br/>

                {/*EDITOR*/}
                <div id="tab-content-0" aria-labelledby="tab-0" hidden={tabValue !== 0}>
                    <h6>Copy and paste here the text you want to read</h6>
                    <div onClick={onEditorClick}>
                        <Editor defaultContent={defaultContent} actions={[]} actionBarClass="my-custom-class"
                                onChange={onChangeText}
                                onPaste={onChangeText}/>
                    </div>
                </div>
                {/*OCR*/}
                <div id="tab-content-1" aria-labelledby="tab-1" hidden={tabValue !== 1}>
                    { !this.state.showLoader && !text &&
                        <div>
                            <h6>Select images that you want to scan and convert to text</h6>
                            <br/>
                            <input type="file" accept="image/*" id="outlined-button-ocr" name="ocr" multiple style={{display: 'none'}}
                                   onChange={event => this.onSelectOcrImages(event, scanImages)}/>
                            <label htmlFor="outlined-button-ocr">
                                <Button variant="outlined" style={{textTransform: 'none'}} component="span">
                                    Upload images
                                </Button>
                            </label>
                        </div>
                    }

                    { this.state.showLoader &&
                            <div>
                                <img id="spinner" src={spinner} alt=""/>
                            </div>
                    }

                    { text && !this.state.showLoader &&
                        <div>
                            <Editor defaultContent={this.formatText(text)} actions={[]} actionBarClass="my-custom-class"
                                    onChange={onChangeText}
                                    onPaste={onChangeText}/>

                            <input type="file" accept="image/*" id="outlined-button-ocr-re" name="ocr" multiple style={{display: 'none'}}
                                   onChange={event => this.onSelectOcrImages(event, scanImages)}/>
                            <label htmlFor="outlined-button-ocr-re">
                                <Button variant="outlined" style={{textTransform: 'none'}} component="span">
                                    Re upload images
                                </Button>
                            </label>
                        </div>
                    }

                </div>
                {/*MANGA*/}
                <div id="tab-content-2" aria-labelledby="tab-2" hidden={tabValue !== 2}>
                    <h1>To do</h1>
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
