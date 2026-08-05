import React, { useEffect, useState } from 'react';
import { 
  Box, Typography, Button, Card, CardContent, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, 
  Paper, Dialog, DialogTitle, DialogContent, DialogActions, TextField, MenuItem, Chip 
} from '@mui/material';
import { Add, Emergency, FlightTakeoff, LocalHospital } from '@mui/icons-material';
import { bookingService } from '../api/booking.service';
import { hospitalService } from '../api/hospital.service';
import { ambulanceService } from '../api/ambulance.service';
import StatusChip from '../components/StatusChip';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';

const Bookings = () => {
  const { user } = useAuth();

  const [bookings, setBookings] = useState([]);
  const [hospitals, setHospitals] = useState([]);
  const [ambulances, setAmbulances] = useState([]);
  const [loading, setLoading] = useState(true);
  const [openModal, setOpenModal] = useState(false);

  const [formData, setFormData] = useState({
    patientName: user?.name || '',
    patientAge: 35,
    patientGender: 'Male',
    patientBloodGroup: 'O+',
    emergencyContact: '',
    medicalCondition: '',
    pickupLocation: '',
    destinationHospitalId: '',
    emergencyLevel: 'CRITICAL',
    airAmbulanceId: '',
    notes: '',
  });

  const loadData = async () => {
    try {
      const bData = await bookingService.getAll();
      const hData = await hospitalService.getAll();
      const aData = await ambulanceService.getAvailable();
      setBookings(bData);
      setHospitals(hData);
      setAmbulances(aData);
    } catch (err) {
      console.error('Failed to load emergency bookings', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleStatusUpdate = async (id, status) => {
    await bookingService.updateStatus(id, { status });
    loadData();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = {
      ...formData,
      destinationHospitalId: parseInt(formData.destinationHospitalId),
      airAmbulanceId: formData.airAmbulanceId ? parseInt(formData.airAmbulanceId) : null,
    };
    await bookingService.create(payload);
    setOpenModal(false);
    loadData();
  };

  if (loading) return <LoadingSpinner message="Loading Emergency Airlift Bookings..." />;

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3, flexWrap: 'wrap', gap: 2 }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 800 }}>Emergency Transport Dispatch</Typography>
          <Typography variant="body2" sx={{ color: '#94A3B8' }}>Live Airlift Requests & Flight Mission Status</Typography>
        </Box>
        <Button 
          variant="contained" 
          color="error" 
          startIcon={<Add />} 
          onClick={() => setOpenModal(true)}
          sx={{ fontWeight: 700 }}
        >
          Request Emergency Evacuation
        </Button>
      </Box>

      <Card>
        <CardContent sx={{ p: 0 }}>
          <TableContainer component={Paper} sx={{ bgcolor: 'transparent', boxShadow: 'none' }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>ID</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Patient Name</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Pickup Location</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Destination Hospital</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Air Ambulance</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Level</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Status</TableCell>
                  <TableCell align="right" sx={{ color: '#64748B', fontWeight: 700 }}>Action</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {bookings.map((b) => (
                  <TableRow key={b.id} hover>
                    <TableCell sx={{ fontWeight: 800, color: '#38BDF8' }}>#{b.id}</TableCell>
                    <TableCell sx={{ color: '#F8FAFC', fontWeight: 600 }}>{b.patient?.name || 'Emergency Patient'}</TableCell>
                    <TableCell sx={{ color: '#94A3B8' }}>{b.pickupLocation}</TableCell>
                    <TableCell sx={{ color: '#94A3B8' }}>{b.destinationHospital?.name}</TableCell>
                    <TableCell sx={{ color: '#38BDF8', fontWeight: 600 }}>
                      {b.airAmbulance ? b.airAmbulance.registrationNumber : 'Auto-Assigning...'}
                    </TableCell>
                    <TableCell><StatusChip status={b.emergencyLevel} /></TableCell>
                    <TableCell><StatusChip status={b.status} /></TableCell>
                    <TableCell align="right">
                      {b.status === 'REQUESTED' && (
                        <Button size="small" variant="contained" onClick={() => handleStatusUpdate(b.id, 'ASSIGNED')}>
                          Approve
                        </Button>
                      )}
                      {b.status === 'ASSIGNED' && (
                        <Button size="small" variant="contained" color="secondary" onClick={() => handleStatusUpdate(b.id, 'IN_TRANSIT')}>
                          Takeoff
                        </Button>
                      )}
                      {b.status === 'IN_TRANSIT' && (
                        <Button size="small" variant="contained" color="success" onClick={() => handleStatusUpdate(b.id, 'COMPLETED')}>
                          Landed / Complete
                        </Button>
                      )}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </CardContent>
      </Card>

      {/* New Booking Request Modal */}
      <Dialog open={openModal} onClose={() => setOpenModal(false)} maxWidth="sm" fullWidth PaperProps={{ sx: { bgcolor: '#1E293B' } }}>
        <form onSubmit={handleSubmit}>
          <DialogTitle sx={{ color: '#F8FAFC', fontWeight: 700 }}>Request Emergency Air Evacuation</DialogTitle>
          <DialogContent>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 1 }}>
              <TextField label="Patient Name" value={formData.patientName} onChange={(e) => setFormData({ ...formData, patientName: e.target.value })} required fullWidth />
              <Box sx={{ display: 'flex', gap: 2 }}>
                <TextField label="Age" type="number" value={formData.patientAge} onChange={(e) => setFormData({ ...formData, patientAge: parseInt(e.target.value) })} fullWidth />
                <TextField label="Gender" value={formData.patientGender} onChange={(e) => setFormData({ ...formData, patientGender: e.target.value })} fullWidth />
                <TextField label="Blood Group" value={formData.patientBloodGroup} onChange={(e) => setFormData({ ...formData, patientBloodGroup: e.target.value })} fullWidth />
              </Box>
              <TextField label="Emergency Contact Phone" value={formData.emergencyContact} onChange={(e) => setFormData({ ...formData, emergencyContact: e.target.value })} required fullWidth />
              <TextField label="Medical Condition / Diagnosis" value={formData.medicalCondition} onChange={(e) => setFormData({ ...formData, medicalCondition: e.target.value })} multiline rows={2} required fullWidth />
              <TextField label="Pickup Location / Coordinates" value={formData.pickupLocation} onChange={(e) => setFormData({ ...formData, pickupLocation: e.target.value })} required fullWidth />
              
              <TextField label="Destination Hospital" select value={formData.destinationHospitalId} onChange={(e) => setFormData({ ...formData, destinationHospitalId: e.target.value })} required fullWidth>
                {hospitals.map((h) => (
                  <MenuItem key={h.id} value={h.id}>{h.name} - {h.city}</MenuItem>
                ))}
              </TextField>

              <Box sx={{ display: 'flex', gap: 2 }}>
                <TextField label="Emergency Level" select value={formData.emergencyLevel} onChange={(e) => setFormData({ ...formData, emergencyLevel: e.target.value })} fullWidth>
                  {['CRITICAL', 'HIGH', 'MODERATE', 'LOW'].map((lvl) => (
                    <MenuItem key={lvl} value={lvl}>{lvl}</MenuItem>
                  ))}
                </TextField>
                <TextField label="Preferred Air Ambulance (Optional)" select value={formData.airAmbulanceId} onChange={(e) => setFormData({ ...formData, airAmbulanceId: e.target.value })} fullWidth>
                  <MenuItem value="">Auto-Assign Nearest Aircraft</MenuItem>
                  {ambulances.map((a) => (
                    <MenuItem key={a.id} value={a.id}>{a.registrationNumber} ({a.model})</MenuItem>
                  ))}
                </TextField>
              </Box>
            </Box>
          </DialogContent>
          <DialogActions sx={{ p: 2.5 }}>
            <Button onClick={() => setOpenModal(false)} sx={{ color: '#94A3B8' }}>Cancel</Button>
            <Button type="submit" variant="contained" color="error">Submit Emergency Request</Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default Bookings;
