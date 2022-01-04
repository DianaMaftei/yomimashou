import { Button } from '@material-ui/core';
import React from 'react';
import './actionBtn.scss';


const ActionButton = ({onClick, label, disabled=false}: ActionButtonProps) => (
    <div id="action-btn">
        <Button disabled={disabled} variant="contained" onClick={onClick}> {label} </Button>
    </div>
);

type ActionButtonProps = {
    onClick: React.MouseEventHandler<HTMLButtonElement>
    label: string
    disabled?: boolean
}

export default ActionButton;
