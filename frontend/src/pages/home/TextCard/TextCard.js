import React from 'react';
import Card from "@material-ui/core/Card/Card";
import CardActionArea from "@material-ui/core/CardActionArea/CardActionArea";
import TextCardTop from "./TextCardTop";
import TextCardBottom from "./TextCardBottom";
import CardContent from "@material-ui/core//CardContent/CardContent";
import Typography from "@material-ui/core/Typography/Typography";
import CardActions from "@material-ui/core/CardActions/CardActions";
import DateRange from '@material-ui/icons/DateRange';
import Divider from "@material-ui/core/Divider/Divider";
import "./TextCard.css";
import Link from "react-router-dom/Link";
import KanjiPieChart from "./KanjiPieChart";
import axios from "axios/index";
import apiUrl from "../../../AppUrl";

export default ({text}) => {
    if (!text) return <div/>;

    return (
        <Card style={{maxWidth: 350}}>
            <Link to={"/read/" + text.id} className="text-card-link">
                <CardActionArea>
                    <TextCardTop text={text}/>
                    <CardContent>
                        <KanjiPieChart kanjiCountByLevel={text.kanjiCountByLevel} className={text.imageFileName ? 'absolute-pos' : 'relative-pos'}/>
                        <Typography variant="h5" component="h2" style={!text.imageFileName ? {display: 'block', float: 'right'} : {}}>
                            {text.title}
                        </Typography>
                        <Typography gutterBottom variant="caption">
                            <DateRange className="text-card-date"/>

                            {text.creationDate}
                            <span style={{float: 'right'}}>
                            by {text.createdBy}
                        </span>
                        </Typography>
                        {/*<Typography gutterBottom variant="caption">*/}
                        {/*Text Length: {text.content.length} characters*/}
                        {/*</Typography>*/}
                        <Divider/>
                        <Typography component="p" className="text-card-excerpt">
                            {text.excerpt.replace(/<br>/g, "\n")}
                        </Typography>
                    </CardContent>
                </CardActionArea>
            </Link>
            <Divider/>
            <CardActions className="text-card-bottom-container">
                <TextCardBottom text={text}/>
            </CardActions>
        </Card>
    );
}