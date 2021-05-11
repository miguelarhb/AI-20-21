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

module.exports = {
    createUser,
    loginUser
}