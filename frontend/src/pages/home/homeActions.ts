import { getAllTexts, getTextsStatuses } from '../../service/TextService';


export const getTextsAction = () => {
    return {
        type: 'GET_TEXTS',
        payload: getAllTexts()
    };
};

export const getTextsStatusesAction = () => {
    return {
        type: 'GET_TEXTS_STATUSES',
        payload: getTextsStatuses()
    };
};
