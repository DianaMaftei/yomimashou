const apiUrl = process.env.NODE_ENV === 'production' ? process.env.REACT_APP_PROD_API_URL : process.env.REACT_APP_DEV_API_URL;
const studyApiUrl = process.env.NODE_ENV === 'production' ? process.env.REACT_APP_PROD_STUDY_API_URL : process.env.REACT_APP_DEV_STUDY_API_URL;
const ocrApiUrl = process.env.NODE_ENV === 'production' ? process.env.REACT_APP_PROD_OCR_API_URL : process.env.REACT_APP_DEV_OCR_API_URL;

export {
  apiUrl,
  studyApiUrl,
  ocrApiUrl
}
