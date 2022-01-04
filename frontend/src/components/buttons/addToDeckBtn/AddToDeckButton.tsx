import AddIcon from 'mdi-react/AddIcon';
import StyleIcon from 'mdi-react/StyleIcon';
import React from 'react';
import './addToDeckBtn.scss';


const AddToDeckButton = ({onClick}: AddToDeckButtonProps) => (
    <span className="add-to-deck-btn" onClick={onClick}>
            <StyleIcon size="30"/>
            <AddIcon size="30"/>
    </span>
);

type AddToDeckButtonProps = {
    onClick: React.MouseEventHandler<HTMLDivElement>
}

export default AddToDeckButton;
