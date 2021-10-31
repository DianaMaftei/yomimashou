import React from 'react';
import "./summary.scss";
import CardContent from "@material-ui/core/CardContent/CardContent";
import CardActions from "@material-ui/core/CardActions/CardActions";
import Card from "@material-ui/core/Card/Card";
import DeckMasteryIndicator from "../../decks/deck/deck-mastery-indicator/DeckMasteryIndicator";
import {Button} from "@material-ui/core";
import DoneIcon from 'mdi-react/DoneIcon';
import CloseIcon from 'mdi-react/CloseIcon';
import StarIcon from 'mdi-react/StarIcon';

const Summary = ({summary, practiceMore}) => {
    if (!summary.show) {
        return <div/>
    }

    return (
        <div key={summary} id="summary">
            {/*deckINfo
            // how many total
            // how many are new/unseen
            // how many are active (reviewed)
            // how many are due

            */}

            <Card style={{maxWidth: 350, borderRadius: 15}}>
                <CardContent className="card-text-content">
                    {/*<DeckMasteryIndicator/>*/}
                    <div className="practice-results">
                        <span>
                            <span className="icon icon-know"><DoneIcon size={24}/></span>
                            <span>{summary.know}</span>
                        </span>
                        <span>
                            <span className="icon icon-know-well"><StarIcon size={24}/></span>
                            <span>{summary.knowWell}</span>
                        </span>
                        <span>
                            <span className="icon icon-notknow"><CloseIcon size={24}/></span>
                            <span>{summary.doNotKnow}</span>
                        </span>
                    </div>
                </CardContent>
                <CardActions className="text-card-bottom-container">
                    <Button variant="contained" color="primary" onClick={practiceMore}>Practice More</Button>
                </CardActions>
            </Card>
        </div>


    );
};

export default Summary;
