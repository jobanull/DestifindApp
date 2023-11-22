/* eslint-disable linebreak-style */
// eslint-disable-next-line no-unused-vars
const hapi = require('@hapi/hapi');
const joi = require('@hapi/joi');
const users = require('./data');
const jwt = require('jsonwebtoken');

// Part 1 : untuk registrasi buat nanti.
const nameuser = joi.object({
  username: joi.string().min(3).required(),
});

const emailUser = joi.object({
  email: joi.string().email().required(),
});

const userPassword = joi.object({
  password: joi.string().min(8).required(),
});
const register = (request, h) => {
  const {username, email, password} = request.payload;

  const usernameValidation = nameuser.validate({username});
  const emailValidation = emailUser.validate({email});
  const passwordValidation = userPassword.validate({password});

  // eslint-disable-next-line max-len
  if (!usernameValidation.error && !emailValidation.error && !passwordValidation.error) {
    // Validasi data pengguna (tambahkan validasi sesuai kebutuhan)
    const user = {username, email, password};
    users.push(user);
    return {message: 'Pendaftaran berhasil!'};
  } else {
    // eslint-disable-next-line max-len
    return h.response({error: 'Username, email, dan password diperlukan!'}).code(400);
  }
};
// Part 2 : Login
const blueprint = joi.object({
  email: joi.string().email().required(),
});

const login = (request, h) => {
  const {email, password} = request.payload;
  const EmailValidation = blueprint.validate({email});
  const passwordValidation = userPassword.validate({password});

  if (!EmailValidation.error && !passwordValidation.error) {
    const user = users.find((user) => (
      user.email === email && user.password === password));

    if (user) {
      const token = jwt.sign({email: user.email},
          'Destifindpw', {expiresIn: '1h'});
      users.push(token);
      const response = h.response({
        status: 'success',
        message: 'Accomplish',
        data: users.map((user) => ({
          username: user.username,
          email: user.email,
        }
        )),
      });
      response.code(200);
      return response;
    }
  }
  const response = h.response({
    status: 'fail',
    message: 'Invalid',
  });
  response.code(400);
  return response;
};
module.exports = {login, register};


/* Coba coba ini hehe don't matter*/

//         const username_req = joi.object ({
//         username: joi.string().min(2).max(14).required()
//         })
//         const usernameValidation = username_req.validate({ username });
//         if (!usernameValidation.error) {
//             const data =
//             `${username}`
//         const response = h.response({
//             status: 'success',
//             message: 'Congratulations you\'ve made it!',
//             data: data
//         });
//         response.code(200);
//         return response;
//     }
//     const user_email = joi.object ({
//         email: joi.string().email().required()
//     })
//     const emailValidation = user_email.validate({ email });
//     if (!emailValidation.error) {
//          const data =
//             `${email}`
//     const response = h.response({
//         status: 'success',
//         message: 'Congratulations you\'ve made it!',
//         data: data
//     });
//     response.code(200);
//     return response;
// }

//     else if (usernameValidation.error || emailValidation.error) {
//         const errors = {
// eslint-disable-next-line max-len
//             username: usernameValidation.error ? usernameValidation.error.message : null,
// eslint-disable-next-line max-len
//             email: emailValidation.error ? emailValidation.error.message : null
//         }
//      const response = h.response({
//         status: 'error',
//         message: 'error input',
//         data: errors
//         });
//         response.code(200);
//         return response;

//     }
