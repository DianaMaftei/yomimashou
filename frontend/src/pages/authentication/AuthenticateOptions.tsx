import FacebookIcon from 'mdi-react/FacebookIcon';
import colors from '../../style/colorConstants';
import GoogleIcon from 'mdi-react/GoogleIcon';
import React from 'react';
import './authenticateOptions.scss';
import IconButton from '../../components/buttons/iconBtn/IconButton';


const AuthenticateOptions = ({isLogin}: AuthenticateOptionsProps) => (
    <div className="options-container">
        <p>Sign {isLogin ? "in" : "up"} with</p>
        <div id="options-btns">
            <IconButton onClick={() => {}} label="Facebook">
                <FacebookIcon size="24" color={colors.yomiDarkBlue}/>
            </IconButton>
            <IconButton onClick={() => {}} label="Google">
                <GoogleIcon size="24" color={colors.yomiDarkBlue}/>
            </IconButton>
        </div>
    </div>
)

type AuthenticateOptionsProps = {
    isLogin: boolean
}

export default AuthenticateOptions;
