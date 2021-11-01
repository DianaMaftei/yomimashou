import React from 'react';
import ArrowLeftIcon from 'mdi-react/ArrowLeftIcon';
import colors from "../../../style/colorConstants";
import "./backBtn.scss";

const BackButton = ({onClick}) => {

    return (
        <span className="back-btn" onClick={onClick}>
            <ArrowLeftIcon color={colors.yomiWhite} size="24"/>
        </span>
    );
};

export default BackButton;
