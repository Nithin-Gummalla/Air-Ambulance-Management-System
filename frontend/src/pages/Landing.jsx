import React from 'react';
import { Box, Container, Typography, Button, Grid, Card, CardContent, Stack, Chip } from '@mui/material';
import { LocalHospital, FlightTakeoff, LocalShipping, Speed, Security, SupportAgent, ArrowForward } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Landing = () => {
  const navigate = useNavigate();
  const { user } = useAuth();

  return (
    <Box sx={{ bgcolor: '#0F172A', color: '#F8FAFC', minHeight: '100vh', pt: 6, pb: 10 }}>
      {/* Hero Section */}
      <Container maxWidth="lg">
        <Grid container spacing={6} alignItems="center">
          <Grid item xs={12} md={7}>
            <Chip 
              label="24/7 Rapid Emergency Dispatch" 
              color="primary" 
              size="small" 
              sx={{ mb: 2, fontWeight: 700, bgcolor: 'rgba(2, 132, 199, 0.2)', color: '#38BDF8', border: '1px solid #0284C7' }} 
            />
            <Typography variant="h2" component="h1" sx={{ fontWeight: 800, mb: 2, leadingHeight: 1.2 }}>
              Lifesaving Emergency <span style={{ color: '#0284C7' }}>Air Transport</span> Network
            </Typography>
            <Typography variant="h6" sx={{ color: '#94A3B8', fontWeight: 400, mb: 4 }}>
              Connecting patients, hospitals, trauma centers, and medical air fleets in real time. Speed, safety, and specialized critical care flight logistics.
            </Typography>

            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <Button 
                variant="contained" 
                size="large" 
                onClick={() => navigate(user ? '/bookings' : '/login')}
                startIcon={<FlightTakeoff />}
                sx={{ py: 1.5, px: 4, fontSize: '1.1rem', bgcolor: '#EF4444', '&:hover': { bgcolor: '#DC2626' } }}
              >
                Request Emergency Transport
              </Button>
              <Button 
                variant="outlined" 
                size="large" 
                onClick={() => navigate('/hospitals')}
                sx={{ py: 1.5, px: 3, color: '#38BDF8', borderColor: '#0284C7' }}
              >
                View Hospital Network
              </Button>
            </Stack>
          </Grid>

          <Grid item xs={12} md={5}>
            <Card sx={{ p: 2, bgcolor: '#1E293B', borderRadius: 4, border: '1px solid rgba(2, 132, 199, 0.3)', boxShadow: '0 20px 40px rgba(0,0,0,0.6)' }}>
              <CardContent>
                <Typography variant="h6" sx={{ color: '#38BDF8', fontWeight: 700, mb: 2, display: 'flex', alignItems: 'center', gap: 1 }}>
                  <Speed /> Live Dispatch Metrics
                </Typography>
                <Stack spacing={2}>
                  <Box sx={{ p: 2, bgcolor: 'rgba(15, 23, 42, 0.6)', borderRadius: 2 }}>
                    <Typography variant="caption" sx={{ color: '#64748B' }}>Average Dispatch Time</Typography>
                    <Typography variant="h5" sx={{ color: '#10B981', fontWeight: 800 }}>&lt; 8 Minutes</Typography>
                  </Box>
                  <Box sx={{ p: 2, bgcolor: 'rgba(15, 23, 42, 0.6)', borderRadius: 2 }}>
                    <Typography variant="caption" sx={{ color: '#64748B' }}>Active Fleet Aircraft</Typography>
                    <Typography variant="h5" sx={{ color: '#38BDF8', fontWeight: 800 }}>45+ Medical Copters & Jets</Typography>
                  </Box>
                  <Box sx={{ p: 2, bgcolor: 'rgba(15, 23, 42, 0.6)', borderRadius: 2 }}>
                    <Typography variant="caption" sx={{ color: '#64748B' }}>Partner Hospital Helipads</Typography>
                    <Typography variant="h5" sx={{ color: '#F59E0B', fontWeight: 800 }}>120 Trauma Centers</Typography>
                  </Box>
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        </Grid>

        {/* Feature Cards */}
        <Box sx={{ mt: 10 }}>
          <Typography variant="h4" textAlign="center" sx={{ fontWeight: 800, mb: 6 }}>
            Comprehensive Air Evacuation Services
          </Typography>

          <Grid container spacing={4}>
            <Grid item xs={12} md={4}>
              <Card sx={{ height: '100%', p: 3, transition: '0.3s', '&:hover': { transform: 'translateY(-6px)' } }}>
                <LocalHospital sx={{ fontSize: 44, color: '#0284C7', mb: 2 }} />
                <Typography variant="h6" sx={{ fontWeight: 700, mb: 1 }}>Emergency Transport</Typography>
                <Typography variant="body2" sx={{ color: '#94A3B8' }}>
                  Rapid patient airlift from pickup locations directly to specialized ICU trauma centers with flight physicians.
                </Typography>
              </Card>
            </Grid>

            <Grid item xs={12} md={4}>
              <Card sx={{ height: '100%', p: 3, transition: '0.3s', '&:hover': { transform: 'translateY(-6px)' } }}>
                <LocalShipping sx={{ fontSize: 44, color: '#06B6D4', mb: 2 }} />
                <Typography variant="h6" sx={{ fontWeight: 700, mb: 1 }}>Organ Transport Logistics</Typography>
                <Typography variant="body2" sx={{ color: '#94A3B8' }}>
                  Time-critical organ transfer between donor and recipient hospitals in organ-preservation flight units.
                </Typography>
              </Card>
            </Grid>

            <Grid item xs={12} md={4}>
              <Card sx={{ height: '100%', p: 3, transition: '0.3s', '&:hover': { transform: 'translateY(-6px)' } }}>
                <SupportAgent sx={{ fontSize: 44, color: '#10B981', mb: 2 }} />
                <Typography variant="h6" sx={{ fontWeight: 700, mb: 1 }}>Inter-Hospital Transfer</Typography>
                <Typography variant="body2" sx={{ color: '#94A3B8' }}>
                  Seamless medical transport between regional hospitals for specialized cardiac, neurological, or burn surgeries.
                </Typography>
              </Card>
            </Grid>
          </Grid>
        </Box>
      </Container>
    </Box>
  );
};

export default Landing;
