const express = require('express')
const router = express.Router()

const itemController = require('../controllers/item-controller')

// POST Add Item
router.post('user:/add', itemController.addItem)

// DELETE Delete Item
router.delete('user:/delete/name:', itemController.deleteItem)

// GET All Item
router.get('user:/all', itemController.allItem)

// PUT Edit Item
router.put('user:/edit/name:', itemController.editItem)

module.exports = router