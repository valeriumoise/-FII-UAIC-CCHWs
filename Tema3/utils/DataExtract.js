function loadDataFromImage(data) {
    let faces = data[0].faceAnnotations;
    let facialExpressions = [];
    for (let i = 0; i < faces.length; i++) {
        facialExpressions.push({
            'detectionConfidence': faces[i].detectionConfidence,
            'landmarkingConfidence': faces[i].landmarkingConfidence,
            "joyLikelihood": faces[i].joyLikelihood,
            "sorrowLikelihood": faces[i].sorrowLikelihood,
            "angerLikelihood": faces[i].angerLikelihood,
            "surpriseLikelihood": faces[i].surpriseLikelihood,
            "underExposedLikelihood": faces[i].underExposedLikelihood,
            "blurredLikelihood": faces[i].blurredLikelihood,
            "headwearLikelihood": faces[i].headwearLikelihood
        })
    }

    let labels = [];
    let labelAnnotations = data[0].labelAnnotations;
    for (let i = 0; i < labelAnnotations.length; i++) {
        labels.push({
            description: labelAnnotations[i].description,
            score: labelAnnotations[i].score
        });
    }

    let dominantColors = [];
    let dominantAnnotations = data[0].imagePropertiesAnnotation.dominantColors.colors;
    for (let i = 0; i <= dominantAnnotations.length; i++) {
        if (i >= 5) break;
        dominantColors.push({
            red: dominantAnnotations[i].color.red,
            green: dominantAnnotations[i].color.green,
            blue: dominantAnnotations[i].color.blue,
            alpha: dominantAnnotations[i].color.alpha
        })
    }
    let web = [];
    let webEntities = data[0].webDetection.webEntities;
    for (let i = 0; i < webEntities.length; i++) {
        web.push(webEntities[i].description)
    }
    return {
        'noFaces': data[0].faceAnnotations.length,
        'facialExpressions': facialExpressions,
        'labels': labels,
        'web': web,
        'text': data[0].fullTextAnnotation!=null?data[0].fullTextAnnotation.text:'',
        'colors': dominantColors,
        "bestGuestLabel": data[0].webDetection.bestGuessLabels[0]
    }

}

module.exports = {
    loadDataFromImage
};