const { parse } = require('querystring');
const helpers = require('./../helpers/helpers');

module.exports = async (req, res, routes) => {

    const route = routes.find((route) =>{
        const isMethod = route.method === req.method;
        
        let isPath = false;
        if (typeof route.path === 'object') {
            isPath = req.url.match(route.path);
        }
        else {
            isPath = route.path === req.url;
        }
        return isPath && isMethod;
    });
    let param = null;
    if (route && typeof route.path === 'object') {
        param = req.url.match(route.path)[1];
    }
    if (route) {
        let body = null;
        if (req.method === 'POST' || req.method === 'PUT') {
            body = await getPostData(req);
        }

        return route.handler(req, res, param, body);
    }
    else {
        return helpers.error(res, 'You were misled', 404);
    }
};

function getPostData(req) {
    return new Promise((resolve, reject) => {
       try {
           let body = '';
           req.on('data', chunk => {
               body += chunk.toString(); // convert Buffer to string
           });

           req.on('end', () => {
               //resolve(parse(body));
               resolve(body);
           });
       }
       catch (e) {
           reject(e);
       }
    });
}