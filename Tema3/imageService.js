const {callAnnotateImage} = require("./AppVision");
const formidable = require('formidable');
const crypto = require('crypto');
const PassThrough = require('stream').PassThrough;


async function handle(req, res) {
    let form = new formidable.IncomingForm();
    form.parse(req, function (err, fields, files) {
        callAnnotateImage(files.image.path, res,files.image.name);
    });
}

module.exports = {
    handle: handle
};