const {urlFile} = require("./utils/UrlConstructor");
const {loadDataFromImage} = require("./Utils/DataExtract");
const crypto = require('crypto');
const path = require('path');
const {config} = require("./config");
const vision = require('@google-cloud/vision');
const {Storage} = require('@google-cloud/storage');
const credentials = require('./auth.json');
const fs = require('fs');
const {getRequest} = require("./config");

const admin = require('firebase-admin');
const storage = new Storage({credentials});
const client = new vision.ImageAnnotatorClient({credentials});
admin.initializeApp({credential: admin.credential.cert(credentials)});
let db = admin.firestore();
let collection = db.collection("images");

async function verifyIfExists(fileHash) {
    const ref = db.collection(`images`).doc(fileHash);
    return await ref.get()
        .then(e => {
            if (e.exists)
                return e.data();
            return null;
        })
        .catch(e => {
            return null
        });
}

function JsonBody(data, res) {
    res.setHeader("Content-Type", "application/json");
    res.write(JSON.stringify(data));
    res.end();
}

callAnnotateImage = async (filePath, res,type) => {
    let imageFile = fs.readFileSync(filePath);
    let encoded = Buffer.from(imageFile).toString('base64');
    let hash = crypto.createHash(config.hash_alg);
    hash.setEncoding(config.hash_encoding);
    hash.write(encoded);
    hash.end();
    let hashFile = hash.read();
    let file = await verifyIfExists(hashFile);
    if (file !== null && file !== undefined) {
        JsonBody(file, res);
        return;
    }
    let fileStorage = await storage.bucket(config.bucket).upload(filePath, {destination: `${hashFile}${path.extname(type)}`, public: true});
    fs.unlinkSync(filePath);
    const request = getRequest(hashFile,path.extname(type));
    client.annotateImage(request)
        .then(response => {
            let dataResponse = {path: fileStorage[1].mediaLink, ...loadDataFromImage(response)};
            collection.doc(hashFile).set(dataResponse)
                .then(a => {
                    JsonBody(dataResponse, res);
                })
                .catch(e => console.error(e));
        }).catch(e => {
        console.log(e);
    });
};
module.exports = {
    callAnnotateImage
};


