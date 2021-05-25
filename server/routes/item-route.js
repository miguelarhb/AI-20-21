const express = require('express')
const router = express.Router()

const itemController = require('../controllers/item-controller')

// POST Add Item
router.post('/add*', itemController.addItem)

// DELETE Delete Item
router.delete('/delete*', itemController.deleteItem)

// GET All Item
router.get('/all*', itemController.allItem)

// PUT Edit Item
router.put('/edit*', itemController.editItem)

module.exports = router