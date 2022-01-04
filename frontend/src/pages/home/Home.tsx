import PlusIcon from 'mdi-react/PlusIcon';
import { useEffect } from 'react';
import 'react-image-crop/dist/ReactCrop.css';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import Header from '../../components/header/Header';
import spinner from '../read/YomiText/Rikai/spinner.svg';
import './home.scss';
import { getTextsAction, getTextsStatusesAction } from './homeActions';
import MasonryLayout from './MasonryLayout/MasonryLayout';


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
