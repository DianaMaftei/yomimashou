import React from 'react';
import { Link } from 'react-router-dom';


const DrawerLink = ({link, color, children, text, onClick}: DrawerLinkProps) => (
    <Link to={link} className="drawer-link" onClick={onClick}>
        <div style={{color: color}}>
            <span>{children}</span>
            <span>{text}</span>
        </div>
    </Link>
);

type DrawerLinkProps = {
    link: string
    color: string
    children: React.ReactNode
    text: string
    onClick: React.MouseEventHandler<HTMLAnchorElement>
}

export default DrawerLink;
