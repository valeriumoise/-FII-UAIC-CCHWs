const mongoose = require('mongoose');
const Character = require('./../models/Character');
const Profession = require('./../models/Profession');
const Race = require('./../models/Race')
const helpers = require('./../helpers/helpers');
const config = require('./../config/config')

let project = {
    _id: 1,
    name: 1,
    biography: 1,
    aliases: 1,
    hairColor: 1,
    eyeColor: 1,
    skin: 1,
    race: { $concat: [config.API_V1_URL, "/races/", { "$toString": "$race" }, '/'] },
    geneder: 1,
    profession: [{ $concat: [config.API_V1_URL, "/professions/", { "$toString": "$profession" }, '/'] }],
    abilities: 1,
    created: "$createdAt",
    edited: "$updatedAt",
    url: { $concat: [config.API_V1_URL, "/characters/", { "$toString": "$_id" }, '/'] }
}

class CharacterController {
    // GET /characters
    async index(req, res) {
        try {
            const pipeline = [{ $project: project }];
            const characters = await Character.aggregation(pipeline);
            return helpers.success(res, characters);
        }
        catch (error) {
            return helpers.error(res, error);
        }
    }

    // POST /characters
    async create(req, res, param, postData) {
        postData = JSON.parse(postData);
        let { name, biography, aliases = [], hairColor = [], eyeColor = [], skin = null, race, gender, profession = [], abilities = [] || null} = postData;

        try {
            for (let x in [aliases, hairColor, eyeColor, profession, abilities]) {
                x = Array.isArray(x) ? x : [x];
            }

            let raceExists = await this.validateRace(race);
            if (!raceExists) {
                return helpers.validateError(res, 'Race is invalid');
            }
            if (race != null) {
                race = mongoose.Types.ObjectId(race);
            }

            let professionsExist = await this.validateProfessions(profession);
            if (!professionsExist) {
                return helpers.validationError(res, 'Profession(s) is invalid');
            }
            if (profession.length > 0) {
                profession = profession.map(professionId => mongoose.Types.ObjectId(professionId))
            } else{
                profession = null;
            }

            const character = await Character.create({ name, biography, aliases, hairColor, eyeColor, skin, race, gender, profession, abilities });

            return helpers.success(res, character.toClient());
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

    // GET /characters/:id
    async show(req, res, param) {
        try {
            const pipeline = [
                { $match: { "_id": mongoose.Types.ObjectId(param) } },
                { $project: project }
            ];

            const character = await Character.aggregation(pipeline);

            return helpers.success(res, character);
        }
        catch (error) {
            return helpers.error(res, error);
        }
    }

    // PUT /characters/:id
    async update(req, res, param, postData) {
        let character;

        try {
            character = await Character.get({ _id: param }, { isManager: 1 });
        }
        catch (e) {
            console.log(e);
        }

        if (!character) {
            return helpers.error(res, 'Entity not found', 404);
        }

        postData = JSON.parse(postData);

        try {
            let { name, biography, aliases = [], hairColor = [], eyeColor = [], skin = null, race, gender, profession = [], abilities = [] } = postData;
            let updateData = postData;

            for (let x in [aliases, hairColor, eyeColor, profession, abilities]) {
                x = Array.isArray(x) ? x : [x];
            }

            let raceExists = await this.validateRace(race);
            if (!raceExists) {
                return helpers.validateError(res, 'Race is invalid');
            }
            if (race != null) {
                race = mongoose.Types.ObjectId(race);
            }

            let professionsExist = await this.validateProfessions(profession);
            if (!professionsExist) {
                return helpers.validationError(res, 'Profession(s) is invalid');
            }
            if (profession.length > 0) {
                profession = profession.map(professionId => mongoose.Types.ObjectId(professionId))
            }else{
                profession = null;
            }

            const character = await Character.findOneAndUpdate({ _id: param }, { $set: { name, biography, aliases, hairColor, eyeColor, skin, race, gender, profession, abilities } }, { new: true });

            return helpers.success(res, character.toClient());
        }
        catch (error) {
            console.log(error);

            if (error.name === 'ValidationError') {
                return helpers.validationError(res, error);
            }
            else if (error.message.indexOf('duplicate key error') !== -1) {
                return helpers.validationError(res, 'Email already exists');
            }
            else {
                return helpers.error(res);
            }
        }
    }

    // DELETE /employee/:id
    async delete(req, res, param) {
        let character;
        try {
            character = await Character.get({ _id: param });
        }
        catch (e) {
            console.log(e);
        }
        if (!character) {
            return helpers.error(res, 'Entity not found', 404);
        }
        try {
            await Character.remove({ _id: param });
            return helpers.success(res);
        }
        catch (error) {
            return helpers.error(res, error);
        }
    }

    async validateRace(race) {
        if (race === null) {
            return false;
        }
        try {
            const raceExists = await Race.get({ _id: race });
            return raceExists;
        }
        catch (e) {
            return false;
        }
    }

    async validateProfessions(professions) {
        if (professions === null) {
            return true;
        }
        if (professions.length == 0) {
            return true;
        }
        try {
            const existentProfessions = await Profession.getAll({ _id: { $in: professions } }, { _id: 1 });
            return (professions.length === existentProfessions.length);
        }
        catch (e) {
            return false;
        }
    }
}

module.exports = new CharacterController();
