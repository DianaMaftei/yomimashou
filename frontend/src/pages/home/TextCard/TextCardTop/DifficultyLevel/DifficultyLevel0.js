import React from 'react';
import SignalWifi0Bar from '@material-ui/icons/SignalWifi0Bar';
import Tooltip from "@material-ui/core/es/Tooltip/Tooltip";

export default () => (
    <Tooltip title="Very easy" placement="top">
        <SignalWifi0Bar fontSize="large" className="difficulty-level level-zero"/>
    </Tooltip>
);