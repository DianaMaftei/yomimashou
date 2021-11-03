import React from 'react';
import {Button} from "@material-ui/core";
import * as PropTypes from "prop-types";
import "./actionBtn.scss";

const ActionButton = ({onClick, label, disabled}) => (
    <div id="action-btn">
        <Button disabled={disabled} variant="contained" onClick={onClick}> {label} </Button>
    </div>
);

ActionButton.propTypes = {
    onClick: PropTypes.func.isRequired,
    label: PropTypes.string.isRequired,
    disabled: PropTypes.bool
};

export default ActionButton;
