const characterController = require('../controllers/CharacterController');
const raceController = require('../controllers/RaceController');
const professionController = require('../controllers/ProfessionController')
const discoveryController = require('../controllers/DiscoveryController')

const config = require('./../config/config')

const routes = [
    {
        method: 'GET',
        path: config.API_V1_PATH + '/',
        handler: discoveryController.index.bind(discoveryController)
    },
    {
        method: 'GET',
        path: config.API_V1_PATH + '/characters',
        handler: characterController.index.bind(characterController)
    },
    {
        method: 'GET',
        path: new RegExp(config.API_V1_PATH + /characters\/([0-9a-z]+)/),
        handler: characterController.show.bind(characterController)
    },
    {
        method: 'POST',
        path: config.API_V1_PATH + '/characters',
        handler: characterController.create.bind(characterController)
    },
    {
        method: 'PUT',
        path: new RegExp(config.API_V1_PATH + /characters\/([0-9a-z]+)/),
        handler: characterController.update.bind(characterController)
    },
    {
        method: 'DELETE',
        path: new RegExp(config.API_V1_PATH + /characters\/([0-9a-z]+)/),
        handler: characterController.delete.bind(characterController)
    },
    {
        method: 'POST',
        path: config.API_V1_PATH + '/races',
        handler: raceController.create.bind(raceController)
    },
    {
        method: 'GET',
        path: config.API_V1_PATH + '/races',
        handler: raceController.index.bind(raceController)
    },
    {
        method: 'GET',
        path: new RegExp(config.API_V1_PATH + /races\/([0-9a-z]+)/),
        handler: raceController.show.bind(raceController)
    },
    {
        method: 'PUT',
        path: new RegExp(config.API_V1_PATH + /races\/([0-9a-z]+)/),
        handler: raceController.update.bind(raceController)
    },
    {
        method: 'DELETE',
        path: new RegExp(config.API_V1_PATH + /races\/([0-9a-z]+)/),
        handler: raceController.delete.bind(raceController)
    },
    {
        method: 'GET',
        path: config.API_V1_PATH + '/professions',
        handler: professionController.index.bind(professionController)
    },
    {
        method: 'GET',
        path: new RegExp(config.API_V1_PATH + /professions\/([0-9a-z]+)/),
        handler: professionController.show.bind(professionController)
    },
    {
        method: 'POST',
        path: config.API_V1_PATH + '/professions',
        handler: professionController.create.bind(professionController)
    },
    {
        method: 'PUT',
        path: new RegExp(config.API_V1_PATH + /professions\/([0-9a-z]+)/),
        handler: professionController.update.bind(professionController)
    },
    {
        method: 'DELETE',
        path: new RegExp(config.API_V1_PATH + /professions\/([0-9a-z]+)/),
        handler: professionController.delete.bind(professionController)
    }
];

module.exports = routes;