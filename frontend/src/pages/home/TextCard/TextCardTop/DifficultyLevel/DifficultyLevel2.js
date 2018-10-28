import React from 'react';
import SignalWifi2Bar from '@material-ui/icons/SignalWifi2Bar';
import Tooltip from "@material-ui/core/es/Tooltip/Tooltip";

export default () => (
    <Tooltip title="Lower Intermediate" placement="top">
        <SignalWifi2Bar fontSize="large" className="difficulty-level level-two"/>
    </Tooltip>
);