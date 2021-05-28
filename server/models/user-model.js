const mongoose = require('mongoose')
const Item = require('./item-model')
const Prescription = require('./prescription-model')
const Schema = mongoose.Schema

const userSchema = new Schema({
    username: {
        type: String,
        unique: true,
        required: true
    },
    password: {
        type: String,
        required: true
    },
    items: [{
        type: String
    }],
    schedule: [{
        type: String
    }],
    patients: [String],
    requestPatient: [String],
    requestCaretaker: [String],
    caretaker: String
})

const User = mongoose.model('User', userSchema);
module.exports = User;