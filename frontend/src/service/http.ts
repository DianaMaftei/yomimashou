import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';


enum StatusCode {
    UNAUTHORIZED = 401,
    FORBIDDEN = 403,
    NOT_FOUND = 404,
    TOO_MANY_REQUESTS = 429,
    INTERNAL_SERVER_ERROR = 500,
}

const injectToken = (config: AxiosRequestConfig): AxiosRequestConfig => {
    try {
        const token = localStorage.getItem('token');

        if(token != null && config.headers) {
            config.headers.Authorization = `${token}`;
        }
        return config;
    } catch(error) {
        //TODO handle error
        if(error instanceof Error) {
            console.log(error.message);
        }
        return config;
    }
};

class Http {
    private instance: AxiosInstance | null = null;

    private get http(): AxiosInstance {
        return this.instance != null ? this.instance : this.initHttp();
    }

    initHttp() {
        axios.defaults.headers.common['Content-Type'] = 'application/json;charset=utf-8';
        const http = axios;

        http.interceptors.request.use(injectToken, (error) => Promise.reject(error));

        http.interceptors.response.use(
            (response) => response,
            (error) => {
                const {response} = error;
                return this.handleError(response);
            }
        );

        this.instance = http;
        return http;
    }

    get<T = any, R = AxiosResponse<T>>(url: string, config?: AxiosRequestConfig): Promise<R> {
        return this.http.get<T, R>(url, config);
    }

    post<T = any, R = AxiosResponse<T>>(
        url: string,
        data?: T,
        config?: AxiosRequestConfig
    ): Promise<R> {
        return this.http.post<T, R>(url, data, config);
    }

    put<T = any, R = AxiosResponse<T>>(
        url: string,
        data?: T,
        config?: AxiosRequestConfig
    ): Promise<R> {
        return this.http.put<T, R>(url, data, config);
    }

    delete<T = any, R = AxiosResponse<T>>(url: string, config?: AxiosRequestConfig): Promise<R> {
        return this.http.delete<T, R>(url, config);
    }

    // Handle global app errors
    // We can handle generic app errors depending on the status code
    private handleError(error: any) {
        if (!error || !error.status) return;

        const {status} = error;


        switch(status) {
            case StatusCode.INTERNAL_SERVER_ERROR: {
                // Handle InternalServerError
                break;
            }
            case StatusCode.FORBIDDEN: {
                // Handle Forbidden
                break;
            }
            case StatusCode.UNAUTHORIZED: {
                // Handle Unauthorized
                break;
            }
            case StatusCode.NOT_FOUND: {
                // Handle not found
                break;
            }
            case StatusCode.TOO_MANY_REQUESTS: {
                // Handle TooManyRequests
                break;
            }
        }

        return Promise.reject(error);
    }
}

export const http = new Http();
