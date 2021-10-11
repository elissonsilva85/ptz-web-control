/*jshint strict:false */

(function() {
    'use strict';
    // this function is strict...
}());

//
// https://www.section.io/engineering-education/compile-your-nodejs-application-into-a-exe-file/
//

const path = require('path');
const fs = require('fs');
const express = require('express');
const app = express();

const Server = require('http').Server;
const server = new Server(app);

const config = JSON.parse(fs.readFileSync('config.json', 'utf8'));

// Setting up our port

server.listen(config.port, () => console.log(`Server at ${config.port}`));

/*
app.all('*', (req, res, next) => {
    let origin = req.get('origin');
    res.header('Access-Control-Allow-Origin', origin);
    res.header("Access-Control-Allow-Headers", "X-Requested-With");
    res.header('Access-Control-Allow-Headers', 'Content-Type');
    next();
});
*/

require('./src/proxy')(app, config);
require('./src/youtube')(app, config);

app.use('/app', express.static( path.join( __dirname, './view/dist/app' ) ));

app.get('/', function(req, res) {
    res.redirect('/app')
});
