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

export default ({ text }) => {
    return (
        <Card style={{ maxWidth: 300 }}>
            <CardActionArea>
                <TextCardTop text={text}/>
                <CardContent>
                    <Typography variant="h5" component="h2">
                        {text.title}
                    </Typography>
                    <Typography gutterBottom variant="caption">
                        <DateRange className="text-card-date"/>
                        <span>
                           {new Intl.DateTimeFormat('en-GB', {
                               year: 'numeric',
                               month: 'long',
                               day: '2-digit'
                           }).format(text.date)}
                        </span>
                        <span style={{ float: 'right' }}>
                            by {text.user}
                        </span>
                    </Typography>
                    <Divider/>
                    <Typography component="p" className="text-card-excerpt">
                        {text.excerpt}
                    </Typography>
                </CardContent>
            </CardActionArea>
            <Divider/>
            <CardActions className="text-card-bottom-container">
                <TextCardBottom text={text}/>
            </CardActions>
        </Card>
    );
}