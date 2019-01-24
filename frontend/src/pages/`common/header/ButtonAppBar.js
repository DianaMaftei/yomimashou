import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Button from '@material-ui/core/Button';
import logo from "./appLogo.svg";
import './header.css';
import Link from "react-router-dom/es/Link";

export default () => (
    <div className="header">
        <AppBar position="sticky" style={{ background: 'transparent', boxShadow: 'none' }}>
            <Toolbar>
                <img className="logo" src={logo} alt="logo"/>
                <div className="button">
                    <Link to="/"><Button color="inherit" style={{ textTransform: 'initial', color:'white' }}>Home</Button></Link>
                    <Link to="/login"><Button color="inherit" style={{ textTransform: 'initial', color:'white' }}>Login</Button></Link>
                    <Link to="/register"><Button color="inherit" style={{ textTransform: 'initial', color:'white' }}>Register</Button></Link>
                </div>
            </Toolbar>
        </AppBar>
    </div>
);