const fs = require('fs')
const path = require('path')

function mergeValues(values, content) {
    for (const key in values) {
        if (typeof values[key] == 'object'){
            for (const key2 in values[key]) {
                content = content.replace(`{{${key}.${key2}}}`, values[key][key2]);
            }
        } else{
            content = content.replace(`{{${key}}}`, values[key]);
        }
        
    }
    return content;
}

function view(templateName, values, res) {
    const pth = path.join(__dirname, 'views', templateName + '.html');
    let fileContent = fs.readFileSync(pth);
    fileContent = mergeValues(values, fileContent.toString());
    res.write(fileContent);
}

module.exports = {
    view: view
}