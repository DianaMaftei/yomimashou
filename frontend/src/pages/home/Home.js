import React from 'react';
import "./home.css";
import {apiUrl} from "../../AppUrl";
import axios from "axios";
import { connect } from "react-redux";
import MasonryLayout from "./MasonryLayout/MasonryLayout";
import 'react-image-crop/dist/ReactCrop.css';
import { isAuthenticated, withHeaders } from "../../auth/auth";
import Header from "../`common/header/Header";
import PlusIcon from 'mdi-react/PlusIcon';
import Link from "react-router-dom/Link";

const mapStateToProps = (state) => ({
    texts: state.home.texts,
    textsStatuses: state.home.textsStatuses
});

const mapDispatchToProps = (dispatch) => ({
    getTexts: () => {
        dispatch({
            type: 'GET_TEXTS',
            payload: axios.get(apiUrl + '/api/text')
        });
    },
    getTextsStatuses: () => {
        dispatch({
            type: 'GET_TEXTS_STATUSES',
            payload: axios.get(apiUrl + '/api/users/textStatus', withHeaders())
        });
    }
});

export class Home extends React.Component {
    constructor(props) {
        super(props);
        props.getTexts();
        if(isAuthenticated()) {
            props.getTextsStatuses();
        }
    }

    render() {
        if (this.props.texts.length === 0) {
            return <div/>;
        }

        return (
            <div className="home-page">
                <div id="app-header">
                    <Header leftIcon="menu"/>
                </div>
                <MasonryLayout texts={this.props.texts} textsStatuses={this.props.textsStatuses}/>
                <Link to={"/add"} id="add-btn">
                    <PlusIcon size={42}/>
                </Link>
            </div>
        );
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Home);
