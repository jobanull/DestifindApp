/* eslint-disable linebreak-style */
const {login, register} = require('./handler');
const users = require('./data');
const routes = [
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
      return {users};
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
