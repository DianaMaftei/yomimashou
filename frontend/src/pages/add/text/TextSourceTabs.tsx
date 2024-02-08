import { Tab, Tabs } from '@material-ui/core';
import React from 'react';


const TextSourceTabs = ({value, onChange}: TextSourceTabsProps) => {
    return (
        <Tabs value={value} onChange={onChange} variant="scrollable" scrollButtons="off" className="text-source-tabs">
            <Tab label="EDITOR" id="tab-0" aria-controls="tab-content-0" selected={true}/>
            <Tab label="OCR" id="tab-1" aria-controls="tab-content-1"/>
            <Tab label="MANGA" id="tab-2" aria-controls="tab-content-2"/>
            <Tab label="SUBS" id="tab-3" aria-controls="tab-content-3"/>
        </Tabs>
    );
};

type TextSourceTabsProps = {
    value: number
    onChange: any
}
export default TextSourceTabs;
