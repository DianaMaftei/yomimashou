import React from 'react';
import StyleIcon from 'mdi-react/StyleIcon';
import AddIcon from 'mdi-react/AddIcon';
import colors from "../../../style/colorConstants";
import "./addToDeckBtn.scss";

const AddToDeckButton = ({onClick}) => {
    return (
        <span className="add-to-deck-btn" onClick={onClick}>
            <StyleIcon color={colors.yomiDarkBlue} size="30" />
            <AddIcon color={colors.yomiDarkBlue} size="30" />
        </span>
    );
};

export default AddToDeckButton;
