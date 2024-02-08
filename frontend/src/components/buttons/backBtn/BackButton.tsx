import ArrowLeftIcon from 'mdi-react/ArrowLeftIcon';
import React from 'react';
import './backBtn.scss';


const BackButton = ({onClick}: BackButtonProps) => (
    <span className="back-btn" onClick={onClick}>
        <ArrowLeftIcon size="24"/>
    </span>
);

type BackButtonProps = {
    onClick: React.MouseEventHandler<HTMLButtonElement>
}

export default BackButton;
