const express = require('express');
const http = require('http');
const mongoose = require('mongoose');
const https = require('https');
const path = require('path');
const fs = require('fs');

//Express App
const app = express();

//Server Port
const PORT = process.env.PORT || 3000;

//MongoDB database
const dbURL = 'mongodb://localhost:27017/shopISTdb';

//Logger - Middleware
const logger = require('./middleware/logger');
app.use(logger);

//Body Parser - Middleware
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

//Sends message to /
app.use('/user', require('./routes/user-route'));
app.use('/prescription', require('./routes/prescription-route'));
app.use('/item', require('./routes/item-route'));

const server = http.createServer(app)

//MongoDB Connect & Run SSL Server
const db = mongoose.connect(dbURL, {
        useNewUrlParser: true,
        useUnifiedTopology: true,
        useCreateIndex: true,
        useFindAndModify: false
    })
    .then((result) => {
        console.log('Connection to DB Successful');
        //Run SSL Server
        server.listen(PORT, (err) => {
            if (err)
                console.error('Something went wrong', err);
            else
                console.log(`Secure Server listening on port ${PORT}`);
        });
    })
    .catch((err) => console.error('Connection to DB Failed: ', err));