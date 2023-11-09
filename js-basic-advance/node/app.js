const express = require('express');
const bodyParser = require('body-parser')
const locationRoutes = require('./routes/location')

const app = express();

app.use(bodyParser.json());

app.use((req, res, next) => {
    res.setHeader('Access-Control-Allow-Origin', 'http://localhost:9000');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
    next();
})
//funnel the custom routes to app as middleware
app.use(locationRoutes);

app.listen(process.env.PORT || 3000); // cloud provider will inject the port as env otherwise fallback to 3000 for local dev