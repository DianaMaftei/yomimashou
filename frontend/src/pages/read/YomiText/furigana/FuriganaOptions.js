import React from "react";
import FormControl from "@material-ui/core/FormControl/FormControl";
import RadioGroup from "@material-ui/core/RadioGroup/RadioGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel/FormControlLabel";
import Radio from "@material-ui/core/Radio/Radio";

export default ({analyzer, toggleFurigana, kanjiLevel, setLevel}) => {
    return (
        <div>
            <button className="btn btn-light" id="toggle-furigana"
                    disabled={!kanjiLevel || !analyzer}
                    onClick={toggleFurigana}>
                ルビ
            </button>
            <FormControl component="fieldset">
                <RadioGroup
                    aria-label="kanji level"
                    style={{flexDirection: 'row', paddingLeft: 10}}
                    name="JLPT level"
                    value={kanjiLevel}
                    onChange={setLevel}
                >
                    <FormControlLabel value="N5" control={<Radio/>} label="N5"/>
                    <FormControlLabel value="N4" control={<Radio/>} label="N4"/>
                    <FormControlLabel value="N3" control={<Radio/>} label="N3"/>
                    <FormControlLabel value="N2" control={<Radio/>} label="N2"/>
                    <FormControlLabel value="N1" control={<Radio/>} label="N1"/>
                    <FormControlLabel value="None" control={<Radio/>} label="None"/>
                </RadioGroup>
            </FormControl>
        </div>
    )
}