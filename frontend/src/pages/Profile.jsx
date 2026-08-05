import React from 'react';
import { Box, Typography, Card, CardContent, Avatar, Grid, Chip, Divider, Button } from '@mui/material';
import { AccountCircle, Email, Phone, Security } from '@mui/icons-material';
import { useAuth } from '../context/AuthContext';

const Profile = () => {
  const { user, logout } = useAuth();

  if (!user) return null;

  return (
    <Box sx={{ p: { xs: 2, md: 4 }, maxWidth: 800, mx: 'auto' }}>
      <Typography variant="h4" sx={{ fontWeight: 800, mb: 3 }}>User Profile</Typography>

      <Card sx={{ mb: 4 }}>
        <CardContent sx={{ p: 4 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 3, mb: 4 }}>
            <Avatar sx={{ width: 80, height: 80, bgcolor: '#0284C7', fontSize: 36, fontWeight: 700 }}>
              {user.name ? user.name.charAt(0).toUpperCase() : 'U'}
            </Avatar>
            <Box>
              <Typography variant="h5" sx={{ fontWeight: 800, color: '#F8FAFC' }}>{user.name}</Typography>
              <Typography variant="body2" sx={{ color: '#94A3B8', mb: 1 }}>{user.email}</Typography>
              <Chip 
                label={user.roles ? user.roles[0].replace('ROLE_', '') : 'USER'} 
                color="primary" 
                size="small"
                sx={{ fontWeight: 700 }}
              />
            </Box>
          </Box>

          <Divider sx={{ borderColor: 'rgba(255, 255, 255, 0.08)', mb: 3 }} />

          <Grid container spacing={3}>
            <Grid item xs={12} sm={6}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
                <Email sx={{ color: '#0284C7' }} />
                <Box>
                  <Typography variant="caption" sx={{ color: '#64748B' }}>Email Address</Typography>
                  <Typography variant="body1" sx={{ color: '#F8FAFC', fontWeight: 600 }}>{user.email}</Typography>
                </Box>
              </Box>
            </Grid>

            <Grid item xs={12} sm={6}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
                <Phone sx={{ color: '#06B6D4' }} />
                <Box>
                  <Typography variant="caption" sx={{ color: '#64748B' }}>Phone Contact</Typography>
                  <Typography variant="body1" sx={{ color: '#F8FAFC', fontWeight: 600 }}>{user.phone || 'N/A'}</Typography>
                </Box>
              </Box>
            </Grid>

            <Grid item xs={12} sm={6}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
                <Security sx={{ color: '#10B981' }} />
                <Box>
                  <Typography variant="caption" sx={{ color: '#64748B' }}>Security Role</Typography>
                  <Typography variant="body1" sx={{ color: '#F8FAFC', fontWeight: 600 }}>
                    {user.roles ? user.roles.join(', ') : 'ROLE_USER'}
                  </Typography>
                </Box>
              </Box>
            </Grid>
          </Grid>
        </CardContent>
      </Card>
    </Box>
  );
};

export default Profile;
