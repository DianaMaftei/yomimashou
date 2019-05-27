import React from 'react';
import "./SimpleModal.css";

export default class SimpleModal extends React.Component {
    state = {
        open: false
    };

    handleOpen = () => {
        this.setState({open: true});
    };

    handleClose = () => {
        this.setState({open: false});
    };

    render() {
        const {label} = this.props;

        return (
            <div id="simple-modal">
                <a href="#openModal" onClick={this.handleOpen}>{label}</a>

                <div id="openModal" className="modalDialog">
                    <div id="modal-container"><a href="#close" title="Close" className="close" onClick={this.handleClose}>X</a>
                        {React.cloneElement(this.props.children, {open: this.state.open})}
                    </div>
                </div>
            </div>
        );
    }
}