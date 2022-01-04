import Card from '@material-ui/core/Card/Card';
import CardActions from '@material-ui/core/CardActions/CardActions';
import CardContent from '@material-ui/core/CardContent/CardContent';
import CloseIcon from 'mdi-react/CloseIcon';
import DoneIcon from 'mdi-react/DoneIcon';
import StarIcon from 'mdi-react/StarIcon';
import React from 'react';
import ActionButton from '../../../components/buttons/actionBtn/ActionButton';
import './summary.scss';


const Summary = ({summary, practiceMore}: SummaryProps) => {
    if (!summary.show) {
        return <div/>
    }

    return (
        <div key={summary} id="summary">
            <Card style={{maxWidth: 350, borderRadius: 15}}>
                <CardContent className="card-text-content">
                    <div className="practice-results">
                        <span>
                            <span className="icon icon-notknow"><CloseIcon size="24"/></span>
                            <span>{summary.doNotKnow}</span>
                        </span>
                        <span>
                            <span className="icon icon-know"><DoneIcon size="24"/></span>
                            <span>{summary.know}</span>
                        </span>
                        <span>
                            <span className="icon icon-know-well"><StarIcon size="24"/></span>
                            <span>{summary.knowWell}</span>
                        </span>

                    </div>
                </CardContent>
                <CardActions className="text-card-bottom-container">
                    <ActionButton onClick={practiceMore} label="Practice More"/>
                </CardActions>
            </Card>
        </div>
    );
};

type SummaryProps = {
    summary: object
    practiceMore: any
}

export default Summary;
