import React from 'react';
import Card from "@material-ui/core/Card/Card";
import CardActionArea from "@material-ui/core/CardActionArea/CardActionArea";
import TextInfo from "../../../components/textInfo/TextInfo";
import TextCardBottom from "./TextCardBottom/TextCardBottom";
import CardContent from "@material-ui/core//CardContent/CardContent";
import Typography from "@material-ui/core/Typography/Typography";
import CardActions from "@material-ui/core/CardActions/CardActions";
import Divider from "@material-ui/core/Divider/Divider";
import "./textCard.scss";
import {Link} from "react-router-dom";
import * as PropTypes from "prop-types";

const TextCard = ({text, status}) => {
    if (!text) return <div/>;

    return (
        <Card style={{maxWidth: 350, borderRadius: 15, width:'90vw'}}>
            <Link to={"/read/" + text.id} className="text-card-link">
                <CardActionArea>
                    <TextInfo text={text}/>
                    <CardContent className="card-text-content">
                        {/*<KanjiPieChart kanjiCountByLevel={text.kanjiCountByLevel}*/}
                        {/*               className={text.imageFileName ? 'absolute-pos' : 'relative-pos'}/>*/}
                        <Typography variant="h5" component="h2" className="text-card-title"
                                    style={!text.imageFileName ? {display: 'block', float: 'right'} : {}}>
                            {text.title}
                        </Typography>
                        <Divider/>
                        <div className="text-info-creator">
                            <span>added by </span> <span className="text-info-creator-username">{text.createdBy}</span>
                        </div>
                        <Typography component="p" className="text-card-excerpt">
                            {text.excerpt.replace(/<br>/g, "\n")}
                        </Typography>
                        <div className="card-text-gradient"/>
                    </CardContent>
                </CardActionArea>
            </Link>
            <Divider/>
            <CardActions className="text-card-bottom-container">
                <TextCardBottom text={text} status={status}/>
            </CardActions>
        </Card>
    );
}

TextCard.propTypes = {
    text: PropTypes.object.isRequired,
    status: PropTypes.string
};


export default TextCard;
