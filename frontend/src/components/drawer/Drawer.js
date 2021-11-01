import React from 'react';
import "./drawer.scss";
import logo from "./appLogo.svg";
import colors from "../../style/colorConstants";
import ViewDashboardIcon from 'mdi-react/ViewDashboardIcon';
import AddCircleOutlineIcon from 'mdi-react/AddCircleOutlineIcon';
import BrainIcon from 'mdi-react/BrainIcon';
import AssessmentIcon from 'mdi-react/AssessmentIcon';
import SettingsIcon from 'mdi-react/SettingsIcon';

import {withRouter, Link} from "react-router-dom";
import {isAuthenticated} from "../../auth/auth";

export const showDrawer = (event) => {
    const container = document.getElementById('router-container');
    event.stopPropagation();
    event.preventDefault();
    container.classList.remove(...container.classList);
    container.classList.add("router-container");
    container.classList.add("push-effect");
    container.classList.add("drawer-open");
}

export const closeDrawer = () => {
    setTimeout(function () {
        const container = document.getElementById('router-container');
        container.classList.remove(...container.classList);
        container.classList.add("router-container");
        container.classList.add("push-effect");
    }, 25);
};

function getLinkColor(pathname, pageUrl) {
    return pathname === pageUrl ? colors.yomiWhite : colors.yomiGray500;
}

const Drawer = ({history}) => {
    let pathname = history.location.pathname;
    history.listen((location) => {
        pathname = location.pathname;
    });

    return (
        <div id="drawer" key={pathname}>
            <div id="drawer-top">
                <img className="logo" src={logo} alt="logo"/>
            </div>
            <div id="drawer-center">
                <Link to={"/"} className="drawer-link" onClick={closeDrawer}>
                    <div style={{color: getLinkColor(pathname, "/")}}>
                        <span><ViewDashboardIcon size="42" color="inherit"/></span>
                        <span>Texts</span>
                    </div>
                </Link>
                <Link to={"/add"} className="drawer-link" onClick={closeDrawer}>
                    <div style={{color: getLinkColor(pathname, "/add")}}>
                        <span><AddCircleOutlineIcon size="42" color="inherit"/></span>
                        <span>Add Text</span>
                    </div>
                </Link>
                <Link to={"/decks"} className="drawer-link" onClick={closeDrawer}>
                    <div style={{color: getLinkColor(pathname, "/decks")}}>
                        <span><BrainIcon size="42" color="inherit"/></span>
                        <span>Practice</span>
                    </div>
                </Link>
                <Link to={"/"} className="drawer-link" onClick={closeDrawer}>
                    <div>
                        <span><AssessmentIcon size="42" color={colors.yomiGray500}/></span>
                        <span>Dashboard</span>
                    </div>
                </Link>
            </div>
            {
                isAuthenticated() &&
                <div id="drawer-bottom">
                    <Link to={"/"} className="drawer-link" onClick={closeDrawer}>
                        <div id="drawer-settings">
                            <span><SettingsIcon size="42" color={colors.yomiGray500}/></span>
                            <span>Settings</span>
                        </div>
                    </Link>
                    <div>|</div>
                    <div>
                        <span>Logout</span>
                    </div>
                </div>
            }

            {
                !isAuthenticated() &&
                <div id="drawer-bottom">
                    <Link to={"/login"} className="drawer-link" onClick={closeDrawer}>
                        <span style={{paddingRight:"20px"}}>Login</span>
                    </Link>
                    <div>|</div>
                    <Link to={"/register"} className="drawer-link" onClick={closeDrawer}>
                        <span>Register</span>
                    </Link>
                </div>
            }

        </div>
    );
};

export default withRouter(Drawer);
