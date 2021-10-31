import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import './header.scss';
import TopBar from "./TopBar";

export default ({leftIcon, rightIcon, centerText, onRightIconClick, onLeftIconClick, fontSize}) => (
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
