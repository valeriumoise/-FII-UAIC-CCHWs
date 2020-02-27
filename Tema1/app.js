const http = require('http');
const url = require('url')
const homeService = require('./homeService')
const logService = require('./logService')
const metricsService = require('./metricsService')

const hostname = '127.0.0.1';
const port = 8080;

const server = http.createServer((req, res) => {
  req.startTime = Date.now();
  const reqUrl = url.parse(req.url, true);
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.writeHead(200, 'OK', { 'Content-Type': 'text/html; charset=utf-8' });
  if (reqUrl.pathname.startsWith('/metrics')) {
    metricsService.handle(req,res);
  } else if (reqUrl.pathname.startsWith('/log')) {
    logService.handle(req,res);
  } else if (reqUrl.pathname.startsWith('/')) {
    homeService.handle(req, res);
  } else {
    res.writeHead(404, 'Resource Not Found', { 'Content-Type': 'text/html' });
    res.end('<!doctype html><html><head><title>404</title></head><body>404: Resource Not Found</body></html>');
  }
});

server.listen(port, hostname, () => {
  console.log(`[${new Date().toISOString()}] Server started running at http://${hostname}:${port}/`);
});