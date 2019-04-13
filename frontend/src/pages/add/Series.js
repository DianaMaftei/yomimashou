import React from "react";
import {Checkbox, FormControl, FormControlLabel, MenuItem, Select} from "@material-ui/core/umd/material-ui.development";

export default () => (
    <div className="add-text-series">
        <FormControlLabel value="series" control={<Checkbox/>} label="Series" disabled/>
        <div>
            <FormControl disabled>
                <Select
                    value="ceva"
                    onChange={() => {
                    }}
                    style={{width: 200}}
                >
                    <MenuItem value="Ten">Ten</MenuItem>
                    <MenuItem value="Twenty">Twenty</MenuItem>
                    <MenuItem value="Thirty">Thirty</MenuItem>
                </Select>
            </FormControl>
        </div>
    </div>
);