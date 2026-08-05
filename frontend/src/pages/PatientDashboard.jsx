import React, { useEffect, useState } from 'react';
import { Box, Typography, Grid, Button, Card, CardContent } from '@mui/material';
import { Emergency, FlightTakeoff, LocalHospital } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import StatCard from '../components/StatCard';
import LoadingSpinner from '../components/LoadingSpinner';
import { dashboardService } from '../api/dashboard.service';

const PatientDashboard = () => {
  const navigate = useNavigate();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    dashboardService.getPatientStats()
      .then(setStats)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <LoadingSpinner message="Loading Patient Services..." />;

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 800 }}>Patient Emergency Services</Typography>
          <Typography variant="body2" sx={{ color: '#94A3B8' }}>Track Emergency Airlift Status & Booking History</Typography>
        </Box>
        <Button 
          variant="contained" 
          color="error" 
          size="large"
          startIcon={<Emergency />} 
          onClick={() => navigate('/bookings')}
          sx={{ py: 1.5, px: 3, fontWeight: 700 }}
        >
          Request Immediate Evacuation
        </Button>
      </Box>

      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={4}>
          <StatCard title="Active Transport Requests" value={stats?.emergencyRequests || 0} icon={<Emergency />} color="#EF4444" subtitle="Dispatch Status" />
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <StatCard title="Available Helicopters" value={stats?.availableAmbulances || 0} icon={<FlightTakeoff />} color="#0284C7" subtitle="Ready for Immediate Flight" />
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <StatCard title="Partner Hospitals" value={stats?.totalHospitals || 0} icon={<LocalHospital />} color="#10B981" subtitle="ICU Trauma Network" />
        </Grid>
      </Grid>
    </Box>
  );
};

export default PatientDashboard;
