import axiosInstance from './axiosInstance';

export const bookingService = {
  getAll: async () => (await axiosInstance.get('/bookings')).data,
  getById: async (id) => (await axiosInstance.get(`/bookings/${id}`)).data,
  getByPatient: async (patientId) => (await axiosInstance.get(`/bookings/patient/${patientId}`)).data,
  getByHospital: async (hospitalId) => (await axiosInstance.get(`/bookings/hospital/${hospitalId}`)).data,
  getByPilot: async (pilotId) => (await axiosInstance.get(`/bookings/pilot/${pilotId}`)).data,
  create: async (data) => (await axiosInstance.post('/bookings', data)).data,
  updateStatus: async (id, statusData) => (await axiosInstance.patch(`/bookings/${id}/status`, statusData)).data,
  assignAmbulance: async (id, ambulanceId) => (await axiosInstance.put(`/bookings/${id}/assign-ambulance`, { airAmbulanceId: ambulanceId })).data,
  cancel: async (id) => (await axiosInstance.delete(`/bookings/${id}`)).data,
};
