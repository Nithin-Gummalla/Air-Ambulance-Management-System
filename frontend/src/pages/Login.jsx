import React, { useState } from 'react';
import { 
  Box, Container, Card, CardContent, Typography, TextField, Button, Alert, Link as MuiLink, Stack, CircularProgress 
} from '@mui/material';
import { LocalHospital, Lock, Email } from '@mui/icons-material';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Login = () => {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [formData, setFormData] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const user = await login(formData);
      if (user.roles.includes('ROLE_ADMIN')) navigate('/admin-dashboard');
      else if (user.roles.includes('ROLE_HOSPITAL')) navigate('/hospital-dashboard');
      else if (user.roles.includes('ROLE_PILOT')) navigate('/pilot-dashboard');
      else navigate('/patient-dashboard');
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid email or password credentials');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ minHeight: '85vh', display: 'flex', alignItems: 'center', justifyContent: 'center', py: 6 }}>
      <Container maxWidth="xs">
        <Card sx={{ p: 2, bgcolor: '#1E293B', border: '1px solid rgba(255, 255, 255, 0.08)' }}>
          <CardContent>
            <Box sx={{ textAlign: 'center', mb: 3 }}>
              <LocalHospital sx={{ color: '#0284C7', fontSize: 44, mb: 1 }} />
              <Typography variant="h5" sx={{ fontWeight: 800 }}>Welcome Back</Typography>
              <Typography variant="body2" sx={{ color: '#94A3B8' }}>Air Ambulance Management Portal</Typography>
            </Box>

            {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

            <form onSubmit={handleSubmit}>
              <Stack spacing={2.5}>
                <TextField
                  label="Email Address"
                  name="email"
                  type="email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                  fullWidth
                  InputProps={{
                    startAdornment: <Email sx={{ color: '#64748B', mr: 1 }} />,
                  }}
                />

                <TextField
                  label="Password"
                  name="password"
                  type="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                  fullWidth
                  InputProps={{
                    startAdornment: <Lock sx={{ color: '#64748B', mr: 1 }} />,
                  }}
                />

                <Button 
                  type="submit" 
                  variant="contained" 
                  size="large" 
                  disabled={loading}
                  sx={{ py: 1.5, fontWeight: 700 }}
                >
                  {loading ? <CircularProgress size={24} color="inherit" /> : 'Sign In'}
                </Button>
              </Stack>
            </form>

            <Box sx={{ mt: 3, textAlign: 'center' }}>
              <Typography variant="body2" sx={{ color: '#94A3B8' }}>
                Don't have an account?{' '}
                <MuiLink component={Link} to="/register" sx={{ color: '#38BDF8', fontWeight: 600 }}>
                  Register Here
                </MuiLink>
              </Typography>
            </Box>
          </CardContent>
        </Card>
      </Container>
    </Box>
  );
};

export default Login;
