import React from 'react';
import MenuIcon from 'mdi-react/MenuIcon';
import MoreVertIcon from 'mdi-react/MoreVertIcon';
import colors from '../../style/colorConstants';
import BackButton from '../buttons/backBtn/BackButton';
import { showDrawer } from '../drawer/Drawer';


const getOnClick = (onLeftIconClick: any) => onLeftIconClick ? onLeftIconClick : showDrawer;

const TopBar = ({leftIcon, rightIcon, onRightIconClick, onLeftIconClick}: TopBarProps) => (
    <div id="top-bar">
        {
            leftIcon && leftIcon === 'back' ? <BackButton onClick={onLeftIconClick}/> :
                <MenuIcon color={colors.yomiWhite} size="30" onClick={getOnClick(onLeftIconClick)}/>
        }
        {rightIcon && <MoreVertIcon color={colors.yomiWhite} size="30" onClick={onRightIconClick}/>}
    </div>
);

type TopBarProps = {
    leftIcon?: string
    rightIcon?: string
    onRightIconClick?: React.MouseEventHandler<HTMLButtonElement>
    onLeftIconClick?: React.MouseEventHandler<HTMLButtonElement>
}

export default TopBar;
