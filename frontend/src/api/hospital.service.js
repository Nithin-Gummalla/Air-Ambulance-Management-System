import axiosInstance from './axiosInstance';

export const hospitalService = {
  getAll: async () => (await axiosInstance.get('/hospitals')).data,
  getById: async (id) => (await axiosInstance.get(`/hospitals/${id}`)).data,
  search: async (query) => (await axiosInstance.get(`/hospitals/search?query=${query}`)).data,
  create: async (data) => (await axiosInstance.post('/hospitals', data)).data,
  update: async (id, data) => (await axiosInstance.put(`/hospitals/${id}`, data)).data,
  delete: async (id) => (await axiosInstance.delete(`/hospitals/${id}`)).data,
  getByUserId: async (userId) => (await axiosInstance.get(`/hospitals/user/${userId}`)).data,
};
