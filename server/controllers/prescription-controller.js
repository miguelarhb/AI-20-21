const Prescription = require('../models/prescription-model')
const User = require('../models/user-model')

const addPrescription = (req, res) => {
    const newPrescription = new Prescription({
        name: req.body.name,
        item: req.body.item,
        quantity: req.body.quantity,
        periodicity: req.body.periodicity,
        start: req.body.start,
        end: req.body.end,
        notes: req.body.notes,
        alarms: req.body.alarms
    })

    const query = { username: req.query.user }
    User.findOne(query)
        .then((userFound) => {
            newPrescription.save()
                .then((prescription) => {
                    const list = userFound.schedule
                    list.push(prescription.id)
                    userFound.schedule = list
                    userFound.save()
                        .then(() => {
                            res.status(200).send()
                            console.log('Prescription Saved')
                        })
                        .catch((err) => {
                            res.status(400).send()
                            console.log('Prescription - Save Failure in User error: ' + err)
                        })
                })
                .catch((err) => {
                    res.status(400).send()
                    console.log('Prescription - Save Failure error: ' + err)
                })
        })
        .catch((err) => {
            res.status(400).send()
            console.log('Prescription - No User error: ' + err)
        })
}

const deletePrescription = (req, res) => {
    const deletePrescriptionName = req.query.name
    const query = { username: req.query.user }
    User.findOne(query)
        .then((userFound) => {
            userFound.schedule.forEach((prescriptionID) => {
                Prescription.findById(prescriptionID)
                    .then((prescriptionFound) => {
                        if (prescriptionFound.name == deletePrescriptionName) {
                            Prescription.findByIdAndDelete(prescriptionID)
                                .then(() => {
                                    var filtered = userFound.schedule.filter((value) => {
                                        return value != prescriptionID;
                                    })
                                    userFound.schedule = filtered
                                    userFound.save()
                                        .then(() => {
                                            res.status(200).send()
                                            console.log("Prescription deleted")
                                        })
                                        .catch((err) => {
                                            res.status(400).send()
                                            console.log('Prescription - Save Failure in User error: ' + err)
                                        })
                                })
                                .catch((err) => {
                                    res.status(400).send()
                                    console.log('Prescription - Not Deleted error: ' + err)
                                })
                        }
                    })
                    .catch((err) => {
                        res.status(400).send()
                        console.log('Prescription - Not Found error: ' + err)
                    })
            })
        })
        .catch((err) => {
            res.status(400).send()
            console.log('Prescription - No User error: ' + err)
        })
}

const editPrescription = (req, res) => {
    const editPrescriptionName = req.query.name
    const query = { username: req.query.user }
    User.findOne(query)
        .then((userFound) => {
            userFound.schedule.forEach((itemID) => {
                Prescription.findById(itemID)
                    .then((prescriptionFound) => {
                        if (prescriptionFound.name == editPrescriptionName) {
                            prescriptionFound.name = req.body.name
                            prescriptionFound.item = req.body.item
                            prescriptionFound.quantity = req.body.quantity
                            prescriptionFound.periodicity = req.body.periodicity
                            prescriptionFound.start = req.body.start
                            prescriptionFound.end = req.body.end
                            prescriptionFound.notes = req.body.notes
                            prescriptionFound.alarms = req.body.alarms
                            prescriptionFound.save()
                                .then(() => {
                                    res.status(200).send()
                                    console.log("Prescription edited")
                                })
                                .catch((err) => {
                                    res.status(400).send()
                                    console.log('Prescription Edit Failure error: ' + err)
                                })
                        }
                    })
                    .catch((err) => {
                        res.status(400).send()
                        console.log('Prescription Not Found error: ' + err)
                    })
            })
        })
        .catch((err) => {
            res.status(400).send()
            console.log('Prescription - No User error: ' + err)
        })
}

const allPrescription = (req, res) => {
    var prescriptions = []
    const query = { username: req.query.user }
    User.findOne(query)
        .then((userFound) => {
            userFound.schedule.forEach(function(prescriptionID) {
                Prescription.findById(prescriptionID)
                    .then((prescriptionFound) => {
                        prescriptions.push(prescriptionFound)
                        if (prescriptions.length == userFound.schedule.length) {
                            res.status(200).send(prescriptions)
                            console.log('Sent Prescriptions')
                        }
                    })
                    .catch((err) => {
                        res.status(400).send()
                        console.log('Prescription Not Found error: ' + err)
                    })
            })
        })
        .catch((err) => {
            res.status(400).send()
            console.log('Prescription - No User error: ' + err)
        })
}

module.exports = {
    addPrescription,
    deletePrescription,
    editPrescription,
    allPrescription,
}