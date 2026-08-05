import React, { useEffect, useState } from 'react';
import { 
  Box, Typography, Button, Card, CardContent, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, 
  Paper, TextField, Dialog, DialogTitle, DialogContent, DialogActions, Chip, IconButton, MenuItem 
} from '@mui/material';
import { Add, Search, Edit, Delete, FlightTakeoff, LocalGasStation } from '@mui/icons-material';
import { ambulanceService } from '../api/ambulance.service';
import { pilotService } from '../api/pilot.service';
import StatusChip from '../components/StatusChip';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';

const AirAmbulances = () => {
  const { user } = useAuth();
  const isAdmin = user?.roles?.includes('ROLE_ADMIN');

  const [ambulances, setAmbulances] = useState([]);
  const [pilots, setPilots] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [openModal, setOpenModal] = useState(false);
  const [selectedAmbulance, setSelectedAmbulance] = useState(null);

  const [formData, setFormData] = useState({
    registrationNumber: '',
    model: '',
    capacity: 4,
    currentLocation: '',
    fuelStatus: 'FULL',
    status: 'AVAILABLE',
    availability: true,
    pilotId: '',
  });

  const loadData = async () => {
    try {
      const ambData = searchQuery ? await ambulanceService.search(searchQuery) : await ambulanceService.getAll();
      const pilotData = await pilotService.getAll();
      setAmbulances(ambData);
      setPilots(pilotData);
    } catch (err) {
      console.error('Failed to load fleet data', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, [searchQuery]);

  const handleOpenCreate = () => {
    setSelectedAmbulance(null);
    setFormData({
      registrationNumber: '',
      model: '',
      capacity: 4,
      currentLocation: '',
      fuelStatus: 'FULL',
      status: 'AVAILABLE',
      availability: true,
      pilotId: '',
    });
    setOpenModal(true);
  };

  const handleOpenEdit = (amb) => {
    setSelectedAmbulance(amb);
    setFormData({
      registrationNumber: amb.registrationNumber,
      model: amb.model,
      capacity: amb.capacity,
      currentLocation: amb.currentLocation,
      fuelStatus: amb.fuelStatus,
      status: amb.status,
      availability: amb.availability,
      pilotId: amb.pilot?.id || '',
    });
    setOpenModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Delete this air ambulance from fleet?')) {
      await ambulanceService.delete(id);
      loadData();
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = { ...formData, pilotId: formData.pilotId ? parseInt(formData.pilotId) : null };
    if (selectedAmbulance) {
      await ambulanceService.update(selectedAmbulance.id, payload);
    } else {
      await ambulanceService.create(payload);
    }
    setOpenModal(false);
    loadData();
  };

  if (loading) return <LoadingSpinner message="Loading Fleet Aircraft Roster..." />;

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3, flexWrap: 'wrap', gap: 2 }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 800 }}>Air Ambulance Fleet</Typography>
          <Typography variant="body2" sx={{ color: '#94A3B8' }}>Medical Helicopters & Evacuation Jets Roster</Typography>
        </Box>
        {isAdmin && (
          <Button variant="contained" startIcon={<Add />} onClick={handleOpenCreate}>
            Register Aircraft
          </Button>
        )}
      </Box>

      <Box sx={{ mb: 3, maxWidth: 400 }}>
        <TextField
          placeholder="Search by model or location..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          fullWidth
          InputProps={{
            startAdornment: <Search sx={{ color: '#64748B', mr: 1 }} />,
          }}
        />
      </Box>

      <Card>
        <CardContent sx={{ p: 0 }}>
          <TableContainer component={Paper} sx={{ bgcolor: 'transparent', boxShadow: 'none' }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Registration #</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Aircraft Model</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Capacity</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Current Base</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Fuel Status</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Assigned Pilot</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Status</TableCell>
                  {isAdmin && <TableCell align="right" sx={{ color: '#64748B', fontWeight: 700 }}>Actions</TableCell>}
                </TableRow>
              </TableHead>
              <TableBody>
                {ambulances.map((amb) => (
                  <TableRow key={amb.id} hover>
                    <TableCell sx={{ fontWeight: 800, color: '#38BDF8' }}>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <FlightTakeoff sx={{ color: '#0284C7' }} />
                        {amb.registrationNumber}
                      </Box>
                    </TableCell>
                    <TableCell sx={{ color: '#F8FAFC' }}>{amb.model}</TableCell>
                    <TableCell sx={{ color: '#94A3B8' }}>{amb.capacity} Beds/Seats</TableCell>
                    <TableCell sx={{ color: '#94A3B8' }}>{amb.currentLocation}</TableCell>
                    <TableCell sx={{ color: '#10B981', fontWeight: 600 }}>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                        <LocalGasStation fontSize="small" />
                        {amb.fuelStatus}
                      </Box>
                    </TableCell>
                    <TableCell sx={{ color: '#F8FAFC' }}>{amb.pilot?.name || 'Unassigned'}</TableCell>
                    <TableCell><StatusChip status={amb.status} /></TableCell>
                    {isAdmin && (
                      <TableCell align="right">
                        <IconButton onClick={() => handleOpenEdit(amb)} color="primary"><Edit /></IconButton>
                        <IconButton onClick={() => handleDelete(amb.id)} color="error"><Delete /></IconButton>
                      </TableCell>
                    )}
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </CardContent>
      </Card>

      {/* Add / Edit Aircraft Modal */}
      <Dialog open={openModal} onClose={() => setOpenModal(false)} maxWidth="sm" fullWidth PaperProps={{ sx: { bgcolor: '#1E293B' } }}>
        <form onSubmit={handleSubmit}>
          <DialogTitle sx={{ color: '#F8FAFC', fontWeight: 700 }}>
            {selectedAmbulance ? 'Edit Air Ambulance' : 'Register New Air Ambulance'}
          </DialogTitle>
          <DialogContent>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 1 }}>
              <TextField label="Registration Tail #" value={formData.registrationNumber} onChange={(e) => setFormData({ ...formData, registrationNumber: e.target.value })} required fullWidth />
              <TextField label="Aircraft Model" value={formData.model} onChange={(e) => setFormData({ ...formData, model: e.target.value })} required fullWidth />
              <TextField label="Capacity (Patients/Medical Staff)" type="number" value={formData.capacity} onChange={(e) => setFormData({ ...formData, capacity: parseInt(e.target.value) })} required fullWidth />
              <TextField label="Current Base Location" value={formData.currentLocation} onChange={(e) => setFormData({ ...formData, currentLocation: e.target.value })} required fullWidth />
              
              <Box sx={{ display: 'flex', gap: 2 }}>
                <TextField label="Fuel Status" select value={formData.fuelStatus} onChange={(e) => setFormData({ ...formData, fuelStatus: e.target.value })} fullWidth>
                  {['FULL', 'HIGH', 'MEDIUM', 'LOW', 'CRITICAL'].map((f) => (
                    <MenuItem key={f} value={f}>{f}</MenuItem>
                  ))}
                </TextField>
                <TextField label="Status" select value={formData.status} onChange={(e) => setFormData({ ...formData, status: e.target.value })} fullWidth>
                  {['AVAILABLE', 'IN_SERVICE', 'MAINTENANCE', 'DECOMMISSIONED'].map((s) => (
                    <MenuItem key={s} value={s}>{s}</MenuItem>
                  ))}
                </TextField>
              </Box>

              <TextField label="Assign Captain / Pilot" select value={formData.pilotId} onChange={(e) => setFormData({ ...formData, pilotId: e.target.value })} fullWidth>
                <MenuItem value="">Unassigned</MenuItem>
                {pilots.map((p) => (
                  <MenuItem key={p.id} value={p.id}>{p.name} ({p.licenseNumber})</MenuItem>
                ))}
              </TextField>
            </Box>
          </DialogContent>
          <DialogActions sx={{ p: 2.5 }}>
            <Button onClick={() => setOpenModal(false)} sx={{ color: '#94A3B8' }}>Cancel</Button>
            <Button type="submit" variant="contained">Save Aircraft</Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default AirAmbulances;
