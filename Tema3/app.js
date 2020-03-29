const http = require('http');
const url = require('url')

const indexService = require('./indexService')
const imageService = require('./imageService')

const config = {
    PORT: 8080
}

process.on('uncaughtException', function (err) {
    // handle the error safely
    console.log('Uncaught exception');
    console.error(err.stack);
    console.log(err);
});

const server = http.createServer(async (req, res) => {
    const reqUrl = url.parse(req.url, true);
    res.setHeader('Access-Control-Allow-Origin', '*');
    if (reqUrl.pathname.startsWith('/') && req.method == 'GET') {
        res.writeHead(200, 'OK', { 'Content-Type': 'text/html; charset=utf-8' });
        await indexService.handle(req, res);
    } else if (reqUrl.pathname.startsWith('/upload') && req.method == 'POST') {
        await imageService.handle(req, res);
    } else {
        res.writeHead(404, 'Resource Not Found', { 'Content-Type': 'text/html' });
        res.end('<!doctype html><html><head><title>404</title></head><body>404: Resource Not Found</body></html>');
    }
});

server.listen(config.PORT, () => {
    console.log(`Server is listening on port ${config.PORT}`);
});