import React from 'react';
import "./home.css";
import apiUrl from "../../AppUrl";
import axios from "axios";
import { connect } from "react-redux";
import MasonryLayout from "./MasonryLayout/MasonryLayout";

const mapStateToProps = (state) => ({
    texts: state.home.texts
});

const mapDispatchToProps = (dispatch) => ({
    getTexts: result => {
        dispatch({
            type: 'GET_TEXTS',
            payload: axios.get(apiUrl + '/api/text')
        });
    }, getTextById: id => {
        dispatch({
            type: 'GET_TEXT_BY_ID',
            payload: axios.get(apiUrl + '/api/text/' + id)
        });
    }
});

export class Home extends React.Component {
    constructor(props) {
        super(props);
        props.getTexts();
    }

    render() {
        if (this.props.texts.length === 0) {
            return <div/>;
        }

        return (
            <div className="home-container">
                <MasonryLayout texts={this.props.texts} onCardClick={this.props.getTextById}/>
            </div>
        );
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Home);
