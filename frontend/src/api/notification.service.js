import axiosInstance from './axiosInstance';

export const notificationService = {
  getNotifications: async () => (await axiosInstance.get('/notifications')).data,
  markAsRead: async (id) => (await axiosInstance.patch(`/notifications/${id}/read`)).data,
  markAllAsRead: async () => (await axiosInstance.patch('/notifications/read-all')).data,
};
