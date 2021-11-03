import React from 'react';
import MenuIcon from 'mdi-react/MenuIcon';
import MoreVertIcon from 'mdi-react/MoreVertIcon';

import colors from "../../style/colorConstants";
import BackButton from "../buttons/backBtn/BackButton";
import { showDrawer } from "../drawer/Drawer";
import * as PropTypes from "prop-types";

const getOnClick = (onLeftIconClick) => onLeftIconClick ? onLeftIconClick : showDrawer;

const TopBar = ({leftIcon, rightIcon, onRightIconClick, onLeftIconClick}) => (
    <div id="top-bar">
        {
            leftIcon && leftIcon === "back" ? <BackButton onClick={onLeftIconClick}/> :
                <MenuIcon color={colors.yomiWhite} size="30" onClick={getOnClick(onLeftIconClick)}/>
        }
        { rightIcon && <MoreVertIcon color={colors.yomiWhite} size="30" onClick={onRightIconClick}/> }
    </div>
);

TopBar.propTypes = {
    leftIcon: PropTypes.string,
    rightIcon: PropTypes.string,
    onRightIconClick: PropTypes.func,
    onLeftIconClick: PropTypes.func
};

export default TopBar;
