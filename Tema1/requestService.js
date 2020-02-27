const http = require("http")
const https = require("https")
const url = require("url")

module.exports = {
    request(base_url, kwargs) {
        
        full_url = `${base_url}?`;
        for ( const [k, v] of Object.entries(kwargs)) {
            s = `${s} ${k} ${v}`;
        }
        return s;
    }
}