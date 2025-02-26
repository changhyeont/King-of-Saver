// src/api/axiosConfig.js
import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json'
    }
});

// 요청 인터셉터
axiosInstance.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            // 토큰이 'Bearer '로 시작하지 않으면 추가
            const finalToken = token.startsWith('Bearer ') ? token : `Bearer ${token}`;
            config.headers.Authorization = finalToken;
        }
        return config;
    },
    error => Promise.reject(error)
);

// 응답 인터셉터
axiosInstance.interceptors.response.use(
    response => response,
    error => {
        console.error('API 오류:', error);
        if (error.response?.status === 403) {
            // 토큰 만료 시 처리
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;