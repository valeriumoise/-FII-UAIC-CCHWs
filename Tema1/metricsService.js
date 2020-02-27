const fs = require('fs')
const path = require('path')
const reader = require('./reader')
const logger = require('./logger')

const metricsPath = path.join(__dirname, 'metrics');



function loadData(){
    return {
        home: {
            hits: fs.readFileSync(path.join(metricsPath, 'hits', 'home')).toString(),
            latency: fs.readFileSync(path.join(metricsPath, 'latency', 'home')).toString(),
        },
        logs: {
            hits: fs.readFileSync(path.join(metricsPath, 'hits', 'logs')).toString(),
            latency: fs.readFileSync(path.join(metricsPath, 'latency', 'logs')).toString(),
        },
        metrics: {
            hits: fs.readFileSync(path.join(metricsPath, 'hits', 'metrics')).toString(),
            latency: fs.readFileSync(path.join(metricsPath, 'latency', 'metrics')).toString()
        }
    }
}

function handle(req, res) {
    const data = loadData();
    reader.view('metrics', data, res);
    res.end();
    logger.log(req, "metrics");
    register(Date.now() - req.startTime, 'metrics');
}

function registerHit(page) {
    const pth = path.join(metricsPath, 'hits', page);
    currentHits = parseInt(fs.readFileSync(pth).toString());
    afterHits = currentHits + 1;
    fs.writeFileSync(pth, afterHits);
    return afterHits
}

function registerLatency(page, newSize, newValue) {
    const pth = path.join(metricsPath, 'latency', page);
    currentMean = parseInt(fs.readFileSync(pth).toString());
    newMean = currentMean + (newValue - currentMean) / newSize;
    fs.writeFileSync(pth, newMean);
}


function register(latency, page) {
    currentHits = registerHit(page);
    registerLatency(page, currentHits, latency);
}

module.exports = {
    handle: handle,
    register: register
}