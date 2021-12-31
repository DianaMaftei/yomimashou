import {Button} from "@material-ui/core";
import spinner from "../../../read/YomiText/Rikai/spinner.svg";
import Editor from "react-pell";
import * as PropTypes from "prop-types";

const onSelectOcrImages = (e, scanImages, toggleLoader) => {
    if (e.target.files && e.target.files.length > 0) {
        let formData = new FormData();
        for (let i = 0; i < e.target.files.length; i++) {
            formData.append("file", e.target.files[i]);
        }
        scanImages(formData);
        toggleLoader()
    }
}

const formatText = (text) => {
    let paragraphs = text.split("。\n");
    return paragraphs.map((paragraph, index) => {
        let newParagraph = "<div>" + paragraph;
        newParagraph += index < paragraphs.length - 1 ? "。\n" + "</div><br/>" : "</div>";
        return newParagraph;
    }).join("");
}

const OCR = ({showLoader, text, scanImages, onChangeText, toggleLoader}) => {
    return <div id="tab-content-1" aria-labelledby="tab-1">
        {showLoader &&
            <div>
                <img id="spinner" src={spinner} alt=""/>
            </div>
        }

        {!showLoader && !text &&
            <div>
                <h6>Select images that you want to scan and convert to text</h6>
                <br/>
            </div>
        }

        {!showLoader && text &&
            <Editor defaultContent={formatText(text)} actions={[]} actionBarClass="my-custom-class"
                onChange={onChangeText}
                onPaste={onChangeText}/>
        }

        <div>
            <input type="file" accept="image/*" id="outlined-button-ocr" name="ocr" multiple
                   style={{display: "none"}}
                   onChange={(e) => onSelectOcrImages(e, scanImages, toggleLoader)}/>
            <label htmlFor="outlined-button-ocr">
                <Button variant="outlined" style={{textTransform: "none"}} component="span">
                    { text ? 'Reupload' : 'Upload' } images
                </Button>
            </label>
        </div>
    </div>;
}

OCR.propTypes = {
    tabValue: PropTypes.number,
    showLoader: PropTypes.bool,
    text: PropTypes.string,
    scanImages: PropTypes.func,
    defaultContent: PropTypes.string,
    onChangeText: PropTypes.func,
    toggleLoader: PropTypes.func
};

export default OCR;
