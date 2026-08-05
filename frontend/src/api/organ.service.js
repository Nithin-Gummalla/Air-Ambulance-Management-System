import axiosInstance from './axiosInstance';

export const organService = {
  getAll: async () => (await axiosInstance.get('/organ-transports')).data,
  getById: async (id) => (await axiosInstance.get(`/organ-transports/${id}`)).data,
  getByHospital: async (hospitalId) => (await axiosInstance.get(`/organ-transports/hospital/${hospitalId}`)).data,
  create: async (data) => (await axiosInstance.post('/organ-transports', data)).data,
  update: async (id, data) => (await axiosInstance.put(`/organ-transports/${id}`, data)).data,
  delete: async (id) => (await axiosInstance.delete(`/organ-transports/${id}`)).data,
};
