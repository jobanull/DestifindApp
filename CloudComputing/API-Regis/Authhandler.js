/* eslint-disable linebreak-style */
/* eslint-disable max-len */
/* eslint-disable require-jsdoc */
/* eslint-disable linebreak-style */
// eslint-disable-next-line no-unused-vars
const {nanoid} = require('nanoid');
const joi = require('@hapi/joi');
const jwt = require('jsonwebtoken');
const {MongoClient, ServerApiVersion} = require('mongodb');
const bcrypt = require('bcrypt');
const saltRounds = 10;
const uri = process.env.uri;

let mongoClient;
let collection;
// melakukan koneksi ke mongodbCloud
async function insertData() {
  const client = new MongoClient(uri, {
    serverApi: {
      version: ServerApiVersion.v1,
      strict: true,
      deprecationErrors: true,
    },
  });

  try {
    await client.connect();
    console.log('Mongodb is Connected');
    mongoClient = client;
    collection = client.db('users').collection('users');
  } catch (error) {
    console.error('Failed to connect to MongoDB database:', error);
  }
}

insertData();

// Proses deklarasi validasi
const nameuser = joi.object({
  username: joi.string().min(3).required(),
});

const emailUser = joi.object({
  email: joi.string().email().required(),
});

const userPassword = joi.object({
  password: joi.string().min(8).required(),
});

async function hashPassword(password) {
  return bcrypt.hash(password, saltRounds);
}
// Melakukan proses register
async function register(request, h) {
  const {username, email, password} = request.payload;
  // Validasi Input data dari user
  const usernameValidation = await nameuser.validate({username});
  const emailValidation = await emailUser.validate({email});
  const passwordValidation = await userPassword.validate({password});

  // Jika tidak sesuai maka akan memberi respons
  if (!usernameValidation.error && !emailValidation.error && !passwordValidation.error) {
    if (!collection) {
      return h.response({error: 'Database connection not established!'}).code(500);
    }

    // Melakukan penyesuaian apakah username atau email sudah ada di database atau belum
    const existingUser = await collection.findOne({$or: [{username}, {email}]});

    if (existingUser) {
      return h.response({error: 'Username or email already in use!'}).code(400);
    }

    const hashedPassword = await hashPassword(password);

    const id = nanoid(8);
    const user = {id, username, email, password: hashedPassword};
    // memasukkan ke database user
    try {
      await collection.insertOne(user);
      return {message: 'Registration successful!'};
    } catch (error) {
      return h.response({error: 'Failed to register user!'}).code(400);
    }
  } else {
    return h.response({error: 'Username, email, and password are required!'}).code(400);
  }
}

const blueprint = joi.object({
  email: joi.string().email().required(),
});

// Melakukan proses login
async function login(request, h) {
  const {email, password} = request.payload;

  const EmailValidation = blueprint.validate({email});
  const passwordValidation = userPassword.validate({password});


  try {
    if (!EmailValidation.error && !passwordValidation.error) {
      const user = await collection.findOne({email});

      if (user) {
        const passwordMatch = await bcrypt.compare(password, user.password);
        // jika sesuai email dan password maka akan memberikan token
        if (passwordMatch) {
          const token = jwt.sign({email: user.email, userId: user._id}, 'Destifindpw', {algorithm: 'HS256'}, {expiresIn: '1h'});
          user.token = token;

          const response = h.response({
            status: 'success',
            message: 'Login successful',
            token: token,
          });

          response.code(200);
          return response;
        }
      }
    }
    // jika tidak sesuai email dan password maka akan memberikan respons
    const response = h.response({
      status: 'Fail',
      message: 'Invalid credentials',
    });

    response.code(400);
    return response;
  } catch (error) {
    return h.response({error: 'Failed to perform login!'}).code(400);
  }
}

module.exports = {register, login};
