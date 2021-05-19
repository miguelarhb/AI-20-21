const Prescription = require('../models/prescription-model')
const User = require('../models/user-model')
const Item = require('../models/item-model')

const addPrescription = (req, res) => {
    const user = findUser(req)
    const newPrescription = new Prescription({
        name: req.body.name,
        item: getItemID(req),
        quantity: req.body.quantity,
        periodicity: req.body.periodicity,
        start: req.body.start,
        end: req.body.end,
        notes: req.body.notes
    })
    newPrescription.save((prescription) => {
            user.schedule.push(prescription.id)
        })
        .then(() => {
            res.status(200).send('Prescription added')
        })
        .catch((err) => {
            res.status(400).send('Prescription add error')
            console.log('Prescription add error: ' + err)
        })
}

const deletePrescription = (req, res) => {
    const deletePrescriptionName = req.body.name
    const user = findUser(req)
    user.schedule.forEach((prescriptionID) => {
        Prescription.findById(prescriptionID)
            .then((result) => {
                if (result.name == deletePrescriptionName) {
                    Prescription.findByIdAndDelete(prescriptionID)
                        //TODO break
                }
            })
            .catch((err) => {
                res.status(400).send('Prescription delete error')
                console.log('Prescription delete error: ' + err)
            })
    })
}

const editPrescription = (req, res) => {
    const editPrescriptionName = req.params.name
    const user = findUser(req)
    user.schedule.forEach((prescriptionID) => {
        Prescription.findById(prescriptionID)
            .then((result) => {
                if (result.name == editPrescriptionName) {
                    result.name = req.body.name
                    result.item = getItemID(req)
                    result.quantity = req.body.quantity
                    result.periodicity = req.body.periodicity
                    result.start = req.body.start
                    result.end = req.body.end
                    result.notes = req.body.notes
                        //TODO break
                }
            })
            .catch((err) => {
                res.status(400).send('Prescription edit error')
                console.log('Prescription edit error: ' + err)
            })
    })
}

const allPrescription = (req, res) => {
    const user = findUser(req)
    var prescriptions = []
    user.schedule.forEach((prescriptionID) => {
        Prescription.findById(prescriptionID)
            .then((result) => {
                prescriptions.push(result)
            })
            .catch((err) => {
                res.status(400).send('Prescription all error')
                console.log('Prescription all error: ' + err)
            })
    })
    res.status(200).send(prescriptions)
}

function getItemID(req) {
    const user = findUser(req)
    user.items.forEach((itemID) => {
        Item.findById(itemID)
            .then((result) => {
                if (result.name == req.body.name) {
                    return result.id
                }
            })
            .catch((err) => {
                res.status(400).send('No Item error')
                console.log('No Item error: ' + err)
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
    addPrescription,
    deletePrescription,
    editPrescription,
    allPrescription,
}