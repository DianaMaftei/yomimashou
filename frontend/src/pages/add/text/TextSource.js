import React from 'react';
import "./textSource.scss";
import TextSourceTabs from "./TextSourceTabs";
import OCR from "./sources/OCR";
import Subtitles from "./sources/Subtitles";
import TextBox from "./sources/TextBox";
import {useDispatch, useSelector} from "react-redux";
import * as PropTypes from "prop-types";
import {scanImagesAction, setSubtitlesAction, toggleLoaderAction} from "../addActions";

const TextSource = ({setTabValue, onChangeText, onEditorClick, tabValue, text}) => {
    const dispatch = useDispatch();
    const showLoader = useSelector(state => state.add.showLoader);
    const subtitles = useSelector(state => state.add.subtitles);

    const toggleLoader = () => dispatch(toggleLoaderAction());

    const scanImages = (formData, headers) => dispatch(scanImagesAction(formData, headers));

    if (text && showLoader) {
        toggleLoader();
    }

    return (
        <div id="text-source">
            <TextSourceTabs value={tabValue} onChange={(event, newValue) => setTabValue(newValue)}/>

            {{
                0: <TextBox onClick={onEditorClick} content={text} onChange={onChangeText}/>,

                1: <OCR showLoader={showLoader} text={text} scanImages={scanImages} onChangeText={onChangeText}
                        toggleLoader={toggleLoader}/>,

                2: <div id="tab-content-2" aria-labelledby="tab-2" hidden={tabValue !== 2}><h1>To do</h1></div>,

                3: <Subtitles onChangeText={onChangeText} value={subtitles}
                              setSubs={(subtitles) => dispatch(setSubtitlesAction(subtitles))}/>
            }[tabValue]}

        </div>
    );
}

TextSource.propTypes = {
    setTabValue: PropTypes.func.isRequired,
    onChangeText: PropTypes.func.isRequired,
    onEditorClick: PropTypes.func.isRequired,
    tabValue: PropTypes.number.isRequired,
    text: PropTypes.string,
};

export default TextSource;
