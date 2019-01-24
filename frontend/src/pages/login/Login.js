import React from "react";
import axios from "axios/index";
import apiUrl from "../../AppUrl";
import "../../style/signIn.css";
import Email from '@material-ui/icons/Email';
import Lock from '@material-ui/icons/Lock';
import { connect } from "react-redux";
import { withRouter } from 'react-router-dom'

const mapStateToProps = (state) => ({
    user: state.login.user,
    showError: state.login.showError
});

const mapDispatchToProps = (dispatch) => ({
    setEmail: (event) => {
        dispatch({
            type: 'SET_EMAIL',
            email: event.target.value
        });
    }, setPassword: (event) => {
        dispatch({
            type: 'SET_PASSWORD',
            password: event.target.value
        });
    }, toggleShowError: (show) => {
        dispatch({
            type: 'SHOW_ERROR',
            show: show
        });
    }
});

class Login extends React.Component {

    login() {
        this.props.toggleShowError(false);
        axios.post(apiUrl + '/login', this.props.user).then(resp => {
            localStorage.setItem('username', resp.headers['username']);
            localStorage.setItem('token', resp.headers['authorization']);
            this.props.history.push('/');
        }).catch(err => {
            this.props.toggleShowError(true);
        });
    }

    render() {
        return (
            <div>
                <div className="signIn-container">
                    <h1 id="signIn-heading">Login</h1>
                    {this.props.showError && <h6 className="error-msg">Incorrect email or password. Please try again.</h6>}
                    <label htmlFor="Email">Email</label>
                    <div className="input-group mb-3">
                        <div className="input-group-prepend">
                            <span className="input-group-text" id="basic-addon1">
                               <Email fontSize="small"/>
                            </span>
                        </div>
                        <input type="email" className="form-control" placeholder="Email" aria-label="Email"
                               aria-describedby="basic-addon1"
                               value={this.props.user.email}
                               onChange={this.props.setEmail}/>
                    </div>

                    <br/>

                    <label htmlFor="Password">Password</label>
                    <div className="input-group mb-3">
                        <div className="input-group-prepend">
                            <span className="input-group-text" id="basic-addon1">
                               <Lock fontSize="small"/>
                            </span>
                        </div>
                        <input type="password" className="form-control" placeholder="Password" aria-label="Password"
                               aria-describedby="basic-addon1"
                               value={this.props.user.password}
                               onChange={this.props.setPassword}/>
                    </div>

                    <button className="btn signInBtn" onClick={this.login.bind(this)}>Login</button>
                </div>
            </div>
        )
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Login));
