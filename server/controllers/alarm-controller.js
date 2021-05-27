const Alarm = require('../models/alarm-model')
const Prescription = require('../models/prescription-model')
const User = require('../models/user-model')

const addAlarm = (req, res) => {
    const newAlarm = new Alarm({
        time: req.body.time,
        taken: req.body.taken
    })
    const query = { username: req.query.user }
    const prescriptionName = req.query.name
    User.findOne(query)
        .then((userFound) => {
            userFound.schedule.forEach((scheduleID) => {
                Prescription.findById(scheduleID)
                    .then((prescription) => {
                        if (prescription.name == prescriptionName) {
                            newAlarm.save()
                                .then((alarm) => {
                                    prescription.alarms.push(alarm.id)
                                    prescription.save()
                                        .then(() => {
                                            res.status(200).send()
                                            console.log("Alarm Created")
                                        })
                                        .catch((err) => {
                                            res.status(400).send()
                                            console.log("Alarm ID not Saved in Prescription " + err)
                                        })

                                })
                                .catch((err) => {
                                    res.status(400).send()
                                    console.log("Alarm Not Saved" + err)
                                })
                        }
                    })
                    .catch((err) => {
                        res.status(400).send()
                        console.log("Prescription Not Found" + err)
                    })
            })
        })
        .catch((err) => {
            res.status(400).send()
            console.log("User Not Found" + err)
        })
}

const deleteAlarm = (req, res) => {
    const deletePrescriptionAlarmName = req.query.name
    const query = { username: req.query.user }
    User.findOne(query)
        .then((userFound) => {
            userFound.schedule.forEach((prescriptionID) => {
                Prescription.findById(prescriptionID)
                    .then((prescriptionFound) => {
                        if (prescriptionFound.name == deletePrescriptionAlarmName) {
                            prescriptionFound.alarms.forEach((alarmID) => {
                                Alarm.findByIdAndDelete(alarmID)
                                    .then(() => {
                                        var filtered = prescriptionFound.alarms.filter((value) => {
                                            return value != alarmID;
                                        })
                                        prescriptionFound.alarms = filtered
                                        prescriptionFound.save()
                                            .then(() => {
                                                res.status(200).send()
                                                console.log("Alarm deleted")
                                            })
                                            .catch((err) => {
                                                res.status(400).send()
                                                console.log('Delete Failure in Alarm Prescription error: ' + err)
                                            })
                                    })
                                    .catch((err) => {
                                        res.status(400).send()
                                        console.log('Delete Failure Alarm Not FOund error: ' + err)
                                    })

                            })
                        }
                    })
                    .catch((err) => {
                        res.status(400).send()
                        console.log('Alarm - Not Found error: ' + err)
                    })
            })
        })
        .catch((err) => {
            res.status(400).send()
            console.log('Alarm - No User error: ' + err)
        })
}

const allAlarm = (req, res) => {
    const query = { username: req.query.user }
    const prescriptionName = req.query.name
    const list = []
    User.findOne(query)
        .then((userFound) => {
            userFound.schedule.forEach((scheduleID) => {
                Prescription.findById(scheduleID)
                    .then((prescription) => {
                        if (prescription.name == prescriptionName) {
                            prescription.alarms.forEach((alarmID) => {
                                Alarm.findById(alarmID)
                                    .then((alarm) => {
                                        list.push(alarm)
                                        if (alarm.length == prescription.alarms.length) {
                                            res.status(200).send(list)
                                            console.log('Sent Alarms')
                                        }
                                    })
                                    .catch((err) => {
                                        res.status(400).send()
                                        console.log("Alarm Not Found" + err)
                                    })
                            })
                        }
                    })
                    .catch((err) => {
                        res.status(400).send()
                        console.log("Prescription Not Found" + err)
                    })
            })
        })
        .catch((err) => {
            res.status(400).send()
            console.log("User Not Found" + err)
        })
}

module.exports = {
    addAlarm,
    deleteAlarm,
    allAlarm
}