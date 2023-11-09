const express = require('express'); // npm install express
const bodyParser = require('body-parser'); // parse incoming request npm install body-parser

const app = express(); // create and instantiate server

app.listen(3000);
app.set('view engine', 'ejs'); // npm install ejs // server side templating engine parse template and display
app.set('views' ,'./views')

app.use(bodyParser.urlencoded({extended: false}));
app.use((req, res, next) => {
    res.setHeader('Content-Type', 'text/html');
    next();
});

app.use((req, res, next) => {
    let username = req.body.username || 'Unknown User';
    res.render('index', {
        user: username
    });
});
