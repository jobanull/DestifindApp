/* eslint-disable linebreak-style */
const hapi = require('@hapi/hapi');
const routes = require('./API-Regis/routes');
const Mainroutes = require('./API-Main/mainroutes');
// const Boom = require('@hapi/boom');
// const Jwt = require('hapi-auth-jwt2');
// const JwtKey = 'Destifindpw';

const init = async () => {
  const server = hapi.server({
    port: 8080,
    host: process.env.NODE_ENV !== 'production' ? 'localhost' : '0.0.0.0',
    routes: {
      cors: {
        origin: ['*'],
      },
      // payload: {
      //   multipart: true, // Aktifkan dukungan untuk multipart/form-data
      // },
    },
  });


  const combinedRoutes = [
    ...routes,
    ...Mainroutes,
  ];

  server.route(combinedRoutes);

  await server.start();
  console.log(`Server berjalan di ${server.info.uri}`);
};

init();
