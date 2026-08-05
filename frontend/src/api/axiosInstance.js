import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor: Attach JWT Bearer token
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response Interceptor: Handle 401 Unauthorized token expiry
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response && error.response.status === 401) {
      // Clear token on authentication failure
      const originalRequest = error.config;
      if (!originalRequest._retry && localStorage.getItem('refreshToken')) {
        originalRequest._retry = true;
        try {
          const res = await axios.post(`${API_BASE_URL}/auth/refresh`, {
            refreshToken: localStorage.getItem('refreshToken'),
          });
          if (res.data && res.data.token) {
            localStorage.setItem('token', res.data.token);
            axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${res.data.token}`;
            originalRequest.headers['Authorization'] = `Bearer ${res.data.token}`;
            return axiosInstance(originalRequest);
          }
        } catch (refreshError) {
          localStorage.removeItem('token');
          localStorage.removeItem('refreshToken');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
      }
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
