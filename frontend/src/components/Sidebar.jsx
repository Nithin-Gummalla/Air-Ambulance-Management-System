import React from 'react';
import { 
  Drawer, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Box, Divider, Typography 
} from '@mui/material';
import { 
  Dashboard, LocalHospital, FlightTakeoff, Person, Sick, LocalShipping, Notifications, AccountBox, Emergency 
} from '@mui/icons-material';
import { useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const drawerWidth = 260;

const Sidebar = ({ mobileOpen, onToggleSidebar }) => {
  const { user } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  if (!user) return null;

  const isAdmin = user.roles?.includes('ROLE_ADMIN');
  const isHospital = user.roles?.includes('ROLE_HOSPITAL');
  const isPilot = user.roles?.includes('ROLE_PILOT');
  const isPatient = user.roles?.includes('ROLE_PATIENT');

  const menuItems = [
    { text: 'Dashboard', icon: <Dashboard />, path: isAdmin ? '/admin-dashboard' : isHospital ? '/hospital-dashboard' : isPilot ? '/pilot-dashboard' : '/patient-dashboard' },
    { text: 'Emergency Bookings', icon: <Emergency />, path: '/bookings' },
    { text: 'Air Ambulances', icon: <FlightTakeoff />, path: '/ambulances' },
    { text: 'Hospitals', icon: <LocalHospital />, path: '/hospitals' },
    { text: 'Organ Transports', icon: <LocalShipping />, path: '/organ-transports' },
    { text: 'Patients', icon: <Sick />, path: '/patients' },
    { text: 'Pilots', icon: <Person />, path: '/pilots', show: isAdmin },
    { text: 'Notifications', icon: <Notifications />, path: '/notifications' },
    { text: 'My Profile', icon: <AccountBox />, path: '/profile' },
  ].filter(item => item.show === undefined || item.show);

  const drawerContent = (
    <Box sx={{ overflow: 'auto', py: 2 }}>
      <Box sx={{ px: 3, pb: 2 }}>
        <Typography variant="caption" sx={{ color: '#64748B', fontWeight: 700, textTransform: 'uppercase', letterSpacing: 1 }}>
          Logged in as
        </Typography>
        <Typography variant="subtitle2" sx={{ color: '#F8FAFC', fontWeight: 700, mt: 0.5 }}>
          {user.name}
        </Typography>
        <Typography variant="caption" sx={{ color: '#0284C7', fontWeight: 600 }}>
          {user.roles ? user.roles[0].replace('ROLE_', '') : 'USER'}
        </Typography>
      </Box>
      <Divider sx={{ borderColor: 'rgba(255, 255, 255, 0.08)' }} />
      <List sx={{ px: 1.5, pt: 2 }}>
        {menuItems.map((item) => {
          const isActive = location.pathname === item.path;
          return (
            <ListItem key={item.text} disablePadding sx={{ mb: 0.5 }}>
              <ListItemButton
                onClick={() => {
                  navigate(item.path);
                  if (mobileOpen) onToggleSidebar();
                }}
                sx={{
                  borderRadius: '10px',
                  bgcolor: isActive ? 'rgba(2, 132, 199, 0.15)' : 'transparent',
                  color: isActive ? '#38BDF8' : '#94A3B8',
                  borderLeft: isActive ? '3px solid #0284C7' : '3px solid transparent',
                  '&:hover': {
                    bgcolor: 'rgba(2, 132, 199, 0.1)',
                    color: '#F8FAFC',
                  },
                }}
              >
                <ListItemIcon sx={{ color: isActive ? '#0284C7' : '#64748B', minWidth: 40 }}>
                  {item.icon}
                </ListItemIcon>
                <ListItemText primary={item.text} primaryTypographyProps={{ fontSize: '0.9rem', fontWeight: isActive ? 700 : 500 }} />
              </ListItemButton>
            </ListItem>
          );
        })}
      </List>
    </Box>
  );

  return (
    <>
      {/* Mobile Drawer */}
      <Drawer
        variant="temporary"
        open={mobileOpen}
        onClose={onToggleSidebar}
        ModalProps={{ keepMounted: true }}
        sx={{
          display: { xs: 'block', md: 'none' },
          '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth, bgcolor: '#0F172A', borderRight: '1px solid rgba(255, 255, 255, 0.08)' },
        }}
      >
        {drawerContent}
      </Drawer>

      {/* Desktop Permanent Drawer */}
      <Drawer
        variant="permanent"
        sx={{
          display: { xs: 'none', md: 'block' },
          width: drawerWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': { 
            boxSizing: 'border-box', 
            width: drawerWidth, 
            bgcolor: '#0F172A', 
            borderRight: '1px solid rgba(255, 255, 255, 0.08)',
            mt: '64px'
          },
        }}
        open
      >
        {drawerContent}
      </Drawer>
    </>
  );
};

export default Sidebar;
