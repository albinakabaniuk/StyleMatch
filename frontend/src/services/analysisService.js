import api from '../api';

export const analyzeTestAnswers = async (answers, language) => {
    const response = await api.post('analysis/test', { answers, language });
    return response.data;
};

export const analyzePhoto = async (file, language) => {
    const formData = new FormData();
    formData.append('file', file);
    const response = await api.post('analysis/photo', formData, {
        params: { language },
    });
    return response.data;
};

export const analyzeBodyShape = async (answers, language) => {
    const response = await api.post('body-shape/analyze', { answers, language });
    return response.data;
};
