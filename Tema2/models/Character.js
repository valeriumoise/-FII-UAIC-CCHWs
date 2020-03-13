const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const BaseModel = require('./BaseModel');

const config = require('../config/config')

const characterSchema = new Schema({
    name: {  type: String, unique:true, required: true},
    biography : String,
    aliases: [String],
    hairColor: [String],
    eyeColor: [String],
    skin: String,
    race: {type: mongoose.Schema.Types.ObjectId, ref: 'Race', get: (race) => `${config.API_V1_URL}/races/${race}`, required: true},
    gender: {type: String, required: true},
    profession: {type: mongoose.Schema.Types.ObjectId, ref: 'Profession', get: (profession) => `${config.API_V1_URL}/professions/${profession}`},
    abilities: [String]
}, { timestamps: true });

characterSchema.method('toClient', function () {
    const character = this.toObject();
    character.created = character.createdAt;
    character.edited = character.updatedAt;
    character.url = `${config.API_V1_URL}/characters/${character._id}`
    delete character.__v;
    delete character.deletedAt;    
    delete character.createdAt;
    delete character.updatedAt;
    return character;
});

const characterModel = BaseModel.model('characters', characterSchema);

class Character {
    static create (data) {
        const newCharacter = characterModel(data);

        return new Promise((resolve, reject) => {
            let error = newCharacter.validateSync();
            if (error) {
                reject(error);
            }
            
            newCharacter.save((err, obj) => {
                if (obj) {
                    resolve(obj);
                }
                else {
                    reject(err);
                }
            });
        });
    }

    static getAll (conditions, selectParams) {
        return new Promise((resolve, reject) => {
            const query = characterModel.find(conditions);

            if (selectParams) {
                query.select(selectParams);
            }

            query.lean().exec((err, docs) => {
                if (docs) {
                    resolve(docs);
                }
                else {
                    reject(err);
                }
            });
        });
    }

    static get (conditions, selectParams) {
        return new Promise((resolve, reject) => {
            const query = characterModel.findOne(conditions);

            if (selectParams) {
                query.select(selectParams);
            }

            query.lean().exec((err, docs) => {
                if (docs) {
                    resolve(docs);
                }
                else {
                    reject(err);
                }
            });
        });
    }

    static remove (conditions) {
        return new Promise((resolve, reject) => {
            characterModel.remove(conditions, (err, docs) => {
                if (docs) {
                    resolve(docs);
                }
                else {
                    reject(err);
                }
            });
        });
    }

    static findOneAndUpdate (conditions, updateData, options) {
        return new Promise((resolve, reject) => {
            characterModel.findOneAndUpdate(conditions, updateData, options, (err, docs) => {
                if (docs) {
                    resolve(docs);
                }
                else {
                    reject(err);
                }
            });
        });
    }

    static update (conditions, updateData, options) {
        return new Promise((resolve, reject) => {
            characterModel.update(conditions, updateData, options, (err, docs) => {
                if (docs) {
                    resolve(docs);
                }
                else {
                    reject(err);
                }
            });
        });
    }

    static aggregation (pipeline) {
        return new Promise((resolve, reject) => {
            characterModel.aggregate(pipeline, (err, docs) => {
                if (err) {
                    reject(err);
                }
                else {
                    resolve(docs);
                }
            });
        });
    }
}

module.exports = Character;