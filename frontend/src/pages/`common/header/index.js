import React from 'react';
import ButtonAppBarWithUsername from "./ButtonAppBarWithUsername";
import ButtonAppBar from "./ButtonAppBar";

export default () => {
    let username = localStorage.getItem('username');
    if (username) return ButtonAppBarWithUsername(username);
    else return ButtonAppBar();
};