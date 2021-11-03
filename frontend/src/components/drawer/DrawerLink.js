import {Link} from "react-router-dom";
import React from "react";
import * as PropTypes from "prop-types";

const DrawerLink = ({link, color, children, text, onClick}) => (
    <Link to={link} className="drawer-link" onClick={onClick}>
        <div style={{color: color}}>
            <span>{children}</span>
            <span>{text}</span>
        </div>
    </Link>
);

DrawerLink.propTypes = {
    link: PropTypes.string.isRequired,
    color: PropTypes.string.isRequired,
    children: PropTypes.element.isRequired,
    text: PropTypes.string.isRequired,
    onClick: PropTypes.func.isRequired
};

export default DrawerLink;
