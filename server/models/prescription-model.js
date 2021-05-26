const mongoose = require('mongoose')
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
    alarms: [{
        type: String
    }],
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
        type: String,
        require: true
    },
    end: String,
    notes: String
})

const Prescription = mongoose.model('Prescription', prescriptionSchema);
module.exports = Prescription;