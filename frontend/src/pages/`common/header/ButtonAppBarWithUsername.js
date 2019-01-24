import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Button from '@material-ui/core/Button';
import logo from "./appLogo.svg";
import './header.css';
import Link from "react-router-dom/es/Link";
import userIcon from "../../../userIcon.png";
import apiUrl from "../../../AppUrl";
import axios from "axios/index";
import FormControl from "@material-ui/core/es/FormControl/FormControl";
import MenuItem from "@material-ui/core/es/MenuItem/MenuItem";
import Select from "@material-ui/core/es/Select/Select";


const logout = () => {
    localStorage.removeItem('username');
    localStorage.removeItem('token');
    axios.get(apiUrl + '/logout');
};

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
                    <FormControl className="user-menu">
                        <Select
                            value={username}
                            displayEmpty
                            name="user-menu"
                        >
                            <MenuItem value={username} disabled>
                                <span>{username}</span>
                            </MenuItem>
                            <MenuItem value="">
                                <span onClick={logout}><Link to="/">Logout</Link></span>
                            </MenuItem>
                        </Select>
                    </FormControl>

                </div>
            </Toolbar>
        </AppBar>
    </div>
);