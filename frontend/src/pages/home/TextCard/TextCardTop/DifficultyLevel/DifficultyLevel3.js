import React from 'react';
import SignalWifi3Bar from '@material-ui/icons/SignalWifi3Bar';
import Tooltip from "@material-ui/core/es/Tooltip/Tooltip";

export default () => (
    <Tooltip title="Upper Intermediate" placement="top">
        <SignalWifi3Bar fontSize="large" className="difficulty-level level-three"/>
    </Tooltip>
);