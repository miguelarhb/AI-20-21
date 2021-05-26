const Item = require('../models/item-model')
const User = require('../models/user-model')

const addItem = (req, res) => {
    const newItem = new Item({
        name: req.body.name,
        quantity: req.body.quantity,
        validity: req.body.validity,
        barcode: req.body.barcode,
        notes: req.body.notes
    })

    const query = { username: req.query.user }
    User.findOne(query)
        .then((userFound) => {
            newItem.save((err, item) => {
                    const list = userFound.items
                    list.push(item.id)
                    userFound.items = list
                    userFound.save()
                        .catch((err) => {
                            res.status(400).send()
                            console.log('Item - Save Failure in User error: ' + err)
                        })
                    return new Promise(() => {})
                })
                .catch((err) => {
                    res.status(400).send()
                    console.log('Item - Save Failure error: ' + err)
                })
        })
        .catch((err) => {
            res.status(400).send()
            console.log('Item - No User error: ' + err)
        })
    res.status(200).send()
}

const deleteItem = (req, res) => {
    const deleteItemName = req.query.name
    const query = { username: req.query.user }
    var i = 0
    User.findOne(query)
        .then((userFound) => {
            userFound.items.forEach((itemID) => {
                Item.findById(itemID)
                    .then((itemFound) => {
                        if (itemFound.name == deleteItemName) {
                            Item.findByIdAndDelete(itemID)
                            var filtered = userFound.items[i].filter(function(value, i, arr) {
                                return value != itemID;
                            })
                            userFound.items = filtered
                            userFound.save()
                                .then(() => {
                                    res.status(200).send()
                                    console.log("Item deleted")
                                })
                                .catch((err) => {
                                    res.status(400).send()
                                    console.log('Item - Save Failure in User error: ' + err)
                                })
                        }
                    })
            })
        })
        .catch((err) => {
            res.status(400).send()
            console.log('Item - No User error: ' + err)
        })
}

const allItem = (req, res) => {
    var items = []
    const query = { username: req.query.user }
    User.findOne(query)
        .then((userFound) => {
            userFound.items.forEach(function(itemID) {
                Item.findById(itemID)
                    .then((itemFound) => {
                        items.push(itemFound)
                        if (items.length == userFound.items.length) {
                            res.status(200).send(items)
                        }
                    })
                    .catch((err) => {
                        res.status(400).send()
                        console.log('Item - Get All Failure - Item Not Found error: ' + err)
                    })
            })
        })
        .catch((err) => {
            res.status(400).send()
            console.log('Item - No User error: ' + err)
        })
}

const editItem = (req, res) => {
    const editItemName = req.query.name
    const query = { username: req.query.user }
    User.findOne(query)
        .then((userFound) => {
            userFound.items.forEach((itemID) => {
                Item.findById(itemID)
                    .then((itemFound) => {
                        if (itemFound.name == editItemName) {
                            itemFound.name = req.body.name
                            itemFound.quantity = req.body.quantity
                            itemFound.validity = req.body.validity
                            itemFound.barcode = req.body.barcode
                            itemFound.notes = req.body.notes
                            itemFound.save()
                                .then(() => {
                                    res.status(200).send()
                                    console.log("Item edited")
                                })
                                .catch((err) => {
                                    res.status(400).send()
                                    console.log('Item - Edit Failure error: ' + err)
                                })
                        }
                    })
                    .catch((err) => {
                        res.status(400).send()
                        console.log('Item - Edit Failure - Item Not Found error: ' + err)
                    })
            })
        })
        .catch((err) => {
            res.status(400).send()
            console.log('Item - No User error: ' + err)
        })
}

module.exports = {
    addItem,
    deleteItem,
    allItem,
    editItem
}