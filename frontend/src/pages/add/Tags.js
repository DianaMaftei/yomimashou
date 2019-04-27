import React from "react";
import {Button, Chip, TextField} from "@material-ui/core/umd/material-ui.development";

export default ({tagInput, updateTag, addTag, tags, deleteTag}) => (
    <div>
        <h6>Tags</h6>
        <TextField
            id="tags"
            value={tagInput}
            onChange={updateTag}
        />
        <Button variant="outlined" component="span" onClick={addTag}>
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