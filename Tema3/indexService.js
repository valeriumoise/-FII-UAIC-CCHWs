const fs = require('fs')
const path = require('path')
const indexPath = path.join(__dirname, 'view', 'index.html');

async function handle(req, res) {
    const content = fs.readFileSync(indexPath).toString();
    res.end(content);
}

module.exports = {
    handle: handle
}