import axiosInstance from './axiosInstance';

export const pilotService = {
  getAll: async () => (await axiosInstance.get('/pilots')).data,
  getById: async (id) => (await axiosInstance.get(`/pilots/${id}`)).data,
  getByStatus: async (status) => (await axiosInstance.get(`/pilots/status/${status}`)).data,
  getByUserId: async (userId) => (await axiosInstance.get(`/pilots/user/${userId}`)).data,
  create: async (data) => (await axiosInstance.post('/pilots', data)).data,
  update: async (id, data) => (await axiosInstance.put(`/pilots/${id}`, data)).data,
  delete: async (id) => (await axiosInstance.delete(`/pilots/${id}`)).data,
  updateStatus: async (id, status) => (await axiosInstance.patch(`/pilots/${id}/status?status=${status}`)).data,
};
