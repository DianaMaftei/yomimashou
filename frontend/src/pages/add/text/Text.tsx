import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import TextSource from './TextSource';
import ImageUpload from '../image-upload/ImageUpload';
import { setSourceTabValueAction } from '../addActions';
import { TextField } from '@material-ui/core';


const Text = ({text, title, setTitle, removePlaceholder, setText}: TextProps) => {
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
};

type TextProps = {
    text?: string
    title?: string
    setTitle: any
    removePlaceholder: any
    setText: any
}

export default Text;
