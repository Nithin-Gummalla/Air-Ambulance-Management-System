import React from 'react';
import { Box, Typography, Container, Link as MuiLink } from '@mui/material';

const Footer = () => {
  return (
    <Box 
      component="footer" 
      sx={{ 
        py: 3, 
        px: 2, 
        mt: 'auto', 
        backgroundColor: '#0F172A', 
        borderTop: '1px solid rgba(255, 255, 255, 0.08)',
        textAlign: 'center' 
      }}
    >
      <Container maxWidth="lg">
        <Typography variant="body2" color="#64748B">
          © {new Date().getFullYear()} Air Ambulance Management System. All rights reserved. Emergency Medical Dispatch Network.
        </Typography>
      </Container>
    </Box>
  );
};

export default Footer;
