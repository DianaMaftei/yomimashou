import React from "react";
import apiUrl from "../../AppUrl";
import axios from "axios/index";
import Person from '@material-ui/icons/Person';
import Email from '@material-ui/icons/Email';
import Lock from '@material-ui/icons/Lock';
import "../../style/signIn.css";
import { withRouter } from 'react-router-dom'

class Register extends React.Component {

    user = {};

    register() {
        axios.post(apiUrl + '/api/users/register', this.user).then(() => {
            axios.post(apiUrl + '/login', this.user).then(resp => {
                localStorage.setItem('username', this.user.username);
                localStorage.setItem('token', resp.headers['authorization']);
                this.props.history.push('/');
            })
        });
    }

    emailChange(event) {
        this.user.email = event.target.value;
    }

    usernameChange(event) {
        this.user.username = event.target.value;
    }

    passwordChange(event) {
        this.user.password = event.target.value;
    }

    render() {
        return (
            <div>
                <div className="signIn-container">
                    <h1 id="signIn-heading">Register</h1>

                    <div className="input-group mb-3">
                        <div className="input-group-prepend">
                            <span className="input-group-text" id="basic-addon1">
                               <Email fontSize="small"/>
                            </span>
                        </div>
                        <input type="email" className="form-control" placeholder="Email" aria-label="Email"
                               aria-describedby="basic-addon1"
                               value={this.user.email}
                               onChange={this.emailChange.bind(this)}/>
                    </div>

                    <br/>

                    <div className="input-group mb-3">
                        <div className="input-group-prepend">
                            <span className="input-group-text" id="basic-addon1">
                               <Person fontSize="small"/>
                            </span>
                        </div>
                        <input type="text" className="form-control" placeholder="Username" aria-label="Username"
                               aria-describedby="basic-addon1"
                               value={this.user.username}
                               onChange={this.usernameChange.bind(this)}/>
                    </div>

                    <br/>

                    <div className="input-group mb-3">
                        <div className="input-group-prepend">
                            <span className="input-group-text" id="basic-addon1">
                               <Lock fontSize="small"/>
                            </span>
                        </div>
                        <input type="password" className="form-control" placeholder="Password" aria-label="Password"
                               aria-describedby="basic-addon1"
                               value={this.user.password}
                               onChange={this.passwordChange.bind(this)}/>
                    </div>

                    <button className="btn signInBtn" onClick={this.register.bind(this)}>Register</button>
                </div>
            </div>
        )
    }
}

export default withRouter(Register);