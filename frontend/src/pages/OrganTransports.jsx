import React, { useEffect, useState } from 'react';
import { 
  Box, Typography, Button, Card, CardContent, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, 
  Paper, Dialog, DialogTitle, DialogContent, DialogActions, TextField, MenuItem 
} from '@mui/material';
import { Add, LocalShipping, LocalHospital } from '@mui/icons-material';
import { organService } from '../api/organ.service';
import { hospitalService } from '../api/hospital.service';
import { ambulanceService } from '../api/ambulance.service';
import StatusChip from '../components/StatusChip';
import LoadingSpinner from '../components/LoadingSpinner';

const OrganTransports = () => {
  const [transports, setTransports] = useState([]);
  const [hospitals, setHospitals] = useState([]);
  const [ambulances, setAmbulances] = useState([]);
  const [loading, setLoading] = useState(true);
  const [openModal, setOpenModal] = useState(false);

  const [formData, setFormData] = useState({
    organType: 'Heart',
    sourceHospitalId: '',
    destinationHospitalId: '',
    airAmbulanceId: '',
    priority: 'CRITICAL',
    estimatedTimeOfArrival: '45 Mins',
    status: 'SCHEDULED',
    notes: '',
  });

  const loadData = async () => {
    try {
      const tData = await organService.getAll();
      const hData = await hospitalService.getAll();
      const aData = await ambulanceService.getAvailable();
      setTransports(tData);
      setHospitals(hData);
      setAmbulances(aData);
    } catch (err) {
      console.error('Failed to load organ transport data', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = {
      ...formData,
      sourceHospitalId: parseInt(formData.sourceHospitalId),
      destinationHospitalId: parseInt(formData.destinationHospitalId),
      airAmbulanceId: formData.airAmbulanceId ? parseInt(formData.airAmbulanceId) : null,
    };
    await organService.create(payload);
    setOpenModal(false);
    loadData();
  };

  if (loading) return <LoadingSpinner message="Loading Organ Transport Logistics..." />;

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3, flexWrap: 'wrap', gap: 2 }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 800 }}>Organ Transport Logistics</Typography>
          <Typography variant="body2" sx={{ color: '#94A3B8' }}>Hospital-to-Hospital Critical Organ Flight Missions</Typography>
        </Box>
        <Button variant="contained" startIcon={<Add />} onClick={() => setOpenModal(true)}>
          New Organ Flight Request
        </Button>
      </Box>

      <Card>
        <CardContent sx={{ p: 0 }}>
          <TableContainer component={Paper} sx={{ bgcolor: 'transparent', boxShadow: 'none' }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Organ Type</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Source Hospital</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Destination Hospital</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Aircraft</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Priority</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>ETA</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Status</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {transports.map((t) => (
                  <TableRow key={t.id} hover>
                    <TableCell sx={{ fontWeight: 800, color: '#F59E0B' }}>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <LocalShipping fontSize="small" />
                        {t.organType}
                      </Box>
                    </TableCell>
                    <TableCell sx={{ color: '#94A3B8' }}>{t.sourceHospital?.name}</TableCell>
                    <TableCell sx={{ color: '#94A3B8' }}>{t.destinationHospital?.name}</TableCell>
                    <TableCell sx={{ color: '#38BDF8' }}>{t.airAmbulance?.registrationNumber || 'Unassigned'}</TableCell>
                    <TableCell><StatusChip status={t.priority} /></TableCell>
                    <TableCell sx={{ color: '#10B981', fontWeight: 700 }}>{t.estimatedTimeOfArrival}</TableCell>
                    <TableCell><StatusChip status={t.status} /></TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </CardContent>
      </Card>

      {/* Modal */}
      <Dialog open={openModal} onClose={() => setOpenModal(false)} maxWidth="sm" fullWidth PaperProps={{ sx: { bgcolor: '#1E293B' } }}>
        <form onSubmit={handleSubmit}>
          <DialogTitle sx={{ color: '#F8FAFC', fontWeight: 700 }}>Schedule Organ Transport Flight</DialogTitle>
          <DialogContent>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 1 }}>
              <TextField label="Organ Type (e.g. Heart, Liver, Kidney)" value={formData.organType} onChange={(e) => setFormData({ ...formData, organType: e.target.value })} required fullWidth />
              <TextField label="Source Hospital" select value={formData.sourceHospitalId} onChange={(e) => setFormData({ ...formData, sourceHospitalId: e.target.value })} required fullWidth>
                {hospitals.map((h) => (
                  <MenuItem key={h.id} value={h.id}>{h.name}</MenuItem>
                ))}
              </TextField>
              <TextField label="Destination Hospital" select value={formData.destinationHospitalId} onChange={(e) => setFormData({ ...formData, destinationHospitalId: e.target.value })} required fullWidth>
                {hospitals.map((h) => (
                  <MenuItem key={h.id} value={h.id}>{h.name}</MenuItem>
                ))}
              </TextField>
              <TextField label="Assigned Air Ambulance" select value={formData.airAmbulanceId} onChange={(e) => setFormData({ ...formData, airAmbulanceId: e.target.value })} fullWidth>
                <MenuItem value="">Unassigned</MenuItem>
                {ambulances.map((a) => (
                  <MenuItem key={a.id} value={a.id}>{a.registrationNumber} ({a.model})</MenuItem>
                ))}
              </TextField>
              <Box sx={{ display: 'flex', gap: 2 }}>
                <TextField label="Priority" select value={formData.priority} onChange={(e) => setFormData({ ...formData, priority: e.target.value })} fullWidth>
                  {['CRITICAL', 'HIGH', 'MEDIUM'].map((p) => (
                    <MenuItem key={p} value={p}>{p}</MenuItem>
                  ))}
                </TextField>
                <TextField label="ETA" value={formData.estimatedTimeOfArrival} onChange={(e) => setFormData({ ...formData, estimatedTimeOfArrival: e.target.value })} fullWidth />
              </Box>
            </Box>
          </DialogContent>
          <DialogActions sx={{ p: 2.5 }}>
            <Button onClick={() => setOpenModal(false)} sx={{ color: '#94A3B8' }}>Cancel</Button>
            <Button type="submit" variant="contained">Schedule Flight</Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default OrganTransports;
