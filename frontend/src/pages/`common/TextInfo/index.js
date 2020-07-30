import React from 'react';
import TextInfoWithImage from "./TextInfoWithImage";
import TextInfoNoImage from "./TextInfoNoImage";

export default ({text}) => {
    if(!text) return <div/>

    if(text.imageFileName) {
        return TextInfoWithImage(text);
    }

    return TextInfoNoImage(text);
}