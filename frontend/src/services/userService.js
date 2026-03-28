import api from '../api';

export const getProfile = () => api.get("users/me");
export const getResults = () => api.get("test-results");
export const updateProfile = (data) => api.put("users/me", data);
export const deleteResult = (id) => api.delete(`test-results/${id}`);
export const deleteAllResults = (userId) => api.delete(`test-results/user/${userId}`);

export const deleteAccount = () => api.delete("users/me");
export const updateLanguage = (language) => api.put("users/language", { language });

const userService = {
    getProfile,
    getResults,
    updateProfile,
    deleteResult,
    deleteAllResults,
    deleteAccount,
    updateLanguage
};

export default userService;
