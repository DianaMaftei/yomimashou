import AppBar from '@material-ui/core/AppBar';
import React from 'react';
import './header.scss';
import TopBar from './TopBar';


const Header = ({leftIcon, rightIcon, centerText, onRightIconClick, onLeftIconClick, fontSize}: HeaderProps) => (
    <div className="header">
        <AppBar position="sticky" style={{background: 'transparent', boxShadow: 'none'}}>
            <div>
                <div id="header-rectangle"/>
                <div id="header-oval"/>
            </div>
            <TopBar leftIcon={leftIcon} rightIcon={rightIcon} onRightIconClick={onRightIconClick} onLeftIconClick={onLeftIconClick}/>
            <h2 id="header-text" style={{fontSize: fontSize}}>{centerText}</h2>
        </AppBar>
    </div>
);

type HeaderProps = {
    fontSize?: number
    centerText?: string
    leftIcon?: string
    rightIcon?: string
    onRightIconClick?: React.MouseEventHandler<HTMLButtonElement>
    onLeftIconClick?: React.MouseEventHandler<HTMLButtonElement>
}

export default Header;
