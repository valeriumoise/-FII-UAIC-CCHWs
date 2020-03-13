const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const BaseModel = require('./BaseModel');

const config = require('../config/config')

const professionSchema = new Schema({
    name: {  type: String, unique:true, required: true},
    description: String
}, { timestamps: true });

professionSchema.method('toClient', function () {
    const profession = this.toObject();
    profession.created = profession.createdAt;
    profession.edited = profession.updatedAt;
    profession.url = `${config.API_V1_URL}/professions/${profession._id}`
    delete profession.__v;
    delete profession.deletedAt;    
    delete profession.createdAt;
    delete profession.updatedAt;
    return profession;
});

const professionModel = BaseModel.model('professions', professionSchema);

class Profession {
    static create (data) {
        const newProfession = professionModel(data);

        return new Promise((resolve, reject) => {
            let error = newProfession.validateSync();
            if (error) {
                reject();
            }
            
            newProfession.save((err, obj) => {
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
            const query = professionModel.find(conditions);

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
            const query = professionModel.findOne(conditions);

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
            professionModel.remove(conditions, (err, docs) => {
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
            professionModel.findOneAndUpdate(conditions, updateData, options, (err, docs) => {
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
            professionModel.update(conditions, updateData, options, (err, docs) => {
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
            professionModel.aggregate(pipeline, (err, docs) => {
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

module.exports = Profession;