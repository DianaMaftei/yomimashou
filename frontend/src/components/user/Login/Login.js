import React from "react";
import axios from "axios/index";
import apiUrl from "../../../AppUrl";

export class Login extends React.Component {
    user = {};

    login() {
        axios.post(apiUrl + '/login', this.user).then(resp => {
            localStorage.setItem('username', this.user.username);
            localStorage.setItem('token', resp.headers['authorization']);
        })
    }

    usernameChange(event) {
        this.user.username = event;
    }

    passwordChange(event) {
        this.user.password = event;
    }

    render() {
        return (
            <div>
                <div className="container">
                    <h1>Login</h1>
                    <p>Please fill in this form to log into your account.</p>
                    <hr/>

                    <label htmlFor="email"><b>Username</b></label>
                    <input type="text" placeholder="Enter Username" name="username" value={this.user.username}
                           onChange={event => this.usernameChange(event.target.value)}/>

                    <label htmlFor="psw"><b>Password</b></label>
                    <input type="password" placeholder="Enter Password" name="psw" value={this.user.password}
                           onChange={event => this.passwordChange(event.target.value)}/>

                    <hr/>

                    <button className="loginbtn" onClick={this.login.bind(this)}>Login</button>
                </div>
            </div>
        )
    }
}

