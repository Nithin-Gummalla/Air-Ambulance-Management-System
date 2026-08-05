import React from 'react';
import { Card, CardContent, Typography, Box } from '@mui/material';

const StatCard = ({ title, value, icon, color = '#0284C7', subtitle }) => {
  return (
    <Card 
      sx={{ 
        height: '100%',
        position: 'relative',
        overflow: 'hidden',
        transition: 'transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out',
        '&:hover': {
          transform: 'translateY(-4px)',
          boxShadow: `0 12px 24px -10px ${color}40`,
        }
      }}
    >
      <Box 
        sx={{ 
          position: 'absolute', 
          top: 0, 
          left: 0, 
          width: '4px', 
          height: '100%', 
          bgcolor: color 
        }} 
      />
      <CardContent sx={{ p: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
          <Typography variant="subtitle2" sx={{ color: '#94A3B8', fontWeight: 600, textTransform: 'uppercase', letterSpacing: 0.5 }}>
            {title}
          </Typography>
          <Box 
            sx={{ 
              p: 1.5, 
              borderRadius: '12px', 
              bgcolor: `${color}15`, 
              color: color, 
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center'
            }}
          >
            {icon}
          </Box>
        </Box>

        <Typography variant="h4" sx={{ color: '#F8FAFC', fontWeight: 800, mb: 0.5 }}>
          {value}
        </Typography>

        {subtitle && (
          <Typography variant="caption" sx={{ color: '#64748B', fontWeight: 500 }}>
            {subtitle}
          </Typography>
        )}
      </CardContent>
    </Card>
  );
};

export default StatCard;
