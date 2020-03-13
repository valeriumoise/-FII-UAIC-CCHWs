const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const BaseModel = require('./BaseModel');

const config = require('../config/config')

const raceSchema = new Schema({
    name: { type: String, unique: true, required: true },
    otherNames: [String],
    subspecies: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Race', get: (subspecies) => `${config.API_V1_URL}/races/${subspecies}` }],
    languages: [{ type: String, required: true }],
    locations: [{ type: String, required: true }],
    avglifespan: String,
    avgheight: String,
    hairColor: String,
    eyeColor: String,
    distinctions: [String]
}, { timestamps: true });

raceSchema.method('toClient', function () {
    const race = this.toObject();
    race.created = race.createdAt;
    race.edited = race.updatedAt;
    race.url = `${config.API_V1_URL}/races/${race._id}`
    delete race.__v;
    delete race.deletedAt;
    delete race.createdAt;
    delete race.updatedAt;
    return race;
});

const raceModel = BaseModel.model('races', raceSchema);

class Race {
    static create(data) {
        const newRace = raceModel(data);

        return new Promise((resolve, reject) => {
            let error = newRace.validateSync()
            if (error) {
                reject(error);
            }

            newRace.save((err, obj) => {
                if (obj) {
                    resolve(obj);
                }
                else {
                    reject(err);
                }
            });
        });
    }

    static getAll(conditions, selectParams) {
        return new Promise((resolve, reject) => {
            const query = raceModel.find(conditions);

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

    static get(conditions, selectParams) {
        return new Promise((resolve, reject) => {
            const query = raceModel.findOne(conditions);

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

    static remove(conditions) {
        return new Promise((resolve, reject) => {
            raceModel.remove(conditions, (err, docs) => {
                if (docs) {
                    resolve(docs);
                }
                else {
                    reject(err);
                }
            });
        });
    }

    static findOneAndUpdate(conditions, updateData, options) {
        return new Promise((resolve, reject) => {
            raceModel.findOneAndUpdate(conditions, updateData, options, (err, docs) => {
                if (docs) {
                    resolve(docs);
                }
                else {
                    reject(err);
                }
            });
        });
    }

    static update(conditions, updateData, options) {
        return new Promise((resolve, reject) => {
            raceModel.update(conditions, updateData, options, (err, docs) => {
                if (docs) {
                    resolve(docs);
                }
                else {
                    reject(err);
                }
            });
        });
    }

    static aggregation(pipeline) {
        return new Promise((resolve, reject) => {
            raceModel.aggregate(pipeline, (err, docs) => {
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

module.exports = Race;