import React from 'react';
import TextCardTopWithImage from "./TextCardTopWithImage";
import TextCardTopNoImage from "./TextCardTopNoImage";

export default ({text}) => {
    if(!text) return <div/>

    if(text.imageFileName) {
        return TextCardTopWithImage(text);
    }

    return TextCardTopNoImage(text);
}