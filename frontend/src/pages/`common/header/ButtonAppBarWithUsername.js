import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Button from '@material-ui/core/Button';
import logo from "./appLogo.svg";
import './header.css';
import Link from "react-router-dom/es/Link";
import userIcon from "../../../resources/userIcon.png";
import UserMenu from "./UserMenu";

export default (username) => (
    <div className="header">
        <AppBar position="sticky" style={{background: 'transparent', boxShadow: 'none'}}>
            <Toolbar>
                <img className="logo" src={logo} alt="logo"/>
                <div className="button">
                    <Link to="/"><Button color="inherit"
                                         style={{textTransform: 'initial', color: 'white'}}>Home</Button></Link>
                    <Link to="/add"><Button color="inherit"
                                            style={{textTransform: 'initial', color: 'white'}}>Add</Button></Link>
                    <img className="userIcon" src={userIcon} alt="userIcon"/>
                    <UserMenu username={username}/>
                </div>
            </Toolbar>
        </AppBar>
    </div>
);