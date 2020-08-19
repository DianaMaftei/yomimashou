import React from 'react';
import MaterialIcon from "material-icons-react";
import colors from "../../../../style/colorConstants";
import "./backBtn.css";

const BackButton = ({onClick}) => {

    return (
        <span className="back-btn" >
            <MaterialIcon icon="keyboard_backspace" color={colors.yomiWhite} size="small" onClick={onClick}/>
        </span>
    );
};

export default BackButton;
