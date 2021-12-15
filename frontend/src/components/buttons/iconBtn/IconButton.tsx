import React from 'react';
import './iconBtn.scss';
import { Button } from '@material-ui/core';


const IconButton = ({onClick, children, label}: IconButtonProps) => (
    <div className="icon-btn">
        <Button variant="outlined" component="div" onClick={onClick}>
            {children}
            <span>{label}</span>
        </Button>
    </div>
);

type IconButtonProps = {
    onClick?: React.MouseEventHandler<HTMLDivElement>
    children: React.ReactNode
    label: string
}

export default IconButton;
