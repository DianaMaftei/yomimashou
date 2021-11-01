import React from 'react';
import MenuIcon from 'mdi-react/MenuIcon';
import MoreVertIcon from 'mdi-react/MoreVertIcon';

import colors from "../../style/colorConstants";
import BackButton from "../buttons/backBtn/BackButton";
import { showDrawer } from "../drawer/Drawer";

export default ({leftIcon, rightIcon, onRightIconClick, onLeftIconClick}) => (
    <div id="top-bar">
        {
            leftIcon && leftIcon === "back" ? <BackButton onClick={onLeftIconClick}/> :
                <MenuIcon color={colors.yomiWhite} size="30" onClick={onLeftIconClick ? onLeftIconClick : showDrawer}/>
        }
        { rightIcon && <MoreVertIcon color={colors.yomiWhite} size="30" onClick={onRightIconClick}/> }
    </div>
);
