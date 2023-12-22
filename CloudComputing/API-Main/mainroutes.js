/* eslint-disable max-len */
const {getLocationInfoNearby} = require('./Main App');
const jwt = require('jsonwebtoken');

const Mainroutes = [
  {
    method: 'POST',
    path: `/main`,
    handler: async (request, h) => {
      // eslint-disable-next-line max-len
      const token = request.headers.authorization.split(' ')[1] || request.query.token;
      try {
        // eslint-disable-next-line max-len

        // Melakukan proses verifikasi Token
        const verifikasi = jwt.verify(token, process.env.pwtoken, {algorithms: ['HS256']});
        console.log(verifikasi);
        const result = await getLocationInfoNearby(request, h);
        // Bila verifikasi berhasil, Mengembalikan data tempat
        if (verifikasi) {
          return result;
        }
      } catch (error) {
        // eslint-disable-next-line max-len
        // Jika verifikasi gagal, kembalikan pesan kesalahan atau minta autentikasi
        console.error('Error verifying token:', error.message);
        console.error('Error stack:', error.stack);
        return h.response({error: 'Token tidak valid'}).code(401);
      }
    },
  },
];
module.exports = Mainroutes;

