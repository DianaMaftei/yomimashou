import React from 'react';
import * as PropTypes from "prop-types";
import "./iconBtn.scss";
import {Button} from "@material-ui/core";

const IconButton = ({onClick, children, label}) => (
    <div className="icon-btn">
        <Button variant="outlined" component="div" onClick={onClick}>
            {children}
            <span>{label}</span>
        </Button>
    </div>
);

IconButton.propTypes = {
    onClick: PropTypes.func,
    children: PropTypes.element.isRequired,
    label: PropTypes.string.isRequired
};

export default IconButton;
