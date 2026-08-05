import React, { useEffect, useState } from 'react';
import { 
  Box, Typography, Button, Card, CardContent, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, 
  Paper, TextField, Dialog, DialogTitle, DialogContent, DialogActions, Chip 
} from '@mui/material';
import { Add, Search, Sick } from '@mui/icons-material';
import { patientService } from '../api/patient.service';
import LoadingSpinner from '../components/LoadingSpinner';

const Patients = () => {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [openModal, setOpenModal] = useState(false);

  const [formData, setFormData] = useState({
    name: '',
    age: 30,
    gender: 'Male',
    bloodGroup: 'O+',
    emergencyContact: '',
    medicalCondition: '',
  });

  const loadPatients = async () => {
    try {
      const data = searchQuery ? await patientService.search(searchQuery) : await patientService.getAll();
      setPatients(data);
    } catch (err) {
      console.error('Failed to load patients', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPatients();
  }, [searchQuery]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    await patientService.create(formData);
    setOpenModal(false);
    loadPatients();
  };

  if (loading) return <LoadingSpinner message="Loading Patient Records..." />;

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3, flexWrap: 'wrap', gap: 2 }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 800 }}>Medical Patients Directory</Typography>
          <Typography variant="body2" sx={{ color: '#94A3B8' }}>Registered Patient Profiles & Medical History</Typography>
        </Box>
        <Button variant="contained" startIcon={<Add />} onClick={() => setOpenModal(true)}>
          Register Patient
        </Button>
      </Box>

      <Box sx={{ mb: 3, maxWidth: 400 }}>
        <TextField
          placeholder="Search patient by name..."
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
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Patient Name</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Age / Gender</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Blood Group</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Emergency Contact</TableCell>
                  <TableCell sx={{ color: '#64748B', fontWeight: 700 }}>Medical Condition</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {patients.map((p) => (
                  <TableRow key={p.id} hover>
                    <TableCell sx={{ fontWeight: 700, color: '#F8FAFC' }}>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <Sick sx={{ color: '#0284C7' }} />
                        {p.name}
                      </Box>
                    </TableCell>
                    <TableCell sx={{ color: '#94A3B8' }}>{p.age} Yrs / {p.gender}</TableCell>
                    <TableCell>
                      <Chip label={p.bloodGroup} color="error" size="small" sx={{ fontWeight: 800 }} />
                    </TableCell>
                    <TableCell sx={{ color: '#EF4444', fontWeight: 600 }}>{p.emergencyContact}</TableCell>
                    <TableCell sx={{ color: '#94A3B8' }}>{p.medicalCondition}</TableCell>
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
          <DialogTitle sx={{ color: '#F8FAFC', fontWeight: 700 }}>Register New Patient</DialogTitle>
          <DialogContent>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 1 }}>
              <TextField label="Full Name" value={formData.name} onChange={(e) => setFormData({ ...formData, name: e.target.value })} required fullWidth />
              <Box sx={{ display: 'flex', gap: 2 }}>
                <TextField label="Age" type="number" value={formData.age} onChange={(e) => setFormData({ ...formData, age: parseInt(e.target.value) })} required fullWidth />
                <TextField label="Gender" value={formData.gender} onChange={(e) => setFormData({ ...formData, gender: e.target.value })} required fullWidth />
                <TextField label="Blood Group" value={formData.bloodGroup} onChange={(e) => setFormData({ ...formData, bloodGroup: e.target.value })} required fullWidth />
              </Box>
              <TextField label="Emergency Contact Phone" value={formData.emergencyContact} onChange={(e) => setFormData({ ...formData, emergencyContact: e.target.value })} required fullWidth />
              <TextField label="Medical Condition" value={formData.medicalCondition} onChange={(e) => setFormData({ ...formData, medicalCondition: e.target.value })} multiline rows={3} fullWidth />
            </Box>
          </DialogContent>
          <DialogActions sx={{ p: 2.5 }}>
            <Button onClick={() => setOpenModal(false)} sx={{ color: '#94A3B8' }}>Cancel</Button>
            <Button type="submit" variant="contained">Save Patient</Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default Patients;
