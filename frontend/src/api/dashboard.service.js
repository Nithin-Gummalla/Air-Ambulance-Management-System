import axiosInstance from './axiosInstance';

export const dashboardService = {
  getAdminStats: async () => (await axiosInstance.get('/dashboard/admin')).data,
  getHospitalStats: async () => (await axiosInstance.get('/dashboard/hospital')).data,
  getPilotStats: async () => (await axiosInstance.get('/dashboard/pilot')).data,
  getPatientStats: async () => (await axiosInstance.get('/dashboard/patient')).data,
};
