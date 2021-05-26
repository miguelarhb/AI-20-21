const mongoose = require('mongoose')
const Item = require('./item-model')
const Schema = mongoose.Schema

const prescriptionSchema = new Schema({
    name: {
        type: String,
        require: true
    },
    item: {
        type: String,
        require: true
    },
    quantity: {
        type: Number,
        require: true,
        default: 0
    },
    periodicity: {
        type: String,
        require: true
    },
    start: {
        type: Number,
        require: true
    },
    end: Number,
    notes: String
})

const Prescription = mongoose.model('Prescription', prescriptionSchema);
module.exports = Prescription;