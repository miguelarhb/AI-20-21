const User = require('../models/user-model')

const createUser = (req, res) => {
    const newUser = new User({
        username: req.body.username,
        password: req.body.password
    })
    newUser.save()
        .then(() => {
            console.log('User Created')
            res.status(201).send()
        })
        .catch((err) => {
            console.log('User Create Error: ' + err)
            res.status(400).send()
        })
}

const loginUser = (req, res) => {
    const query = {
        username: req.body.username,
        password: req.body.password
    }
    User.findOne(query)
        .then((result) => {
            if (result != null) {
                console.log('User Login')
                res.status(200).send(JSON.stringify(result))
            } else {
                console.log('Wrong Password')
                res.status(400).send()
            }
        })
        .catch((err) => {
            console.log('No User Login Error: ' + err)
            res.status(404).send()
        })
}

const addCaretaker = (req, res) => {
    const queryUser = { username: req.query.name }
    const queryCaretaker = { username: req.query.caretaker }
    User.findOne(queryUser)
        .then((userFound) => {
            User.findOne(queryCaretaker)
                .then((caretakerFound) => {
                    userFound.caretaker = caretakerFound.id
                    userFound.save()
                        .then(() => {
                            res.status(200).send()
                            console.log('Added Caretaker')
                        })
                        .catch((err) => {
                            console.log('User Save Error: ' + err)
                            res.status(400).send()
                        })
                })
                .catch((err) => {
                    console.log('No Caretaker Found Error: ' + err)
                    res.status(400).send()
                })
        })
        .catch((err) => {
            console.log('No User Found Error: ' + err)
            res.status(400).send()
        })
}

const deleteCaretaker = (req, res) => {
    const queryUser = { username: req.query.name }
    User.findOne(queryUser)
        .then((userFound) => {
            userFound.caretaker = ''
            userFound.save()
                .then(() => {
                    res.status(200).send()
                    console.log('Deleted Caretaker')
                })
                .catch((err) => {
                    console.log('User Save Error: ' + err)
                    res.status(400).send()
                })
        })
        .catch((err) => {
            console.log('No User Found Error: ' + err)
            res.status(400).send()
        })
}

const getCaretaker = (req, res) => {
    const queryUser = { username: req.query.name }
    User.findOne(queryUser)
        .then((userFound) => {
            User.findById(userFound.caretaker)
                .then((caretakerFound) => {
                    res.status(200).send(caretakerFound.username)
                    console.log('Sent Caretaker')
                })
                .catch((err) => {
                    console.log('No Caretaker Found Error: ' + err)
                    res.status(400).send()
                })
        })
        .catch((err) => {
            console.log('No User Found Error: ' + err)
            res.status(400).send()
        })
}

const addPatient = (req, res) => {
    const queryUser = { username: req.query.name }
    const queryPatient = { username: req.query.patient }
    User.findOne(queryUser)
        .then((userFound) => {
            User.findOne(queryPatient)
                .then((patientFound) => {
                    userFound.patients.push(patientFound.id)
                    userFound.save()
                        .then(() => {
                            res.status(200).send()
                            console.log('Added Patient')
                        })
                        .catch((err) => {
                            console.log('User Save Error: ' + err)
                            res.status(400).send()
                        })
                })
                .catch((err) => {
                    console.log('No Caretaker Found Error: ' + err)
                    res.status(400).send()
                })
        })
        .catch((err) => {
            console.log('No User Found Error: ' + err)
            res.status(400).send()
        })
}

const deletePatient = (req, res) => {
    const queryUser = { username: req.query.name }
    const queryPatient = { username: req.query.patient }
    User.findOne(queryUser)
        .then((userFound) => {
            userFound.patients.forEach((patientID) => {
                User.findById(patientID)
                    .then((patient) => {
                        if (patient.username == queryPatient) {
                            var filtered = userFound.patients.filter((value) => {
                                return value != patientID;
                            })
                            userFound.patients = filtered
                            userFound.save()
                                .then(() => {
                                    res.status(200).send()
                                    console.log('Deleted Patient')
                                })
                                .catch((err) => {
                                    console.log('User Save Error: ' + err)
                                    res.status(400).send()
                                })
                        }
                    })
                    .catch((err) => {
                        console.log('No User Found Error: ' + err)
                        res.status(400).send()
                    })
            })
        })
        .catch((err) => {
            console.log('No User Found Error: ' + err)
            res.status(400).send()
        })
}

const getAllPatient = (req, res) => {
    var list = []
    const queryUser = { username: req.query.name }
    User.findOne(queryUser)
        .then((userFound) => {
            userFound.patients.forEach((patientID) => {
                User.findById(patientID)
                    .then((patient) => {
                        list.push(patient.name)
                        if (userFound.patients.length == list.length) {
                            res.status(200).send(list)
                            console.log('Sent All Patient')
                        }
                    })
                    .catch((err) => {
                        console.log('No User Found Error: ' + err)
                        res.status(400).send()
                    })
            })
        })
        .catch((err) => {
            console.log('No User Found Error: ' + err)
            res.status(400).send()
        })
}

const addRequestPatient = (req, res) => {
    const queryUser = { username: req.query.name }
    const queryPatient = { username: req.query.patient }
    User.findOne(queryUser)
        .then((userFound) => {
            User.findOne(queryPatient)
                .then((patientFound) => {
                    userFound.requestPatient.push(patientFound.id)
                    userFound.save()
                        .then(() => {
                            res.status(200).send()
                            console.log('Add Request Patient')
                        })
                        .catch((err) => {
                            console.log('User Save Error: ' + err)
                            res.status(400).send()
                        })
                })
                .catch((err) => {
                    console.log('No Caretaker Found Error: ' + err)
                    res.status(400).send()
                })
        })
        .catch((err) => {
            console.log('No User Found Error: ' + err)
            res.status(400).send()
        })
}

const removeRequestPatient = (req, res) => {
    const queryUser = { username: req.query.name }
    const queryPatient = { username: req.query.patient }
    User.findOne(queryUser)
        .then((userFound) => {
            userFound.requestPatient.forEach((patientID) => {
                User.findById(patientID)
                    .then((patient) => {
                        if (patient.username == queryPatient) {
                            var filtered = userFound.requestPatient.filter((value) => {
                                return value != patientID;
                            })
                            userFound.requestPatient = filtered
                            userFound.save()
                                .then(() => {
                                    res.status(200).send()
                                    console.log('Deleted Request Patient')
                                })
                                .catch((err) => {
                                    console.log('User Save Error: ' + err)
                                    res.status(400).send()
                                })
                        }
                    })
                    .catch((err) => {
                        console.log('No User Found Error: ' + err)
                        res.status(400).send()
                    })
            })
        })
        .catch((err) => {
            console.log('No User Found Error: ' + err)
            res.status(400).send()
        })

}

const getAllRequestPatient = (req, res) => {
    var list = []
    const queryUser = { username: req.query.name }
    User.findOne(queryUser)
        .then((userFound) => {
            userFound.requestPatient.forEach((patientID) => {
                User.findById(patientID)
                    .then((patient) => {
                        list.push(patient.name)
                        if (userFound.requestPatient.length == list.length) {
                            res.status(200).send(list)
                            console.log('Sent All Request Patient')
                        }
                    })
                    .catch((err) => {
                        console.log('No User Found Error: ' + err)
                        res.status(400).send()
                    })
            })
        })
        .catch((err) => {
            console.log('No User Found Error: ' + err)
            res.status(400).send()
        })
}

const addRequestCaretaker = (req, res) => {
    const queryUser = { username: req.query.name }
    const queryCaretaker = { username: req.query.caretaker }
    User.findOne(queryUser)
        .then((userFound) => {
            User.findOne(queryCaretaker)
                .then((caretakerFound) => {
                    userFound.requestCaretaker.push(caretakerFound.id)
                    userFound.save()
                        .then(() => {
                            res.status(200).send()
                            console.log('Add Request Caretaker')
                        })
                        .catch((err) => {
                            console.log('User Save Error: ' + err)
                            res.status(400).send()
                        })
                })
                .catch((err) => {
                    console.log('No Caretaker Found Error: ' + err)
                    res.status(400).send()
                })
        })
        .catch((err) => {
            console.log('No User Found Error: ' + err)
            res.status(400).send()
        })
}

const removeRequestCaretaker = (req, res) => {
    const queryUser = { username: req.query.name }
    const queryCaretaker = { username: req.query.caretaker }
    User.findOne(queryUser)
        .then((userFound) => {
            userFound.requestCaretaker.forEach((caretakerID) => {
                User.findById(caretakerID)
                    .then((caretaker) => {
                        if (caretaker.username == queryCaretaker) {
                            var filtered = userFound.requestCaretaker.filter((value) => {
                                return value != caretakerID;
                            })
                            userFound.requestPatient = filtered
                            userFound.save()
                                .then(() => {
                                    res.status(200).send()
                                    console.log('Deleted Request Caretaker')
                                })
                                .catch((err) => {
                                    console.log('User Save Error: ' + err)
                                    res.status(400).send()
                                })
                        }
                    })
                    .catch((err) => {
                        console.log('No User Found Error: ' + err)
                        res.status(400).send()
                    })
            })
        })
        .catch((err) => {
            console.log('No User Found Error: ' + err)
            res.status(400).send()
        })

}

const getAllRequestCaretaker = (req, res) => {
    var list = []
    const queryUser = { username: req.query.name }
    User.findOne(queryUser)
        .then((userFound) => {
            userFound.requestCaretaker.forEach((caretakerID) => {
                User.findById(caretakerID)
                    .then((caretaker) => {
                        list.push(caretaker.name)
                        if (userFound.requestCaretaker.length == list.length) {
                            res.status(200).send(list)
                            console.log('Sent All Request Caretaker')
                        }
                    })
                    .catch((err) => {
                        console.log('No User Found Error: ' + err)
                        res.status(400).send()
                    })
            })
        })
        .catch((err) => {
            console.log('No User Found Error: ' + err)
            res.status(400).send()
        })
}

module.exports = {
    createUser,
    loginUser,
    addRequestCaretaker,
    removeRequestCaretaker,
    addRequestPatient,
    removeRequestPatient,
    deletePatient,
    addPatient,
    addCaretaker,
    deleteCaretaker,
    getCaretaker,
    getAllPatient,
    getAllRequestPatient,
    getAllRequestCaretaker
}