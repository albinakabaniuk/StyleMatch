import api from '../api';

export const getProfile = () => api.get("/api/users/me");
export const getResults = () => api.get("/api/test-results");
export const updateProfile = (data) => api.put("/api/users/me", data);
export const deleteResult = (id) => api.delete(`/api/test-results/${id}`);
export const deleteAllResults = (userId) => api.delete(`/api/test-results/user/${userId}`);

export const deleteAccount = () => api.delete("/api/users/me");
export const updateLanguage = (language) => api.put("/api/users/language", { language });

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
