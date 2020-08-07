import React from "react";
import FormControl from "@material-ui/core/FormControl/FormControl";
import FormControlLabel from "@material-ui/core/FormControlLabel/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import "./furiganaOptions.css";
import furiganaIcon from "./furigana_icon.svg";

export default ({handleFurigana}) => {
    return (
        <div>
            <FormControl component="fieldset">
                <div className="rubi-jlpt-levels">
                    <img id="furigana-icon" src={furiganaIcon} alt="furigana"/>
                    <FormControlLabel value="N5" control={<Checkbox onChange={handleFurigana}/>} label="N5"
                                      labelPlacement="start"/>
                    <FormControlLabel value="N4" control={<Checkbox onChange={handleFurigana}/>} label="N4"
                                      labelPlacement="start"/>
                    <FormControlLabel value="N3" control={<Checkbox onChange={handleFurigana}/>} label="N3"
                                      labelPlacement="start"/>
                    <FormControlLabel value="N2" control={<Checkbox onChange={handleFurigana}/>} label="N2"
                                      labelPlacement="start"/>
                    <FormControlLabel value="N1" control={<Checkbox onChange={handleFurigana}/>} label="N1"
                                      labelPlacement="start"/>
                </div>
            </FormControl>
        </div>
    )
}