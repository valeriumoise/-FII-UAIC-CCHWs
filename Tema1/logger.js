


function log(req, values) {
    const latency = Date.now() - req.startTime;
    console.log(`[${new Date().toISOString()}] ${req.connection.remoteAddress} requested ${req.url}. responded injecting ${values} into template. Latency: ${latency}ms`);
}

module.exports = {
    log: log
}