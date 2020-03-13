const BaseModel = require("./../models/BaseModel")
const config = require("./../config/config")
const helpers = require("./../helpers/helpers")

class DiscoveryController {
    // GET /
    async index(req, res) {
        try {
            const urls = {
                characters : `${config.API_V1_URL}/characters`,
                races : `${config.API_V1_URL}/races`,
                professions : `${config.API_V1_URL}/professions`
            }
            return helpers.success(res, urls);
        }
        catch (error) {
            return helpers.error(res, error);
        }
    }
}

module.exports = new DiscoveryController();
