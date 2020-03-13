const mongoose = require('mongoose');
const Profession = require('./../models/Profession');
const helpers = require('./../helpers/helpers');
const config = require('./../config/config')

let project = {
    _id: 1,
    name: 1,
    description: 1,
    created: "$createdAt",
    edited: "$updatedAt",
    url: { $concat: [config.API_V1_URL, "/professions/", { "$toString": "$_id" }, '/'] }
}

class ProfessionController {
    // GET /professions
    async index(req, res) {
        try {
            let pipeline = [{
                $project: project,
            }]
            const races = await Profession.aggregation(pipeline);
            return helpers.success(res, races);
        }
        catch (error) {
            return helpers.error(res, error);
        }
    }

    // POST /professions
    async create(req, res, param, postData) {
        postData = JSON.parse(postData);
        let { name, description } = postData;
        try {
            const profession = await Profession.create({ name, description });
            return helpers.success(res, profession.toClient());
        }
        catch (error) {
            if (error.name === 'ValidationError') {
                return helpers.validationError(res, error);
            }
            else if (error.message.indexOf('duplicate key error') !== -1) {
                return helpers.validationError(res, 'Name already exists');
            }
            else {
                return helpers.error(res);
            }
        }
    }

    // GET /professions/:id
    async show(req, res, param) {
        try {
            let pipeline = [
                { $match: { "_id": mongoose.Types.ObjectId(param) } },
                { $project: project }
            ]
            const profession = await Profession.aggregation(pipeline);

            return helpers.success(res, profession);
        }
        catch (error) {
            return helpers.error(res, error);
        }
    }

    // PUT /characters/:id
    async update(req, res, param, postData) {
        let profession;
        try {
            profession = await Profession.get({ _id: param });
        }
        catch (e) {
            console.log(e);
        }
        if (!profession) {
            return helpers.error(res, 'Entity not found', 404);
        }
        postData = JSON.parse(postData);

        profession = await Profession.findOneAndUpdate({ _id: param }, { $set: postData }, { multi: true });

        return helpers.success(res, profession.toClient());
    }
    catch(error) {
        console.log(error);

        if (error.name === 'ValidationError') {
            return helpers.validationError(res, error);
        }
        else if (error.message.indexOf('duplicate key error') !== -1) {
            return helpers.validationError(res, 'Profession name already exists');
        }
        else {
            return helpers.error(res);
        }
    }


    // DELETE /employee/:id
    async delete(req, res, param) {
        let profession;
        try {
            profession = await Profession.get({ _id: param });
        }
        catch (e) {
            console.log(e);
        }
        if (!profession) {
            return helpers.error(res, 'Entity not found', 404);
        }
        try {
            await Profession.remove({ _id: param });
            return helpers.success(res);
        }
        catch (error) {
            console.log(error)
            return helpers.error(res, error);
        }
    }

}

module.exports = new ProfessionController();
