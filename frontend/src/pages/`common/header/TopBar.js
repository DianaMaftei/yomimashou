import React from 'react';
import MaterialIcon from 'material-icons-react';
import colors from "../../../style/colorConstants";

export default ({leftIcon, rightIcon, onOptionsClick}) => (
    <div id="top-bar">
        <MaterialIcon icon={leftIcon} color={colors.yomiWhite} size="medium"/>
        <MaterialIcon icon={rightIcon} color={colors.yomiWhite} size="medium" onClick={onOptionsClick}/>
    </div>
);