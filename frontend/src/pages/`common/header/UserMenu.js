import React from 'react';
import FormControl from "@material-ui/core/es/FormControl/FormControl";
import MenuItem from "@material-ui/core/es/MenuItem/MenuItem";
import Select from "@material-ui/core/es/Select/Select";
import './header.css';
import Link from "react-router-dom/Link";
import {apiUrl} from "../../../AppUrl";
import axios from "axios/index";

const logout = () => {
  localStorage.removeItem('username');
  localStorage.removeItem('token');
  axios.get(apiUrl + '/logout');
};

export default ({username}) => (
    <FormControl className="user-menu">
      <Select
          value={username}
          name="user-menu"
      >
        <MenuItem value={username} disabled>
          <span>{username}</span>
        </MenuItem>
        <MenuItem value="">
          <span><Link to="/decks">Decks</Link></span>
        </MenuItem>
        <MenuItem value="">
          <span onClick={logout}><Link to="/">Logout</Link></span>
        </MenuItem>

      </Select>
    </FormControl>
);