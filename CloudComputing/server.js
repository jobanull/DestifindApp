/* eslint-disable linebreak-style */
const hapi = require('@hapi/hapi');
const routes = require('./Resources/routes');
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
  server.route(routes);

  await server.start();
  console.log(`Server berjalan di ${server.info.uri}`);
};

init();


// // Registrasi plugin untuk otentikasi JWT
// await server.register(Jwt);

// // Strategi otentikasi JWT
// server.auth.strategy('jwt', 'jwt', {
//   key: JwtKey,
//   validate: (decoded, request, h) => {
//     // Fungsi validasi token
//     // eslint-disable-next-line max-len,
//     // Di sini, Anda dapat memeriksa apakah
// pengguna yang dikodekan dalam token ada dalam basis data, dst.
//     // Misalnya:
//     const user = users.find((u) => u.email === decoded.email);
//     if (!user) {
//       return {isValid: false};
//     }
//     return {isValid: true};
//   },
// });

// // Strategi otentikasi JWT yang diterapkan secara default
// server.auth.default('jwt');

// Daftar route
