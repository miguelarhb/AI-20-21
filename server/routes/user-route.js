const express = require('express')
const router = express.Router()

const userController = require('../controllers/user-controller')

// POST Create
router.post('/create', userController.createUser)

// POST Login
router.post('/login', userController.loginUser)

// POST
router.post('/addRequestCaretaker*', userController.addRequestCaretaker)

// POST
router.post('/addRequestPatient*', userController.addRequestPatient)

// POST
router.post('/addPatient*', userController.addPatient)

// POST
router.post('/addCaretaker*', userController.addCaretaker)

// DELETE
router.delete('/removeCaretaker*', userController.deleteCaretaker)

// DELETE
router.delete('/removeRequestCaretaker*', userController.removeRequestCaretaker)

// DELETE
router.delete('/removeRequestPatient*', userController.removeRequestPatient)

// DELETE
router.delete('/removePatient*', userController.deletePatient)

// GET
router.get('/getCaretaker*', userController.getCaretaker)

// GET
router.get('/getAllPatient*', userController.getAllPatient)

// GET
router.get('/getAllRequestPatient*', userController.getAllRequestPatient)

// GET
router.get('/getAllRequestCaretaker*', userController.getAllRequestCaretaker)


module.exports = router