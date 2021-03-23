import React from "react";
import {Button, Chip, TextField} from "@material-ui/core/umd/material-ui.development";

export default ({tagInput, updateTag, addTag, tags, deleteTag}) => (
    <div id="tags">
        <h6>Tags</h6>
        <TextField
            value={tagInput}
            onChange={updateTag}
            style={{width: '70%'}}
        />
        <Button variant="outlined" component="span" onClick={addTag} id="add-tags-btn">
            Add
        </Button>
        <div className="chips-list">
            {tags && tags.map((tag, index) =>
                <Chip
                    key={index}
                    label={tag}
                    variant="outlined"
                    color="primary"
                    onDelete={() => deleteTag(index)}
                    className="tag-chip"
                />
            )}
        </div>
    </div>
);
