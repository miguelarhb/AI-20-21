const express = require('express')
const router = express.Router()

const prescriptionController = require('../controllers/prescription-controller')

// POST Adds prescription
router.post('user:/add', prescriptionController.addPrescription)

// DELETE Delete prescription
router.delete('user:/delete/name:', prescriptionController.deletePrescription)

// GET All prescription
router.get('user:/all', prescriptionController.allPrescription)

// PUT Edit prescription
router.put('user:/edit/name:', prescriptionController.editPrescription)

module.exports = router