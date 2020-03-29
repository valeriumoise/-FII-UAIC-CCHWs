const config = {
    features: ["FACE_DETECTION", "LABEL_DETECTION", "IMAGE_PROPERTIES", "WEB_DETECTION", "TEXT_DETECTION", "PRODUCT_SEARCH"],
    bucket: 'cloud_computing_uaic2',
    hash_encoding: 'hex',
    hash_alg: 'sha1'
};

function getRequest(hashFile,type) {
    return {
        "image": {
            "source": {
                "imageUri":
                    `gs://${config.bucket}//${hashFile}${type}`
            }
        },
        "features": config.features.map(f => {
            return {type: f}
        })
        ,
    };
}

module.exports = {
    config, getRequest
};