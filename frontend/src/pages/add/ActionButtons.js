import React from "react";
import {Button, Switch} from "@material-ui/core/umd/material-ui.development";

export default ({disableAddBtn, submitText}) => (
    <div className="add-action-buttons">
        <Button variant="outlined" component="span" disabled> Preview </Button>
        <div>
            <Switch checked={true} onChange={(state) => console.log(state)} color="primary" disabled/>
            <span>public</span>
        </div>
        <Button variant="contained" color="primary" component="span" disabled={disableAddBtn}
                onClick={submitText}> Add & Read </Button>
    </div>
);