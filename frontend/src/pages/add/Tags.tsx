import { Button, Chip, TextField } from '@material-ui/core';
import React from 'react';


const Tags = ({tagInput, updateTag, addTag, tags, deleteTag}: TagsProps) => (
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


type TagsProps = {
    tagInput?: any
    updateTag?: any
    addTag?: any
    tags?: any
    deleteTag?: any
}

export default Tags;
