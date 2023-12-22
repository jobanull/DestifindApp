/* eslint-disable require-jsdoc */
const users = require('../API-Regis/data');
const {MongoClient, ServerApiVersion} = require('mongodb');
const uri = process.env.uri;
// Create a MongoClient with a MongoClientOptions object to set the Stable API version
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

module.exports = {insertData};
