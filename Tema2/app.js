const http = require('http');

const routes = require('./router/routes');
const router = require('./router/router');
const config = require('./config/config')

process.on('uncaughtException', function(err) {
    // handle the error safely
    console.log('Uncaught exception');
    console.error(err.stack);
    console.log(err);
});


const server = http.createServer(async (req, res) => {
    await router(req, res, routes).catch(error => console.log(error));
});

server.listen(config.PORT, () => {
    console.log(`Server is listening on port ${config.PORT}`);
});