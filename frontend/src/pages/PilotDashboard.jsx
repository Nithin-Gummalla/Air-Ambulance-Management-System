import React, { useEffect, useState } from 'react';
import { Box, Typography, Grid, Card, CardContent, Button } from '@mui/material';
import { FlightTakeoff, Emergency, Person } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import StatCard from '../components/StatCard';
import LoadingSpinner from '../components/LoadingSpinner';
import { dashboardService } from '../api/dashboard.service';

const PilotDashboard = () => {
  const navigate = useNavigate();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    dashboardService.getPilotStats()
      .then(setStats)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <LoadingSpinner message="Connecting to Flight Cockpit Network..." />;

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 800 }}>Pilot Flight Deck</Typography>
          <Typography variant="body2" sx={{ color: '#94A3B8' }}>Assigned Air Ambulances & Flight Mission Schedules</Typography>
        </Box>
        <Button variant="contained" onClick={() => navigate('/bookings')} startIcon={<FlightTakeoff />}>
          View Mission Dispatch
        </Button>
      </Box>

      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={4}>
          <StatCard title="Assigned Missions" value={stats?.emergencyRequests || 0} icon={<Emergency />} color="#EF4444" subtitle="Active Flight Assignments" />
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <StatCard title="Flights Completed Today" value={stats?.flightsToday || 0} icon={<FlightTakeoff />} color="#10B981" subtitle="Successful Med-Evac Flights" />
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <StatCard title="Fleet Aircraft Ready" value={stats?.availableAmbulances || 0} icon={<Person />} color="#0284C7" subtitle="Fueled & Certified" />
        </Grid>
      </Grid>
    </Box>
  );
};

export default PilotDashboard;
