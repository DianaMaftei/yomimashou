import React from 'react';
import ArrowLeftIcon from 'mdi-react/ArrowLeftIcon';
import * as PropTypes from "prop-types";
import "./backBtn.scss";

const BackButton = ({onClick}) => (
    <span className="back-btn" onClick={onClick}>
        <ArrowLeftIcon size="24"/>
    </span>
);

BackButton.propTypes = {
    onClick: PropTypes.func.isRequired
};

export default BackButton;
