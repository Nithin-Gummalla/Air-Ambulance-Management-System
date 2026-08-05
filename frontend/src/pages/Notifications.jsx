import React, { useEffect, useState } from 'react';
import { 
  Box, Typography, Button, Card, CardContent, List, ListItem, ListItemText, ListItemIcon, Divider, Chip 
} from '@mui/material';
import { Notifications as NotificationsIcon, DoneAll, Emergency, FlightTakeoff, LocalShipping } from '@mui/icons-material';
import { notificationService } from '../api/notification.service';
import LoadingSpinner from '../components/LoadingSpinner';

const Notifications = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadNotifications = async () => {
    try {
      const data = await notificationService.getNotifications();
      setNotifications(data);
    } catch (err) {
      console.error('Failed to load notifications', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadNotifications();
  }, []);

  const handleMarkAsRead = async (id) => {
    await notificationService.markAsRead(id);
    loadNotifications();
  };

  const handleMarkAllRead = async () => {
    await notificationService.markAllAsRead();
    loadNotifications();
  };

  const getIcon = (type) => {
    if (type?.includes('FLIGHT')) return <FlightTakeoff sx={{ color: '#0284C7' }} />;
    if (type?.includes('ORGAN')) return <LocalShipping sx={{ color: '#F59E0B' }} />;
    return <Emergency sx={{ color: '#EF4444' }} />;
  };

  if (loading) return <LoadingSpinner message="Fetching Notifications..." />;

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 800 }}>Notifications & Alerts</Typography>
          <Typography variant="body2" sx={{ color: '#94A3B8' }}>Real-time Emergency Mission Notifications</Typography>
        </Box>
        <Button variant="outlined" startIcon={<DoneAll />} onClick={handleMarkAllRead}>
          Mark All Read
        </Button>
      </Box>

      <Card>
        <CardContent sx={{ p: 0 }}>
          <List sx={{ py: 0 }}>
            {notifications.length > 0 ? (
              notifications.map((n, idx) => (
                <React.Fragment key={n.id}>
                  {idx > 0 && <Divider sx={{ borderColor: 'rgba(255, 255, 255, 0.08)' }} />}
                  <ListItem 
                    sx={{ 
                      py: 2.5, 
                      px: 3, 
                      bgcolor: n.isRead ? 'transparent' : 'rgba(2, 132, 199, 0.08)',
                      transition: '0.2s',
                      '&:hover': { bgcolor: 'rgba(255, 255, 255, 0.03)' }
                    }}
                    secondaryAction={
                      !n.isRead && (
                        <Button size="small" onClick={() => handleMarkAsRead(n.id)}>
                          Mark Read
                        </Button>
                      )
                    }
                  >
                    <ListItemIcon>{getIcon(n.type)}</ListItemIcon>
                    <ListItemText 
                      primary={
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 0.5 }}>
                          <Typography variant="subtitle1" sx={{ fontWeight: 700, color: '#F8FAFC' }}>
                            {n.title}
                          </Typography>
                          {!n.isRead && <Chip label="New" color="error" size="small" sx={{ height: 20, fontSize: '0.7rem' }} />}
                        </Box>
                      }
                      secondary={
                        <Box>
                          <Typography variant="body2" sx={{ color: '#94A3B8' }}>{n.message}</Typography>
                          <Typography variant="caption" sx={{ color: '#64748B', mt: 0.5, display: 'block' }}>
                            {new Date(n.createdAt).toLocaleString()}
                          </Typography>
                        </Box>
                      }
                    />
                  </ListItem>
                </React.Fragment>
              ))
            ) : (
              <Box sx={{ py: 6, textAlign: 'center' }}>
                <NotificationsIcon sx={{ fontSize: 48, color: '#64748B', mb: 1 }} />
                <Typography variant="body1" sx={{ color: '#94A3B8' }}>No notifications found</Typography>
              </Box>
            )}
          </List>
        </CardContent>
      </Card>
    </Box>
  );
};

export default Notifications;
