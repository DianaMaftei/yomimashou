import axios from "axios";
import {apiUrl} from "../../AppUrl";
import {withHeaders} from "../../auth/auth";

export const getTextsAction = () => {
    return {
        type: 'GET_TEXTS',
        payload: axios.get(apiUrl + '/api/text')
    }
}

export const getTextsStatusesAction = () => {
    return {
        type: 'GET_TEXTS_STATUSES',
        payload: axios.get(apiUrl + '/api/users/textStatus', withHeaders())
    }
}