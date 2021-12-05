import React from "react";
import {TextField} from "@material-ui/core/umd/material-ui.development";
import {useDispatch, useSelector} from "react-redux";
import TextSource from "./TextSource";
import ImageUpload from "../image-upload/ImageUpload";
import * as PropTypes from "prop-types";
import {setSourceTabValueAction} from "../addActions";

const Text = ({text, title, setTitle, removePlaceholder, setText}) => {
    const dispatch = useDispatch();
    const sourceTabValue = useSelector(state => state.add.sourceTabValue);

    return (
        <div>
            <TextField required fullWidth id="title-required" label="Title" value={title || ''}
                       onChange={setTitle} margin="normal"/>

            <ImageUpload/>

            <TextSource setTabValue={(sourceTabValue) => dispatch(setSourceTabValueAction(sourceTabValue))}
                        tabValue={sourceTabValue}
                        onEditorClick={removePlaceholder}
                        onChangeText={setText} text={text}/>
        </div>
    );
}

Text.propTypes = {
    text: PropTypes.string,
    title: PropTypes.string,
    setTitle: PropTypes.func.isRequired,
    removePlaceholder: PropTypes.func.isRequired,
    setText: PropTypes.func.isRequired
};

export default Text;
