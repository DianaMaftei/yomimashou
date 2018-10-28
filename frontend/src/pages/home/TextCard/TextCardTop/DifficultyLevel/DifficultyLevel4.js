import React from 'react';
import SignalWifi4Bar from '@material-ui/icons/SignalWifi4Bar';
import Tooltip from "@material-ui/core/es/Tooltip/Tooltip";

export default () => (
    <Tooltip title="Advanced" placement="top">
        <SignalWifi4Bar fontSize="large" className="difficulty-level level-four"/>
    </Tooltip>
);