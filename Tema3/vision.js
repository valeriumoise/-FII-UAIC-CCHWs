const vision = require('@google-cloud/vision');
const credentials = require('./auth.json');
const fs = require('fs');
const client = new vision.ImageAnnotatorClient({
    credentials
});

callAnnotateImage = async () => {
    var imageFile = fs.readFileSync('images/test.jpg');
    var encoded = Buffer.from(imageFile).toString('base64');

    const request = {
        "image": {
            "content": encoded
        },
        "features": [
            {
                "type": "FACE_DETECTION"
            },
            {
                "type": "LABEL_DETECTION"
            },
            {
                "type": "IMAGE_PROPERTIES"
            },
            {
                "type": "WEB_DETECTION"
            }
        ],
    };

    try {
        const call = await client.annotateImage(request);
        const util = require('util')

        console.log(util.inspect(call, {showHidden: false, depth: null}))
    } catch (error) {
        console.error(error);
    }
};