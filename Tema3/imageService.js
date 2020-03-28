const fs = require('fs')
const formidable = require('formidable')

async function handle(req, res) {
    var form = new formidable.IncomingForm();
    form.parse(req, function (err, fields, files) {
        res.write('File uploaded');
        console.log(files);
        res.end();
    });
}

module.exports = {
    handle: handle
}