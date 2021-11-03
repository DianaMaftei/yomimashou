import React from 'react';
import StyleIcon from 'mdi-react/StyleIcon';
import AddIcon from 'mdi-react/AddIcon';
import * as PropTypes from "prop-types";
import "./addToDeckBtn.scss";

const AddToDeckButton = ({onClick}) => (
    <span className="add-to-deck-btn" onClick={onClick}>
            <StyleIcon size="30"/>
            <AddIcon size="30"/>
    </span>
);

AddToDeckButton.propTypes = {
    onClick: PropTypes.func.isRequired
};

export default AddToDeckButton;
