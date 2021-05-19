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
    newItem.save((item) => {
            const user = findUser(req)
            user.items.push(item.id)
        })
        .then(() => {
            res.status(200).send('Item added')
        })
        .catch((err) => {
            res.status(400).send('Item add error')
            console.log('Item error: ' + err)
        })
}

const deleteItem = (req, res) => {
    const deleteItemName = req.body.name
    const user = findUser(req)
    user.items.forEach((itemID) => {
        Item.findById(itemID)
            .then((result) => {
                if (result.name == deleteItemName) {
                    Item.findByIdAndDelete(itemID)
                        // TODO break
                }
            })
            .catch((err) => {
                res.status(400).send('Item add error')
                console.log('Item error: ' + err)
            })
    })
}

const allItem = (req, res) => {
    const user = findUser(req)
    var items = []
    user.items.forEach((itemID) => {
        Item.findById(itemID)
            .then((result) => {
                items.push(result)
            })
            .catch((err) => {
                res.status(400).send('Item add error')
                console.log('Item error: ' + err)
            })
    })
    res.status(200).send(items)
}

const editItem = (req, res) => {
    const editItemName = req.params.name
    const user = findUser(req)
    user.items.forEach((itemID) => {
        Item.findById(itemID)
            .then((result) => {
                if (result.name == editItemName) {
                    result.name = req.body.name
                    result.quantity = req.body.quantity
                    result.validity = req.body.validity
                    result.barcode = req.body.barcode
                    result.notes = req.body.notes
                        //TODO break
                }
            })
            .catch((err) => {
                res.status(400).send('Item add error')
                console.log('Item error: ' + err)
            })
    })
}

function findUser(req) {
    const query = { username: req.params.user }
    User.findOne(query)
        .then((result) => {
            return result
        })
        .catch((err) => {
            res.status(400).send('Item - No User error')
            console.log('Item - No User error: ' + err)
        })
}

module.exports = {
    addItem,
    deleteItem,
    allItem,
    editItem
}