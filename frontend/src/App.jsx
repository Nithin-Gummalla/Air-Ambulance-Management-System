import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, CssBaseline, Box } from '@mui/material';
import theme from './theme/theme';
import { AuthProvider } from './context/AuthContext';
import Navbar from './components/Navbar';
import Sidebar from './components/Sidebar';
import Footer from './components/Footer';
import ProtectedRoute from './components/ProtectedRoute';

import Landing from './pages/Landing';
import Login from './pages/Login';
import Register from './pages/Register';
import AdminDashboard from './pages/AdminDashboard';
import HospitalDashboard from './pages/HospitalDashboard';
import PilotDashboard from './pages/PilotDashboard';
import PatientDashboard from './pages/PatientDashboard';
import Hospitals from './pages/Hospitals';
import Patients from './pages/Patients';
import AirAmbulances from './pages/AirAmbulances';
import Bookings from './pages/Bookings';
import OrganTransports from './pages/OrganTransports';
import Notifications from './pages/Notifications';
import Profile from './pages/Profile';

function AppContent() {
  const [mobileOpen, setMobileOpen] = useState(false);
  const handleToggleSidebar = () => setMobileOpen(!mobileOpen);

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh', bgcolor: '#0F172A' }}>
      <Navbar onToggleSidebar={handleToggleSidebar} />
      <Box sx={{ display: 'flex', flexGrow: 1 }}>
        <Sidebar mobileOpen={mobileOpen} onToggleSidebar={handleToggleSidebar} />
        <Box component="main" sx={{ flexGrow: 1, minWidth: 0 }}>
          <Routes>
            {/* Public Routes */}
            <Route path="/" element={<Landing />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            {/* Protected Role-Based Dashboards */}
            <Route element={<ProtectedRoute allowedRoles={['ROLE_ADMIN']} />}>
              <Route path="/admin-dashboard" element={<AdminDashboard />} />
            </Route>
            <Route element={<ProtectedRoute allowedRoles={['ROLE_HOSPITAL', 'ROLE_ADMIN']} />}>
              <Route path="/hospital-dashboard" element={<HospitalDashboard />} />
            </Route>
            <Route element={<ProtectedRoute allowedRoles={['ROLE_PILOT', 'ROLE_ADMIN']} />}>
              <Route path="/pilot-dashboard" element={<PilotDashboard />} />
            </Route>
            <Route element={<ProtectedRoute allowedRoles={['ROLE_PATIENT', 'ROLE_ADMIN']} />}>
              <Route path="/patient-dashboard" element={<PatientDashboard />} />
            </Route>

            {/* General Protected Feature Routes */}
            <Route element={<ProtectedRoute />}>
              <Route path="/hospitals" element={<Hospitals />} />
              <Route path="/patients" element={<Patients />} />
              <Route path="/ambulances" element={<AirAmbulances />} />
              <Route path="/bookings" element={<Bookings />} />
              <Route path="/organ-transports" element={<OrganTransports />} />
              <Route path="/notifications" element={<Notifications />} />
              <Route path="/profile" element={<Profile />} />
            </Route>

            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Box>
      </Box>
      <Footer />
    </Box>
  );
}

export default function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <Router>
          <AppContent />
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
}
