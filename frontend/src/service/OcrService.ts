import { http } from './http';
import { ocrApiUrl } from '../AppUrl';
import { AxiosResponse } from 'axios';


export const scanImages = (formData: FormData): Promise<AxiosResponse<any>> => {
    const headers = {
        "Content-Type": "multipart/form-data",
    }
    return http.post(ocrApiUrl + '/ocr/full', formData, {headers});
};
