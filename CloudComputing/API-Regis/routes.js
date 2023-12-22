/* eslint-disable linebreak-style */
const {login, register} = require('./Authhandler');
const users = require('./data');
const {memcachedd} = require('../API-Main/Places');

const routes = [
  {
    method: 'GET',
    path: '/',
    handler: memcachedd,
  },
  {
    method: 'POST',
    path: '/login',
    handler: login,
  },
  {
    method: 'POST',
    path: '/register',
    handler: register,
  },
  {
    method: 'GET',
    path: '/datauser',
    handler: (request, h) => {
      const response = h.response({
        status: 'success',
        data: {
          Datauser: users,
        },
      });
      response.code(200);
      return response;
    },
  },
];
//  {
//     method: 'GET',
//     path: '/login',
//     handler:
//  },
//  {
//     method: 'PUT',
//     path: '/login',
//     handler:
//  },

module.exports = routes;
