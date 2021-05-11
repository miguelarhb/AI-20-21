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
        type: mongoose.Types.ObjectId,
        ref: Item
    }],
    schedule: [{
        type: mongoose.Types.ObjectId,
        ref: Prescription
    }],
    patient: [String],
    caretaker: String
})

const User = mongoose.model('User', userSchema);
module.exports = User;