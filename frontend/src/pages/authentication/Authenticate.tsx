import { FormControl, IconButton, InputAdornment, InputLabel, OutlinedInput } from '@material-ui/core';
import EmailIcon from 'mdi-react/EmailIcon';
import EyeIcon from 'mdi-react/EyeIcon';
import EyeOffIcon from 'mdi-react/EyeOffIcon';
import UserIcon from 'mdi-react/UserIcon';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, withRouter } from 'react-router-dom';
import ActionButton from '../../components/buttons/actionBtn/ActionButton';
import { User } from '../../model/User';
import { loginUser, registerUser } from '../../service/AuthService';
import { setItem } from '../../service/LocalStorageService';
import './authenticate.scss';
import { setEmailAction, setPasswordAction, setUsernameAction, showErrorAction, showPasswordAction } from './authenticateActions';
import AuthenticateOptions from './AuthenticateOptions';


const Authenticate = ({history}) => {
    const dispatch = useDispatch();
    const isLogin = history.location.pathname === '/login';
    const user: User = useSelector(state => state.authenticate.user);
    const showError = useSelector(state => state.authenticate.showError);
    const showPassword = useSelector(state => state.authenticate.showPassword);
    const toggleShowError = (show) => dispatch(showErrorAction(show));


    // TODO use action
    const doLogin = () => {
        loginUser(user).then(resp => {
            setItem('username', resp.headers['username']);
            setItem('token', resp.headers['authorization']);
            history.push('/');
        }).catch(err => {
            toggleShowError(true);
        });
    };

    // TODO use action
    const register = () => {
        toggleShowError(false);
        registerUser(user).then(() => {
            doLogin();
        });
    };

    const login = () => {
        toggleShowError(false);
        doLogin();
    };

    return (
        <div className="signIn-container">
            <AuthenticateOptions isLogin={isLogin}/>
            <div className="credentials-container">
                <p>Or sign {isLogin ? 'in' : 'up'} with credentials</p>

                {showError &&
                    <h6 className="error-msg">Incorrect email or password. Please try again.</h6>
                }
                <div>
                    {
                        !isLogin &&
                        <FormControl variant="filled">
                            <InputLabel htmlFor="username-input" required>Username</InputLabel>
                            <OutlinedInput
                                id="username-input"
                                type="text"
                                value={user.username}
                                onChange={e => dispatch(setUsernameAction(e.target.value))}
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
                            type="email"
                            value={user.email}
                            onChange={e => dispatch(setEmailAction(e.target.value))}
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
                            type={showPassword ? 'text' : 'password'}
                            value={user.password}
                            onChange={e => dispatch(setPasswordAction(e.target.value))}
                            endAdornment={
                                <InputAdornment position="end">
                                    <IconButton
                                        aria-label="toggle password visibility"
                                        onClick={() => dispatch(showPasswordAction())}
                                        onMouseDown={e => e.preventDefault()}
                                        edge="end"
                                    >
                                        {showPassword ? <EyeOffIcon size="24"/> : <EyeIcon size="24"/>}
                                    </IconButton>
                                </InputAdornment>
                            }
                        />
                    </FormControl>
                </div>
                {
                    isLogin &&
                    <div>
                        <Link to={'/register'} id="sign-up-prompt">
                            <div>Don't have an account yet? Sign up!</div>
                        </Link>
                        <ActionButton onClick={login} label="Sign In"/>
                    </div>
                }
                {
                    !isLogin &&
                    <div>
                        <Link to={'/login'} id="sign-up-prompt">
                            <div>Already have an account?</div>
                        </Link>
                        <ActionButton onClick={register} label="Sign Up"/>
                    </div>
                }
            </div>
        </div>
    );
};

export default withRouter(Authenticate);