import React, { useEffect, useState } from 'react';
import { 
  Box, Typography, Button, Card, CardContent, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, 
  Paper, TextField, Dialog, DialogTitle, DialogContent, DialogActions, Chip, IconButton 
} from '@mui/material';
import { Add, Search, Edit, Delete, LocalHospital } from '@mui/icons-material';
import { hospitalService } from '../api/hospital.service';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';

const Hospitals = () => {
  const { user } = useAuth();
  const isAdmin = user?.roles?.includes('ROLE_ADMIN');

  const [hospitals, setHospitals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [openModal, setOpenModal] = useState(false);
  const [selectedHospital, setSelectedHospital] = useState(null);

  const [formData, setFormData] = useState({
    name: '',
    address: '',
    city: '',
    state: '',
    contactNumber: '',
    emergencyContact: '',
    hasHelipad: true,
    totalICUBeds: 20,
    availableICUBeds: 5,
  });

  const loadHospitals = async () => {
    try {
      const data = searchQuery ? await hospitalService.search(searchQuery) : await hospitalService.getAll();
      setHospitals(data);
    } catch (err) {
      console.error('Failed to load hospitals', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadHospitals();
  }, [searchQuery]);

  const handleOpenCreate = () => {
    setSelectedHospital(null);
    setFormData({
      name: '',
      address: '',
      city: '',
      state: '',
      contactNumber: '',
      emergencyContact: '',
      hasHelipad: true,
      totalICUBeds: 20,
      availableICUBeds: 5,
    });
    setOpenModal(true);
  };

  const handleOpenEdit = (h) => {
    setSelectedHospital(h);
    setFormData({
      name: h.name,
      address: h.address,
      city: h.city,
      state: h.state || '',
      contactNumber: h.contactNumber,
      emergencyContact: h.emergencyContact,
      hasHelipad: h.hasHelipad,
      totalICUBeds: h.totalICUBeds,
      availableICUBeds: h.availableICUBeds,
    });
    setOpenModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this hospital?')) {
      await hospitalService.delete(id);
      loadHospitals();
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (selectedHospital) {
      await hospitalService.update(selectedHospital.id, formData);
    } else {
      await hospitalService.create(formData);
    }
    setOpenModal(false);
    loadHospitals();
  };

  if (loading) return <LoadingSpinner message="Loading Hospital Directory..." />;

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3, flexWrap: 'wrap', gap: 2 }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 800 }}>Partner Hospitals Network</Typography>
          <Typography variant="body2" sx={{ color: '#94A3B8' }}>Search & Manage Certified Helipad Trauma Centers</Typography>
        </Box>
        {isAdmin && (
          <Button variant="contained" startIcon={<Add />} onClick={handleOpenCreate}>
            Add Hospital
          </Button>
        )}
      </Box>

      {/* Search Bar */}
      <Box sx={{ mb: 3, maxWidth: 400 }}>
        <TextField
          placeholder="Search by hospital name or city..."
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
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Hospital Name</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Location</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Emergency Contact</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Helipad</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>ICU Beds (Avail/Total)</TableCell>
                  {isAdmin && <TableCell align="right" sx={{ color: '#64748B', fontWeight: 700 }}>Actions</TableCell>}
                </TableRow>
              </TableHead>
              <TableBody>
                {hospitals.map((h) => (
                  <TableRow key={h.id} hover>
                    <TableCell sx={{ fontWeight: 700, color: '#F8FAFC' }}>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <LocalHospital sx={{ color: '#0284C7' }} />
                        {h.name}
                      </Box>
                    </TableCell>
                    <TableCell sx={{ color: '#94A3B8' }}>{h.city}, {h.state}</TableCell>
                    <TableCell sx={{ color: '#EF4444', fontWeight: 600 }}>{h.emergencyContact}</TableCell>
                    <TableCell>
                      <Chip 
                        label={h.hasHelipad ? 'Yes' : 'No'} 
                        color={h.hasHelipad ? 'success' : 'default'} 
                        size="small" 
                        sx={{ fontWeight: 700 }}
                      />
                    </TableCell>
                    <TableCell sx={{ color: '#F8FAFC' }}>{h.availableICUBeds} / {h.totalICUBeds}</TableCell>
                    {isAdmin && (
                      <TableCell align="right">
                        <IconButton onClick={() => handleOpenEdit(h)} color="primary"><Edit /></IconButton>
                        <IconButton onClick={() => handleDelete(h.id)} color="error"><Delete /></IconButton>
                      </TableCell>
                    )}
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </CardContent>
      </Card>

      {/* Add / Edit Hospital Modal */}
      <Dialog open={openModal} onClose={() => setOpenModal(false)} maxWidth="sm" fullWidth PaperProps={{ sx: { bgcolor: '#1E293B' } }}>
        <form onSubmit={handleSubmit}>
          <DialogTitle sx={{ color: '#F8FAFC', fontWeight: 700 }}>
            {selectedHospital ? 'Edit Hospital' : 'Register New Hospital'}
          </DialogTitle>
          <DialogContent>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 1 }}>
              <TextField label="Hospital Name" value={formData.name} onChange={(e) => setFormData({ ...formData, name: e.target.value })} required fullWidth />
              <TextField label="Address" value={formData.address} onChange={(e) => setFormData({ ...formData, address: e.target.value })} required fullWidth />
              <Box sx={{ display: 'flex', gap: 2 }}>
                <TextField label="City" value={formData.city} onChange={(e) => setFormData({ ...formData, city: e.target.value })} required fullWidth />
                <TextField label="State" value={formData.state} onChange={(e) => setFormData({ ...formData, state: e.target.value })} fullWidth />
              </Box>
              <TextField label="Contact Phone" value={formData.contactNumber} onChange={(e) => setFormData({ ...formData, contactNumber: e.target.value })} required fullWidth />
              <TextField label="Emergency Line" value={formData.emergencyContact} onChange={(e) => setFormData({ ...formData, emergencyContact: e.target.value })} required fullWidth />
              <Box sx={{ display: 'flex', gap: 2 }}>
                <TextField label="Total ICU Beds" type="number" value={formData.totalICUBeds} onChange={(e) => setFormData({ ...formData, totalICUBeds: parseInt(e.target.value) })} fullWidth />
                <TextField label="Available ICU Beds" type="number" value={formData.availableICUBeds} onChange={(e) => setFormData({ ...formData, availableICUBeds: parseInt(e.target.value) })} fullWidth />
              </Box>
            </Box>
          </DialogContent>
          <DialogActions sx={{ p: 2.5 }}>
            <Button onClick={() => setOpenModal(false)} sx={{ color: '#94A3B8' }}>Cancel</Button>
            <Button type="submit" variant="contained">Save Hospital</Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default Hospitals;
