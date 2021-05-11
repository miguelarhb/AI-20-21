const express = require('express')
const router = express.Router()

const prescriptionController = require('../controllers/prescription-controller')

// POST Adds prescription
router.post('/add/', prescriptionController.addPrescription)

// DELETE Delete prescription
router.delete('/delete/', prescriptionController.deletePrescription)

// PUT Edit prescription
router.put('/update/', prescriptionController.editPrescription)

// GET All prescription
router.get('/all', prescriptionController.allPrescription)

// GET prescription
router.get('/check', prescriptionController.checkPrescription)

module.exports = router