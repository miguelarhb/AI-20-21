const express = require('express')
const router = express.Router()

const itemController = require('../controllers/item-controller')

// POST Adds Item
router.post('/add', itemController.addItem)

// DELETE Delete Item
router.delete('/delete', itemController.deleteItem)

// GET All Item
router.get('/all', itemController.allItem)

// GET Item
router.get('/check', itemController.checkItem)

// PUT Edit Item
router.put('/update', itemController.editItem)

module.exports = router