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
        .exec()
        .then((result) => {
            newItem.save((err, item) => {
                const list = result.items
                list.push(item.id)
                result.items = list
                result.save()
                    .catch((err) => {
                        res.status(400).send()
                        console.log('Item - No User error: ' + err)
                    })
                return new Promise(() => {})
            })
        })
        .catch((err) => {
            res.status(400).send()
            console.log('Item - No User error: ' + err)
        })
    res.status(200).send()
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

module.exports = {
    addItem,
    deleteItem,
    allItem,
    editItem
}