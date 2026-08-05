import axiosInstance from './axiosInstance';

export const patientService = {
  getAll: async () => (await axiosInstance.get('/patients')).data,
  getById: async (id) => (await axiosInstance.get(`/patients/${id}`)).data,
  search: async (query) => (await axiosInstance.get(`/patients/search?query=${query}`)).data,
  getByUserId: async (userId) => (await axiosInstance.get(`/patients/user/${userId}`)).data,
  create: async (data) => (await axiosInstance.post('/patients', data)).data,
  update: async (id, data) => (await axiosInstance.put(`/patients/${id}`, data)).data,
  delete: async (id) => (await axiosInstance.delete(`/patients/${id}`)).data,
};
