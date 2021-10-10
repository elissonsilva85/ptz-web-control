/*jshint strict:false */

(function() {
    'use strict';
    // this function is strict...
}());

//
// https://www.section.io/engineering-education/compile-your-nodejs-application-into-a-exe-file/
//

// Setting up our app requirements

const path = require('path');
const fs = require('fs');
const express = require('express');
const app = express();

const Server = require('http').Server;
const server = new Server(app);

const config = JSON.parse(fs.readFileSync('config.json', 'utf8'));

// Setting up our port

server.listen(config.port, () => console.log(`Server at ${config.port}`));

// Configuiring simple express routes
// getDir() function is used here along with package.json.pkg.assets

app.use('/app', express.static( path.join( __dirname, './view/dist/app' ) ));

app.get('/', function(req, res) {
    res.redirect('/app')
});

// Using a function to set default app path
function getDir() {
    if (process.pkg) {
        return path.resolve(process.execPath + "/..");
    } else {
        return path.join(require.main ? require.main.path : process.cwd());
    }
}