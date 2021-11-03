import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import './header.scss';
import TopBar from "./TopBar";
import * as PropTypes from "prop-types";

const Header = ({leftIcon, rightIcon, centerText, onRightIconClick, onLeftIconClick, fontSize}) => (
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

Header.propTypes = {
    fontSize: PropTypes.number,
    centerText: PropTypes.string,
    leftIcon: PropTypes.string,
    rightIcon: PropTypes.string,
    onRightIconClick: PropTypes.func,
    onLeftIconClick: PropTypes.func
};

export default Header;
