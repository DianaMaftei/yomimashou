import React, {useEffect} from 'react';
import "./home.scss";
import {apiUrl} from "../../AppUrl";
import axios from "axios";
import {useDispatch, useSelector} from "react-redux";
import MasonryLayout from "./MasonryLayout/MasonryLayout";
import 'react-image-crop/dist/ReactCrop.css';
import {withHeaders} from "../../auth/auth";
import Header from "../../components/header/Header";
import PlusIcon from 'mdi-react/PlusIcon';
import {Link} from "react-router-dom";
import spinner from "../read/YomiText/Rikai/spinner.svg";

const Home = () => {
    const dispatch = useDispatch();
    const texts = useSelector(state => state.home.texts);
    const textsStatuses = useSelector(state => state.home.textsStatuses);

    useEffect(() => {
        dispatch({
            type: 'GET_TEXTS',
            payload: axios.get(apiUrl + '/api/text')
        });
    }, [dispatch])

    useEffect(() => {
        dispatch({
            type: 'GET_TEXTS_STATUSES',
            payload: axios.get(apiUrl + '/api/users/textStatus', withHeaders())
        });
    }, [dispatch])

    return (
        <div className="home-page">
            <div id="app-header">
                <Header leftIcon="menu"/>
            </div>
            {texts.length === 0 ? (
                <div>
                    <img id="spinner" src={spinner} alt=""/>
                </div>
            ) : (
                <MasonryLayout texts={texts} textsStatuses={textsStatuses}/>
            )
            }
            <Link to={"/add"} id="add-btn">
                <PlusIcon size="42"/>
            </Link>
        </div>
    );
}

export default Home;
