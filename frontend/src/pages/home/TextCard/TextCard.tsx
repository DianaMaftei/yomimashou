import CardContent from '@material-ui/core//CardContent/CardContent';
import Card from '@material-ui/core/Card/Card';
import CardActionArea from '@material-ui/core/CardActionArea/CardActionArea';
import CardActions from '@material-ui/core/CardActions/CardActions';
import Divider from '@material-ui/core/Divider/Divider';
import Typography from '@material-ui/core/Typography/Typography';
import { Link } from 'react-router-dom';
import TextInfo from '../../../components/textInfo/TextInfo';
import { Text } from '../../../model/Text';
import './textCard.scss';
import TextCardBottom from './TextCardBottom/TextCardBottom';


const TextCard = ({text, status}: TextCardProps) => {
    if (!text) {
        return <div/>;
    }

    return (
        <Card style={{maxWidth: 350, borderRadius: 15, width: '90vw'}}>
            <Link to={'/read/' + text.id} className="text-card-link">
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
                            {text.excerpt.replace(/<br>/g, '\n')}
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
};

type TextCardProps = {
    text: Text
    status?: string
}


export default TextCard;
