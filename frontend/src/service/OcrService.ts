import { AxiosResponse } from 'axios';
import { ocrApiUrl } from '../AppUrl';
import { http } from './http';


export const scanImages = (formData: FormData): Promise<AxiosResponse<any>> => {
    const headers = {
        "Content-Type": "multipart/form-data",
    }
    return http.post(ocrApiUrl + '/ocr/full', formData, {headers});
};
