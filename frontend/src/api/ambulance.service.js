import axiosInstance from './axiosInstance';

export const ambulanceService = {
  getAll: async () => (await axiosInstance.get('/ambulances')).data,
  getAvailable: async () => (await axiosInstance.get('/ambulances/available')).data,
  getById: async (id) => (await axiosInstance.get(`/ambulances/${id}`)).data,
  search: async (query) => (await axiosInstance.get(`/ambulances/search?query=${query}`)).data,
  create: async (data) => (await axiosInstance.post('/ambulances', data)).data,
  update: async (id, data) => (await axiosInstance.put(`/ambulances/${id}`, data)).data,
  delete: async (id) => (await axiosInstance.delete(`/ambulances/${id}`)).data,
  assignPilot: async (id, pilotId) => (await axiosInstance.put(`/ambulances/${id}/assign-pilot/${pilotId}`)).data,
  updateStatus: async (id, status, availability) => 
    (await axiosInstance.patch(`/ambulances/${id}/status?status=${status || ''}&availability=${availability !== undefined ? availability : ''}`)).data,
};
