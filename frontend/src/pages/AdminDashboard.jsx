import React, { useEffect, useState } from 'react';
import { Box, Typography, Grid, Card, CardContent, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button } from '@mui/material';
import { LocalHospital, FlightTakeoff, Sick, Emergency, LocalShipping, Person } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import StatCard from '../components/StatCard';
import StatusChip from '../components/StatusChip';
import LoadingSpinner from '../components/LoadingSpinner';
import { dashboardService } from '../api/dashboard.service';

const AdminDashboard = () => {
  const navigate = useNavigate();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const data = await dashboardService.getAdminStats();
        setStats(data);
      } catch (err) {
        console.error('Failed to load admin stats', err);
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  if (loading) return <LoadingSpinner message="Fetching live fleet metrics..." />;

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 800 }}>Admin Mission Control</Typography>
          <Typography variant="body2" sx={{ color: '#94A3B8' }}>Real-time Air Ambulance Fleet & Dispatch Overview</Typography>
        </Box>
        <Button variant="contained" color="error" startIcon={<Emergency />} onClick={() => navigate('/bookings')}>
          New Dispatch
        </Button>
      </Box>

      {/* Stat Grid */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard title="Total Hospitals" value={stats?.totalHospitals || 0} icon={<LocalHospital />} color="#0284C7" subtitle="Verified Trauma Centers" />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard title="Total Aircraft" value={stats?.totalAmbulances || 0} icon={<FlightTakeoff />} color="#06B6D4" subtitle={`${stats?.availableAmbulances || 0} Available for Dispatch`} />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard title="Emergency Requests" value={stats?.emergencyRequests || 0} icon={<Emergency />} color="#EF4444" subtitle="Pending Dispatch Action" />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard title="Organ Transports" value={stats?.activeOrganTransports || 0} icon={<LocalShipping />} color="#F59E0B" subtitle="Active Heart/Liver Flights" />
        </Grid>
      </Grid>

      {/* Recent Emergency Dispatch Table */}
      <Card sx={{ mb: 4 }}>
        <CardContent>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
            <Typography variant="h6" sx={{ fontWeight: 700 }}>Recent Emergency Bookings</Typography>
            <Button size="small" onClick={() => navigate('/bookings')}>View All</Button>
          </Box>

          <TableContainer component={Paper} sx={{ bgcolor: 'transparent', boxShadow: 'none' }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Booking ID</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Patient Name</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Pickup Location</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Destination Hospital</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Level</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Status</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {stats?.recentBookings && stats.recentBookings.length > 0 ? (
                  stats.recentBookings.map((b) => (
                    <TableRow key={b.id} hover sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                      <TableCell sx={{ fontWeight: 700, color: '#38BDF8' }}>#{b.id}</TableCell>
                      <TableCell sx={{ color: '#F8FAFC' }}>{b.patient?.name || 'N/A'}</TableCell>
                      <TableCell sx={{ color: '#94A3B8' }}>{b.pickupLocation}</TableCell>
                      <TableCell sx={{ color: '#94A3B8' }}>{b.destinationHospital?.name || 'N/A'}</TableCell>
                      <TableCell><StatusChip status={b.emergencyLevel} /></TableCell>
                      <TableCell><StatusChip status={b.status} /></TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={6} align="center" sx={{ color: '#64748B', py: 3 }}>No recent bookings available</TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
        </CardContent>
      </Card>
    </Box>
  );
};

export default AdminDashboard;
