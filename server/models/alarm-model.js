const mongoose = require('mongoose')
const Schema = mongoose.Schema

const alarmSchema = new Schema({
    time: {
        type: String,
        require: true
    },
    taken: {
        type: Boolean,
        require: true
    }
})

const Alarm = mongoose.model('Alarm', alarmSchema);
module.exports = Alarm;