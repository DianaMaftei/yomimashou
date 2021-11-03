import {Tab, Tabs} from "@material-ui/core";
import * as PropTypes from "prop-types";
import React from "react";

const TextSourceTabs = ({value, onChange}) => {
    return (
        <Tabs value={value} onChange={onChange} variant="scrollable" scrollButtons="off" className="text-source-tabs">
            <Tab label="EDITOR" id="tab-0" aria-controls="tab-content-0" selected="true"/>
            <Tab label="OCR" id="tab-1" aria-controls="tab-content-1"/>
            <Tab label="MANGA" id="tab-2" aria-controls="tab-content-2"/>
            <Tab label="SUBS" id="tab-3" aria-controls="tab-content-3"/>
        </Tabs>
    );
}

TextSourceTabs.propTypes = {
    value: PropTypes.number.isRequired,
    onChange: PropTypes.func.isRequired
};

export default TextSourceTabs;
