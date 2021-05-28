const express = require('express')
const router = express.Router()

const prescriptionController = require('../controllers/prescription-controller')

// POST Adds prescription
router.post('/add*', prescriptionController.addPrescription)

// DELETE Delete prescription
router.delete('/delete*', prescriptionController.deletePrescription)

// GET All prescription
router.get('/all*', prescriptionController.allPrescription)

// PUT Edit prescription
router.put('/edit*', prescriptionController.editPrescription)

module.exports = router