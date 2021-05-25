const mongoose = require('mongoose')

const Schema = mongoose.Schema

const itemSchema = new Schema({
    name: {
        type: String,
        required: true
    },
    quantity: {
        type: Number,
        required: true,
        default: 0
    },
    validity: String,
    barcode: String,
    notes: String
})

const Item = mongoose.model('Item', itemSchema);
module.exports = Item;