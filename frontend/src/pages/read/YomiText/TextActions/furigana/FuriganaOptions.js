import React from "react";
import FormControl from "@material-ui/core/FormControl/FormControl";
import FormControlLabel from "@material-ui/core/FormControlLabel/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import "./furiganaOptions.scss";
import furiganaIcon from "./furigana_icon.svg";

export default ({handleFurigana, kanjiLevels}) => {
    return (
        <div>
            <FormControl component="fieldset">
                <div className="rubi-jlpt-levels">
                    <img id="furigana-icon" src={furiganaIcon} alt="furigana"/>
                    <FormControlLabel value="N5" control={<Checkbox onChange={handleFurigana} checked={kanjiLevels.N5}/>} label="N5"
                                      labelPlacement="top"/>
                    <FormControlLabel value="N4" control={<Checkbox onChange={handleFurigana} checked={kanjiLevels.N4}/>} label="N4"
                                      labelPlacement="top"/>
                    <FormControlLabel value="N3" control={<Checkbox onChange={handleFurigana} checked={kanjiLevels.N3}/>} label="N3"
                                      labelPlacement="top"/>
                    <FormControlLabel value="N2" control={<Checkbox onChange={handleFurigana} checked={kanjiLevels.N2}/>} label="N2"
                                      labelPlacement="top"/>
                    <FormControlLabel value="N1" control={<Checkbox onChange={handleFurigana} checked={kanjiLevels.N1}/>} label="N1"
                                      labelPlacement="top"/>
                </div>
            </FormControl>
        </div>
    )
}
