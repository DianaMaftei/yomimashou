import React from 'react';
import MaterialIcon from 'material-icons-react';
import colors from "../../../style/colorConstants";
import BackButton from "../buttons/backBtn/BackButton";

export default ({leftIcon, rightIcon, onRightIconClick, onLeftIconClick}) => (
    <div id="top-bar">
        {
            leftIcon && leftIcon === "back" ?
                <BackButton onClick={onLeftIconClick}/> :
                <MaterialIcon icon={leftIcon} color={colors.yomiWhite} size="medium" onClick={onLeftIconClick}/>
        }
        { rightIcon && <MaterialIcon icon={rightIcon} color={colors.yomiWhite} size="medium" onClick={onRightIconClick}/> }
    </div>
);