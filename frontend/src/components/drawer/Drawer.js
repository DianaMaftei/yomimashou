import React from 'react';
import "./drawer.css";
import logo from "./appLogo.svg";
import colors from "../../style/colorConstants";
import MaterialIcon from "material-icons-react";
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
                        <span><MaterialIcon icon="dashboard" size="medium" color="inherit"/></span>
                        <span>Texts</span>
                    </div>
                </Link>
                <Link to={"/add"} className="drawer-link" onClick={closeDrawer}>
                    <div style={{color: getLinkColor(pathname, "/add")}}>
                        <span><MaterialIcon icon="add_circle_outline" size="medium" color="inherit"/></span>
                        <span>Add Text</span>
                    </div>
                </Link>
                <Link to={"/decks"} className="drawer-link" onClick={closeDrawer}>
                    <div style={{color: getLinkColor(pathname, "/decks")}}>
                        <span><MaterialIcon icon="psychology" size="medium" color="inherit"/></span>
                        <span>Practice</span>
                    </div>
                </Link>
                <Link to={"/"} className="drawer-link" onClick={closeDrawer}>
                    <div>
                        <span><MaterialIcon icon="assessment" size="medium" color={colors.yomiGray500}/></span>
                        <span>Dashboard</span>
                    </div>
                </Link>
            </div>
            {
                isAuthenticated() &&
                <div id="drawer-bottom">
                    <Link to={"/"} className="drawer-link" onClick={closeDrawer}>
                        <div id="drawer-settings">
                            <span><MaterialIcon icon="settings" size="medium" color={colors.yomiGray500}/></span>
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
