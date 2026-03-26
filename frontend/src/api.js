import axios from 'axios';

const api = axios.create({
    baseURL: '/api',
    timeout: 60000, // 60s
});

// ── Request interceptor: attach JWT ──────────────────────────────────────────
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// ── Response interceptor: surface timeout errors clearly ─────────────────────
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.code === 'ECONNABORTED') {
            // Axios timeout — tell user clearly rather than showing generic error
            return Promise.reject(new Error('Request timed out. Please try again.'));
        }
        return Promise.reject(error);
    }
);

export default api;
