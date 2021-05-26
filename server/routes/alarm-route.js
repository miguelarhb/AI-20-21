const express = require('express')
const router = express.Router()

const alarmController = require('../controllers/alarm-controller')

// POST Add Alarm
router.post('/add*', alarmController.addAlarm)

// DELETE Delete Alarm
router.delete('/delete*', alarmController.deleteAlarm)

// GET All Alarm
router.get('/all*', alarmController.allAlarm)

module.exports = router