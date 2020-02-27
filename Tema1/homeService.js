const url = require('url')
const http = require('http')
const https = require('https')
const config = require('./config.json')
const reader = require('./reader')
const logger = require('./logger')
const metricsService = require('./metricsService')


errorHandle = function (e) {
	console.log("error: " + e.message);
};

module.exports = {
	handle(req, res) {
		var options = {
			host: 'ipv4bot.whatismyipaddress.com',
			port: 80,
			path: '/'
		};
		http.get(options, function (response) {
			response.on("data", function (chunk) {
				const ip = `${chunk}`;
				reqUrl = url.parse(url.format({
					host: config.ipstack.api_base_url,
					pathname: ip,
					query: {
						access_key: config.ipstack.key,
					}
				}));
				reqUrl = url.format(reqUrl);
				http.get(reqUrl, function (response) {
					response.on("data", function (data) {
						json = JSON.parse(data);
						langCode = json.location.languages[0].code;
						https.get(config.adviceslip.api_base_url, function (response) {
							response.on("data", function (data) {
								advice = JSON.parse(data).slip.advice;
								const lang = `en-${langCode}`;
								let url = `https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20200220T155748Z.830d8eabbb029a4b.a9cec9557e3e3bfdad95513458142af93dcb342c&lang=${lang}&text=${advice}`
								https.get(url, function (response) {
									response.on("data", function (data) {
										translatedAdvice = JSON.parse(data).text[0];
										reader.view("index", {advice: translatedAdvice}, res);
										res.end();
										logger.log(req, [translatedAdvice]);
										metricsService.register(Date.now() - req.startTime, 'home');
									});
								}).on('error', errorHandle)
							});
						}).on('error', errorHandle)
					});
				}).on('error', errorHandle)
			});
		}).on('error', errorHandle)
	}
}
