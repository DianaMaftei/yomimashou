import React from 'react';
import "./drawer.css";
import logo from "./appLogo.svg";
import colors from "../../../style/colorConstants";
import MaterialIcon from "material-icons-react";
import Link from "react-router-dom/Link";

export const showDrawer = (event) => {
    var container = document.getElementById( 'router-container' );
    event.stopPropagation();
    event.preventDefault();
    container.classList.remove(...container.classList);
    container.classList.add("router-container");
    container.classList.add("push-effect");
    container.classList.add("drawer-open");
}

export const closeDrawer = () => {
    setTimeout( function() {
        var container = document.getElementById( 'router-container' );
        container.classList.remove(...container.classList);
        container.classList.add("router-container");
        container.classList.add("push-effect");
    }, 25 );
};

const Drawer = () => {
    return (
        <div id="drawer">
            <div id="drawer-top">
                <img className="logo" src={logo} alt="logo"/>
            </div>
            <div id="drawer-center">
                <Link to={"/"} className="drawer-link" onClick={closeDrawer}>
                    <div>
                        <span><MaterialIcon icon="dashboard" size="medium" color={colors.yomiWhite}/></span>
                        <span>Texts</span>
                    </div>
                </Link>
                <Link to={"/add"} className="drawer-link" onClick={closeDrawer}>
                    <div>
                        <span><MaterialIcon icon="add_circle_outline" size="medium" color={colors.yomiGray500}/></span>
                        <span>Add Text</span>
                    </div>
                </Link>
                <Link to={"/decks"} className="drawer-link" onClick={closeDrawer}>
                    <div>
                        <span><MaterialIcon icon="psychology" size="medium" color={colors.yomiGray500}/></span>
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
        </div>
    );
};

export default Drawer;
