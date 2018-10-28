import React from 'react';
import SignalWifi1Bar from '@material-ui/icons/SignalWifi1Bar';
import Tooltip from "@material-ui/core/es/Tooltip/Tooltip";

export default () => (
    <Tooltip title="Easy" placement="top">
        <SignalWifi1Bar fontSize="large" className="difficulty-level level-one"/>
    </Tooltip>
);