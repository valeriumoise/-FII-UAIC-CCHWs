const mongoose = require('mongoose');
const config = require('../config/config');

try {
    mongoose.connect(config.MONGO_CONNECTION_STRING, {useNewUrlParser: true});
    module.exports = mongoose
} catch (errror) {
    console.log(error);
}



const logDebug = true;

mongoose.set('debug', logDebug);