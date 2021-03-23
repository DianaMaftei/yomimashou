import React from "react";
import {Button} from "@material-ui/core/umd/material-ui.development";

export default ({disableAddBtn, submitText}) => (
    <div className="add-action-buttons">
        <Button variant="contained" color="primary" component="span" disabled={disableAddBtn} id="add-action-btn"
                onClick={submitText}> Add & Read </Button>
    </div>
);
