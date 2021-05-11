const express = require('express')
const router = express.Router()

const userController = require('../controllers/user-controller')

// POST Create
router.post('/create', userController.createUser)

// POST Login
router.post('/login', userController.loginUser)

module.exports = router