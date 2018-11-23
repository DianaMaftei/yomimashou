import React from 'react';
import Card from "@material-ui/core/es/Card/Card";
import CardActionArea from "@material-ui/core/es/CardActionArea/CardActionArea";
import TextCardTop from "./TextCardTop";
import TextCardBottom from "./TextCardBottom";
import CardContent from "@material-ui/core/es/CardContent/CardContent";
import Typography from "@material-ui/core/es/Typography/Typography";
import CardActions from "@material-ui/core/es/CardActions/CardActions";
import DateRange from '@material-ui/icons/DateRange';
import Divider from "@material-ui/core/es/Divider/Divider";
import "./TextCard.css";
import Link from "react-router-dom/es/Link";

export default ({ text, onCardClick }) => {
    if (!text) return <div/>

    return (
        <Card style={{ maxWidth: 350 }} onClick={() => onCardClick(text.id)}>
            <Link to="/read" className="text-card-link">
                <CardActionArea>
                    <TextCardTop text={text}/>
                    <CardContent>
                        <Typography variant="h5" component="h2">
                            {text.title}
                        </Typography>
                        <Typography gutterBottom variant="caption">
                            <DateRange className="text-card-date"/>

                            {text.creationDate}
                            <span style={{ float: 'right' }}>
                            by {text.createdBy}
                        </span>
                        </Typography>
                        {/*<Typography gutterBottom variant="caption">*/}
                        {/*Text Length: {text.content.length} characters*/}
                        {/*</Typography>*/}
                        <Divider/>
                        <Typography component="p" className="text-card-excerpt">
                            {text.excerpt.replace(/<br>/g, "")}
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