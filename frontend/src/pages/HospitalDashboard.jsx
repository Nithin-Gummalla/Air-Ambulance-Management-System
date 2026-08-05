import React, { useEffect, useState } from 'react';
import { Box, Typography, Grid, Card, CardContent, Button } from '@mui/material';
import { LocalHospital, Emergency, LocalShipping } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import StatCard from '../components/StatCard';
import LoadingSpinner from '../components/LoadingSpinner';
import { dashboardService } from '../api/dashboard.service';

const HospitalDashboard = () => {
  const navigate = useNavigate();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    dashboardService.getHospitalStats()
      .then(setStats)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <LoadingSpinner message="Loading Hospital Center Stats..." />;

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 800 }}>Hospital Trauma Command</Typography>
          <Typography variant="body2" sx={{ color: '#94A3B8' }}>Inbound Airlifts & ICU Bed Availability</Typography>
        </Box>
        <Button variant="contained" onClick={() => navigate('/organ-transports')} startIcon={<LocalShipping />}>
          Request Organ Transport
        </Button>
      </Box>

      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={4}>
          <StatCard title="Inbound Airlifts" value={stats?.emergencyRequests || 0} icon={<Emergency />} color="#EF4444" subtitle="Active Emergency Transports" />
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <StatCard title="Available Helicopters" value={stats?.availableAmbulances || 0} icon={<LocalHospital />} color="#0284C7" subtitle="On Standby at Base" />
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <StatCard title="Organ Logistics" value={stats?.activeOrganTransports || 0} icon={<LocalShipping />} color="#F59E0B" subtitle="Organ Transport Shipments" />
        </Grid>
      </Grid>
    </Box>
  );
};

export default HospitalDashboard;
