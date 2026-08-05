import React from 'react';
import { Chip } from '@mui/material';

const statusConfig = {
  // Booking Statuses
  REQUESTED: { label: 'Requested', color: 'warning' },
  APPROVED: { label: 'Approved', color: 'info' },
  ASSIGNED: { label: 'Assigned', color: 'primary' },
  IN_TRANSIT: { label: 'In Transit', color: 'secondary' },
  COMPLETED: { label: 'Completed', color: 'success' },
  CANCELLED: { label: 'Cancelled', color: 'error' },

  // Ambulance & Pilot Statuses
  AVAILABLE: { label: 'Available', color: 'success' },
  IN_SERVICE: { label: 'In Mission', color: 'warning' },
  ON_MISSION: { label: 'On Mission', color: 'warning' },
  MAINTENANCE: { label: 'Maintenance', color: 'error' },
  OFF_DUTY: { label: 'Off Duty', color: 'default' },

  // Emergency Levels & Priority
  CRITICAL: { label: 'Critical', color: 'error' },
  HIGH: { label: 'High Priority', color: 'warning' },
  MODERATE: { label: 'Moderate', color: 'info' },
  MEDIUM: { label: 'Medium', color: 'info' },
  LOW: { label: 'Low', color: 'default' },
};

const StatusChip = ({ status, size = 'small' }) => {
  const config = statusConfig[status] || { label: status || 'Unknown', color: 'default' };

  return (
    <Chip 
      label={config.label} 
      color={config.color} 
      size={size} 
      sx={{ fontWeight: 700, borderRadius: '8px' }}
    />
  );
};

export default StatusChip;
