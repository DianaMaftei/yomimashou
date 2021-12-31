import {useEffect} from 'react';
import "./home.scss";
import {useDispatch, useSelector} from "react-redux";
import MasonryLayout from "./MasonryLayout/MasonryLayout";
import 'react-image-crop/dist/ReactCrop.css';
import Header from "../../components/header/Header";
import PlusIcon from 'mdi-react/PlusIcon';
import {Link} from "react-router-dom";
import spinner from "../read/YomiText/Rikai/spinner.svg";
import {getTextsAction, getTextsStatusesAction} from "./homeActions";

const Home = () => {
    const dispatch = useDispatch();
    const texts = useSelector(state => state.home.texts);
    const textsStatuses = useSelector(state => state.home.textsStatuses);

    useEffect(() => dispatch(getTextsAction()), [dispatch])
    useEffect(() => dispatch(getTextsStatusesAction()), [dispatch])

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
