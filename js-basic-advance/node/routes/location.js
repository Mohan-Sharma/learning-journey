const express = require('express');
const { MongoClient, ServerApiVersion, ObjectId } = require('mongodb');

const uri = "mongodb+srv://admin:RzPWibIXM47gvUdV@learnmongodb.nduzvcy.mongodb.net/?retryWrites=true&w=majority";

async function save(location) {
    const client = new MongoClient(uri, { useNewUrlParser: true, useUnifiedTopology: true, serverApi: ServerApiVersion.v1 });
    try {
        const database = client.db('learn_mongodb');
        const locations = database.collection('locations');
        const result = await locations.insertOne(location);
        return result;
    } finally {
        // Ensures that the client will close when you finish/error
        await client.close();
    }
}

async function find(locationId) {
    const client = new MongoClient(uri, { useNewUrlParser: true, useUnifiedTopology: true, serverApi: ServerApiVersion.v1 });
    try {
        const database = client.db("learn_mongodb");
        const movies = database.collection("locations");
        const _id = new ObjectId(locationId);
        const query = { _id: _id };
        const location = await movies.findOne(query);
        console.log(location);
        return location;
    } finally {
        await client.close();
    }
}

const router = express.Router();

const localStorage = {
    locations: []
};

router.post('/save-location', async (req, res, next) => {
    const id = Math.random();
    const location = {
        id: id,
        address: req.body.address,
        coordinates: {
            lat: req.body.lat,
            lng: req.body.lng
        }
    };
    const result = await save(location).catch(console.dir)
    console.log('Saved location', result);
    if (!result) {
        res.status('500').json({ message: 'Failed to save record!' });
    }
    res.json({ message: 'Location stored!', locationId: result.insertedId.toString()});
});

router.get('/get-location', async (req, res, next) => {
    const locationId = req.query.locationId;
    const location = await find(locationId).catch(console.dir);
    console.log('Found Location',location);
    if (!location) {
        res.status('404').json({ message: 'Records not found!' });
    }
    res.json({ message: 'Record Found', data: location });
});

module.exports = router;