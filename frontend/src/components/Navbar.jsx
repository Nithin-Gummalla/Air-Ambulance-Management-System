import React, { useState } from 'react';
import { 
  AppBar, Toolbar, Typography, Button, IconButton, Box, Badge, Avatar, Menu, MenuItem, Tooltip 
} from '@mui/material';
import { 
  LocalHospital, Notifications as NotificationsIcon, AccountCircle, Menu as MenuIcon, ExitToApp, Dashboard 
} from '@mui/icons-material';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Navbar = ({ onToggleSidebar }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = useState(null);

  const handleMenuOpen = (e) => setAnchorEl(e.currentTarget);
  const handleMenuClose = () => setAnchorEl(null);

  const handleLogout = () => {
    handleMenuClose();
    logout();
    navigate('/login');
  };

  const getDashboardRoute = () => {
    if (!user || !user.roles) return '/login';
    if (user.roles.includes('ROLE_ADMIN')) return '/admin-dashboard';
    if (user.roles.includes('ROLE_HOSPITAL')) return '/hospital-dashboard';
    if (user.roles.includes('ROLE_PILOT')) return '/pilot-dashboard';
    return '/patient-dashboard';
  };

  return (
    <AppBar 
      position="sticky" 
      sx={{ 
        backgroundColor: 'rgba(15, 23, 42, 0.85)', 
        backdropFilter: 'blur(12px)', 
        borderBottom: '1px solid rgba(255, 255, 255, 0.08)',
        boxShadow: 'none',
        zIndex: (theme) => theme.zIndex.drawer + 1
      }}
    >
      <Toolbar sx={{ justifyContent: 'space-between' }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          {user && (
            <IconButton color="inherit" onClick={onToggleSidebar} edge="start" sx={{ mr: 1, display: { md: 'none' } }}>
              <MenuIcon />
            </IconButton>
          )}
          <LocalHospital sx={{ color: '#0284C7', fontSize: 32 }} />
          <Typography 
            variant="h6" 
            component={Link} 
            to="/" 
            sx={{ 
              textDecoration: 'none', 
              color: '#F8FAFC', 
              fontWeight: 800, 
              letterSpacing: '-0.5px',
              display: 'flex',
              alignItems: 'center',
              gap: 1
            }}
          >
            AIR <span style={{ color: '#0284C7' }}>AMBULANCE</span>
          </Typography>
        </Box>

        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          {user ? (
            <>
              <Button 
                component={Link} 
                to={getDashboardRoute()} 
                startIcon={<Dashboard />} 
                sx={{ color: '#94A3B8', '&:hover': { color: '#F8FAFC' }, display: { xs: 'none', sm: 'inline-flex' } }}
              >
                Dashboard
              </Button>

              <IconButton component={Link} to="/notifications" color="inherit">
                <Badge badgeContent={3} color="error">
                  <NotificationsIcon />
                </Badge>
              </IconButton>

              <Tooltip title="Account Settings">
                <IconButton onClick={handleMenuOpen} size="small">
                  <Avatar sx={{ bgcolor: '#0284C7', width: 36, height: 36, fontWeight: 700 }}>
                    {user.name ? user.name.charAt(0).toUpperCase() : 'U'}
                  </Avatar>
                </IconButton>
              </Tooltip>

              <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
                PaperProps={{
                  sx: {
                    bgcolor: '#1E293B',
                    border: '1px solid rgba(255, 255, 255, 0.08)',
                    mt: 1.5,
                  }
                }}
              >
                <MenuItem onClick={() => { handleMenuClose(); navigate('/profile'); }}>
                  <AccountCircle sx={{ mr: 1, color: '#0284C7' }} /> Profile
                </MenuItem>
                <MenuItem onClick={handleLogout} sx={{ color: '#EF4444' }}>
                  <ExitToApp sx={{ mr: 1 }} /> Logout
                </MenuItem>
              </Menu>
            </>
          ) : (
            <>
              <Button component={Link} to="/login" variant="text" sx={{ color: '#94A3B8' }}>
                Sign In
              </Button>
              <Button component={Link} to="/register" variant="contained">
                Register
              </Button>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
