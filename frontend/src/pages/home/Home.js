import React from 'react';
import "./home.css";
import apiUrl from "../../AppUrl";
import axios from "axios";
import { connect } from "react-redux";
import MasonryLayout from "./MasonryLayout/MasonryLayout";
import 'react-image-crop/dist/ReactCrop.css';

const mapStateToProps = (state) => ({
    texts: state.home.texts
});

const mapDispatchToProps = (dispatch) => ({
    getTexts: () => {
        dispatch({
            type: 'GET_TEXTS',
            payload: axios.get(apiUrl + '/api/text')
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
                <MasonryLayout texts={this.props.texts}/>
            </div>
        );
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Home);
