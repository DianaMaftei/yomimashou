import React from "react";
import apiUrl from "../../../AppUrl";
import axios from "axios/index";

export class Register extends React.Component {

    user = {};

    register() {
        console.log(this.user);
        axios.post(apiUrl + '/api/users/register', this.user);
    }

    emailChange(event) {
        this.user.email = event;
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
                    <h1>Register</h1>
                    <p>Please fill in this form to create an account.</p>
                    <hr/>

                    <label htmlFor="email"><b>Email</b></label>
                    <input type="text" placeholder="Enter Email" name="email" value={this.user.email}
                           onChange={event => this.emailChange(event.target.value)}/>

                    <label htmlFor="email"><b>Username</b></label>
                    <input type="text" placeholder="Enter Username" name="username" value={this.user.username}
                           onChange={event => this.usernameChange(event.target.value)}/>

                    <label htmlFor="psw"><b>Password</b></label>
                    <input type="password" placeholder="Enter Password" name="psw" value={this.user.password}
                           onChange={event => this.passwordChange(event.target.value)}/>

                    <hr/>

                    <button className="registerbtn" onClick={this.register.bind(this)}>Register</button>
                </div>
            </div>
        )
    }
}