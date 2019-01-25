import React from 'react';
import TextCardTopWithImage from "./TextCardTopWithImage";
import TextCardTopNoImage from "./TextCardTopNoImage";

export default ({text}) => {
    if(!text) return <div/>

    if(text.image) {
        return TextCardTopWithImage(text);
    }

    return TextCardTopNoImage(text);
}