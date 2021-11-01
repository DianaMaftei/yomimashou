import React from "react";
import axios from "axios/index";
import {apiUrl} from "../../AppUrl";
import "./authenticate.scss";
import EmailIcon from 'mdi-react/EmailIcon';
import EyeIcon from 'mdi-react/EyeIcon';
import EyeOffIcon from 'mdi-react/EyeOffIcon';
import {connect} from "react-redux";
import {Link, withRouter} from 'react-router-dom'
import {Button, FormControl, IconButton, InputAdornment, InputLabel, OutlinedInput} from "@material-ui/core";
import FacebookIcon from "mdi-react/FacebookIcon";
import GoogleIcon from "mdi-react/GoogleIcon";
import colors from "../../style/colorConstants";
import UserIcon from "mdi-react/UserIcon";

const mapStateToProps = (state) => ({
    user: state.authenticate.user,
    showError: state.authenticate.showError,
    showPassword: state.authenticate.showPassword
});

const mapDispatchToProps = (dispatch) => ({
    setUsername: (event) => {
        dispatch({
            type: 'SET_USERNAME',
            username: event.target.value
        });
    }, setEmail: (event) => {
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
    }, toggleShowPassword: () => {
        dispatch({
            type: 'SHOW_PASSWORD'
        });
    }
});

class Authenticate extends React.Component {

    constructor(props) {
        super(props);
        this.isLogin = this.props.history.location.pathname === '/login';
    }

    register() {
        this.props.toggleShowError(false);
        axios.post(apiUrl + '/api/users/register', this.props.user).then(() => {
            axios.post(apiUrl + '/login', this.user).then(resp => {
                localStorage.setItem('username', this.user.username);
                localStorage.setItem('token', resp.headers['authorization']);
                this.props.history.push('/');
            }).catch(err => {
                this.props.toggleShowError(true);
            });
        });
    }

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

    handleClickShowPassword() {
        this.props.toggleShowPassword();
    }

    handleMouseDownPassword(event) {
        event.preventDefault();
    }

    shouldComponentUpdate(nextProps, nextState) {
        return this.props.history.location.pathname !== nextProps.history.location.pathname;
    }

    render() {
        return (
            <div className="signIn-container">
                    <div className="options-container">
                        <p>Sign {this.isLogin ? 'in' : 'up'} with</p>
                        <div id="options-btns">
                            <Button variant="outlined" component="div">
                                <span><FacebookIcon size="24" color={colors.yomiDarkBlue}/></span>
                                <span>Facebook</span>
                            </Button>
                            <Button variant="outlined" component="div">
                                <span><GoogleIcon size="24" color={colors.yomiDarkBlue}/></span>
                                <span>Google</span>
                            </Button>
                        </div>
                    </div>
                    <div className="credentials-container">
                        <p>Or sign {this.isLogin ? 'in' : 'up'} with credentials</p>

                        {this.props.showError &&
                            <h6 className="error-msg">Incorrect email or password. Please try again.</h6>
                        }
                        <div>
                            {
                                !this.isLogin &&
                                <FormControl variant="filled">
                                    <InputLabel htmlFor="username-input" required>Username</InputLabel>
                                    <OutlinedInput
                                        id="username-input"
                                        type="text"
                                        value={this.props.user.username}
                                        onChange={this.props.setUsername}
                                        startAdornment={
                                            <InputAdornment position="start">
                                                <UserIcon fontSize="small"/>
                                            </InputAdornment>
                                        }
                                    />
                                </FormControl>
                            }

                            <FormControl variant="filled">
                                <InputLabel htmlFor="email-input" required>Email</InputLabel>
                                <OutlinedInput
                                    id="email-input"
                                    type="text"
                                    value={this.props.user.email}
                                    onChange={this.props.setEmail}
                                    startAdornment={
                                        <InputAdornment position="start">
                                            <EmailIcon size="24"/>
                                        </InputAdornment>
                                    }
                                />
                            </FormControl>

                            <FormControl variant="filled">
                                <InputLabel htmlFor="password-input" required>Password</InputLabel>
                                <OutlinedInput
                                    id="password-input"
                                    type={this.props.showPassword ? 'text' : 'password'}
                                    value={this.props.user.password}
                                    onChange={this.props.setPassword}
                                    endAdornment={
                                        <InputAdornment position="end">
                                            <IconButton
                                                aria-label="toggle password visibility"
                                                onClick={this.handleClickShowPassword.bind(this)}
                                                onMouseDown={this.handleMouseDownPassword}
                                                edge="end"
                                            >
                                                {this.props.showPassword ? <EyeOffIcon size="24"/> : <EyeIcon size="24"/>}
                                            </IconButton>
                                        </InputAdornment>
                                    }
                                />
                            </FormControl>
                        </div>
                        {
                            this.isLogin &&
                                <div>
                                    <Link to={"/register"} id="sign-up-prompt">
                                        <div>Don't have an account yet? Sign up!</div>
                                    </Link>
                                    <Button variant="contained" color="secondary" onClick={this.login.bind(this)} component="div">Sign In</Button>
                                </div>
                        }
                        {
                            !this.isLogin &&
                                <div>
                                    <Link to={"/login"} id="sign-up-prompt">
                                        <div>Already have an account?</div>
                                    </Link>
                                    <Button variant="contained" color="secondary" onClick={this.register.bind(this)} component="div">Sign Up</Button>
                                </div>
                        }
                    </div>
                </div>
        )
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Authenticate));
