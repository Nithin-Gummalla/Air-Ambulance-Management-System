import React, { useState } from 'react';
import { 
  Box, Container, Card, CardContent, Typography, TextField, Button, Alert, Link as MuiLink, Stack, MenuItem, CircularProgress 
} from '@mui/material';
import { LocalHospital } from '@mui/icons-material';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const roles = [
  { value: 'ROLE_PATIENT', label: 'Patient' },
  { value: 'ROLE_HOSPITAL', label: 'Hospital' },
  { value: 'ROLE_PILOT', label: 'Pilot' },
  { value: 'ROLE_ADMIN', label: 'System Administrator' },
];

const Register = () => {
  const navigate = useNavigate();
  const { register } = useAuth();

  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    phone: '',
    role: 'ROLE_PATIENT',
    licenseNumber: '',
    experienceYears: 5,
    address: '',
    city: '',
    age: 30,
    gender: 'Male',
    bloodGroup: 'O+',
    emergencyContact: '',
  });

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
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
      await register(formData);
      setSuccess('Account created successfully! Redirecting to login...');
      setTimeout(() => navigate('/login'), 1500);
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ minHeight: '90vh', display: 'flex', alignItems: 'center', justifyContent: 'center', py: 6 }}>
      <Container maxWidth="sm">
        <Card sx={{ p: 2, bgcolor: '#1E293B', border: '1px solid rgba(255, 255, 255, 0.08)' }}>
          <CardContent>
            <Box sx={{ textAlign: 'center', mb: 3 }}>
              <LocalHospital sx={{ color: '#0284C7', fontSize: 44, mb: 1 }} />
              <Typography variant="h5" sx={{ fontWeight: 800 }}>Create Account</Typography>
              <Typography variant="body2" sx={{ color: '#94A3B8' }}>Join the Air Ambulance Emergency Network</Typography>
            </Box>

            {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
            {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}

            <form onSubmit={handleSubmit}>
              <Stack spacing={2}>
                <TextField
                  label="Role"
                  name="role"
                  select
                  value={formData.role}
                  onChange={handleChange}
                  fullWidth
                >
                  {roles.map((r) => (
                    <MenuItem key={r.value} value={r.value}>{r.label}</MenuItem>
                  ))}
                </TextField>

                <TextField
                  label="Full Name / Organization"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  required
                  fullWidth
                />

                <TextField
                  label="Email Address"
                  name="email"
                  type="email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                  fullWidth
                />

                <TextField
                  label="Password"
                  name="password"
                  type="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                  fullWidth
                />

                <TextField
                  label="Phone Number"
                  name="phone"
                  value={formData.phone}
                  onChange={handleChange}
                  required
                  fullWidth
                />

                {/* Conditional Fields Based on Role */}
                {formData.role === 'ROLE_PILOT' && (
                  <>
                    <TextField
                      label="Aviation License Number"
                      name="licenseNumber"
                      value={formData.licenseNumber}
                      onChange={handleChange}
                      required
                      fullWidth
                    />
                    <TextField
                      label="Flight Experience (Years)"
                      name="experienceYears"
                      type="number"
                      value={formData.experienceYears}
                      onChange={handleChange}
                      required
                      fullWidth
                    />
                  </>
                )}

                {formData.role === 'ROLE_HOSPITAL' && (
                  <>
                    <TextField
                      label="Street Address"
                      name="address"
                      value={formData.address}
                      onChange={handleChange}
                      required
                      fullWidth
                    />
                    <TextField
                      label="City"
                      name="city"
                      value={formData.city}
                      onChange={handleChange}
                      required
                      fullWidth
                    />
                  </>
                )}

                {formData.role === 'ROLE_PATIENT' && (
                  <>
                    <Box sx={{ display: 'flex', gap: 2 }}>
                      <TextField
                        label="Age"
                        name="age"
                        type="number"
                        value={formData.age}
                        onChange={handleChange}
                        fullWidth
                      />
                      <TextField
                        label="Blood Group"
                        name="bloodGroup"
                        value={formData.bloodGroup}
                        onChange={handleChange}
                        fullWidth
                      />
                    </Box>
                    <TextField
                      label="Emergency Contact Number"
                      name="emergencyContact"
                      value={formData.emergencyContact}
                      onChange={handleChange}
                      fullWidth
                    />
                  </>
                )}

                <Button 
                  type="submit" 
                  variant="contained" 
                  size="large" 
                  disabled={loading}
                  sx={{ py: 1.5, fontWeight: 700, mt: 1 }}
                >
                  {loading ? <CircularProgress size={24} color="inherit" /> : 'Register Account'}
                </Button>
              </Stack>
            </form>

            <Box sx={{ mt: 3, textAlign: 'center' }}>
              <Typography variant="body2" sx={{ color: '#94A3B8' }}>
                Already registered?{' '}
                <MuiLink component={Link} to="/login" sx={{ color: '#38BDF8', fontWeight: 600 }}>
                  Sign In Here
                </MuiLink>
              </Typography>
            </Box>
          </CardContent>
        </Card>
      </Container>
    </Box>
  );
};

export default Register;
