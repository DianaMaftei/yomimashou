import React from 'react';
import MaterialIcon from "material-icons-react";
import colors from "../../../style/colorConstants";
import "./addToDeckBtn.css";

const AddToDeckButton = ({size}) => {

    return (
        <span className="add-to-deck-btn" >
            <MaterialIcon icon="style" color={colors.yomiDarkBlue} size={size} />
            <MaterialIcon icon="add" color={colors.yomiDarkBlue} size={size} />
        </span>
    );
};

export default AddToDeckButton;
