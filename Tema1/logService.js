const fs = require('fs')
const path = require('path')
const reader = require('./reader')
const logger = require('./logger')
const metricsService = require('./metricsService')

const logsPath = path.join(__dirname, 'logs', 'log');

function handle(req, res) {
  reader.view('log', {}, res);
  
  var readlines = require('n-readlines');
  var liner = new readlines(logsPath);
  while (next = liner.next()){
    res.write(`${next.toString()} <br>`);
  }
  
  res.end();
  logger.log(req, "logs");
  metricsService.register(Date.now() - req.startTime, 'logs');
}

module.exports = {
  handle: handle
}