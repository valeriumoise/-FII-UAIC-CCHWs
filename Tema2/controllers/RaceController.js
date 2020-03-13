const mongoose = require('mongoose');
const Race = require('./../models/Race');
const helpers = require('./../helpers/helpers');
const config = require('./../config/config')

let project = {
    _id: 1,
    name: 1,
    otherNames: 2,
    subspecies: 1,
    languages: 1,
    locations: 1,
    avglifespan: 1,
    avgheight: 1,
    hairColor: 1,
    eyeColor: 1,
    distinctions: 1,
    created: "$createdAt",
    edited: "$updatedAt",
    url: { $concat: [config.API_V1_URL, "/races/", { "$toString": "$_id"},'/'] }
}



class RaceController {
    // GET /races
    async index(req, res) {
        try {
            let pipeline = [{
                $project: project,
            }]
            let races = await Race.aggregation(pipeline);
            return helpers.success(res, races);
        }
        catch (error) {
            console.log(error);
            return helpers.error(res, error);
        }
    }

    // POST /races
    async create(req, res, param, postData) {
        postData = JSON.parse(postData);
        let { name, otherNames = [], subspecies = [], languages = [], locations = [], avglifespan, avgheight, hairColor, eyeColor, distinctions = [] } = postData;

        try {

            for (let x in [otherNames, subspecies, languages, locations, distinctions]) {
                x = Array.isArray(x) ? x : [x];
            }


            let subspeciesExists = await this.validateSubspecies(subspecies);
            if (!subspeciesExists) {
                return helpers.validationError(res, 'subspecies is invalid');
            }
            if (subspecies != null) {
                subspecies = mongoose.Schema.Types.ObjectId(subspecies);
            }

            const race = await Race.create({ name, otherNames, subspecies, languages, locations, avglifespan, avgheight, hairColor, eyeColor, distinctions });

            return helpers.success(res, race.toClient());
        }
        catch (error) {
            console.log(error);
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

    // GET /races/:id
    async show(req, res, param) {
        try {
            let pipeline = [
                { $match: { "_id": mongoose.Types.ObjectId(param) } },
                { $project: project }
            ]
            const character = await Race.aggregation(pipeline);
            return helpers.success(res, character);
        }
        catch (error) {
            return helpers.error(res, error);
        }
    }

    // PUT /race/:id
    async update(req, res, param, postData) {
        let race;
        try {
            race = await Race.get({ _id: param });
        }
        catch (e) {
            console.log(e);
        }
        if (!race) {
            return helpers.error(res, 'Entity not found', 404);
        }

        postData = JSON.parse(postData);

        let { name, otherNames = [], subspecies = [], languages = [], locations = [], avglifespan, avgheight, hairColor, eyeColor, distinctions = [] } = postData;
        let updateData = postData;

        try {
            for (let x in [otherNames, subspecies, languages, locations, distinctions]) {
                x = Array.isArray(x) ? x : [x];
            }

            let subspeciesExists = await this.validateSubspecies(subspecies);
            if (!subspeciesExists) {
                return helpers.validationError(res, 'subspecies is invalid');
            }
            if (subspecies != null) {
                subspecies = mongoose.Schema.Types.ObjectId(subspecies);
            }
            const race = await Race.findOneAndUpdate({ _id: param }, { $set: updateData }, { new: true });

            return helpers.success(res, race.toClient());
        }
        catch (error) {
            console.log(error);

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

    // DELETE /employee/:id
    async delete(req, res, param) {
        let race;
        try {
            race = await Race.get({ _id: param });
        }
        catch (e) {
            console.log(e);
        }
        if (!race) {
            return helpers.error(res, 'Entity not found', 404);
        }
        try {
            await Race.remove({ _id: param });

            return helpers.success(res);
        }
        catch (error) {
            return helpers.error(res, error);
        }
    }

    async validateSubspecies(subspecies) {
        if (subspecies === null) {
            return false;
        }
        if (subspecies.length == 0) {
            return true;
        }
        try {
            const raceExists = await Race.get({ _id: subspecies });
            return raceExists;
        }
        catch (e) {
            return false;
        }
    }

}

module.exports = new RaceController();
