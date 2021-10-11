//
// 
//

const path = require('path');
const fs = require('fs');
const {google} = require('googleapis');
const OAuth2 = google.auth.OAuth2;

// If modifying these scopes, delete your previously saved credentials
// at ~/.credentials/youtube-nodejs-quickstart.json
const SCOPES = ['https://www.googleapis.com/auth/youtube.readonly'];
const TOKEN_DIR = (process.env.HOME || process.env.HOMEPATH || process.env.USERPROFILE) + '/.credentials/';
const TOKEN_PATH = TOKEN_DIR + 'ptz-web-control-youtube.json';
const REDIRECT_URL = "http://localhost/youtube/callback";

let secret = "";
let oauth2Client = "";
let clientSecret = "";
let clientId = "";

// Load client secrets from a local file.
fs.readFile( path.join( __dirname, '../assets/youtube_secret.json') , (err, content) => {
  if (err) {
    console.log('Error loading client secret file: ' + err);
    return;
  }
  let credentials = JSON.parse(content);
  clientSecret = credentials.installed.client_secret;
  clientId = credentials.installed.client_id;

  fs.readFile( TOKEN_PATH , (err, content) => {
    if (err) {
      console.log('Error loading credential file: ' + err);
      return;
    }
    oauth2Client = new OAuth2(clientId, clientSecret, REDIRECT_URL);
    oauth2Client.credentials = JSON.parse(content);
  });
  
});

/**
 * Create an OAuth2 client with the given credentials, and then execute the
 * given callback function.
 *
 * @param {Object} credentials The authorization client credentials.
 * @param {function} callback The callback to call with the authorized client.
 */
function authorize(callback) {
  oauth2Client = new OAuth2(clientId, clientSecret, REDIRECT_URL);

  // Check if we have previously stored a token.
  fs.readFile(TOKEN_PATH, function(err, token) {
    if (err) {
      getNewToken(oauth2Client, callback);
    } else {
      oauth2Client.credentials = JSON.parse(token);
      callback(true, oauth2Client);
    }
  });
}

/**
 * Get and store new token after prompting for user authorization, and then
 * execute the given callback with the authorized OAuth2 client.
 *
 * @param {google.auth.OAuth2} oauth2Client The OAuth2 client to get token for.
 * @param {getEventsCallback} callback The callback to call with the authorized
 *     client.
 */
function getNewToken(oauth2Client, callback) {
  var authUrl = oauth2Client.generateAuthUrl({
    access_type: 'offline',
    scope: SCOPES
  });
  callback(false, authUrl);
}

/**
 * Store token to disk be used in later program executions.
 *
 * @param {Object} token The token to store to disk.
 */
function storeToken(token) {
  try {
    fs.mkdirSync(TOKEN_DIR);
  } catch (err) {
    if (err.code != 'EEXIST') {
      throw err;
    }
  }
  fs.writeFile(TOKEN_PATH, JSON.stringify(token), (err) => {
    if (err) throw err;
    console.log('Token stored to ' + TOKEN_PATH);
  });
}

/**
 * Lists the names and IDs of up to 10 files.
 *
 * @param {google.auth.OAuth2} auth An authorized OAuth2 client.
 */
function getChannel(auth) {
  var service = google.youtube('v3');
  service.channels.list({
    auth: auth,
    part: 'snippet,contentDetails,statistics',
    forUsername: 'GoogleDevelopers'
  }, function(err, response) {
    if (err) {
      console.log('The API returned an error: ' + err);
      return;
    }
    var channels = response.data.items;
    if (channels.length == 0) {
      console.log('No channel found.');
    } else {
      console.log('This channel\'s ID is %s. Its title is \'%s\', and ' +
                  'it has %s views.',
                  channels[0].id,
                  channels[0].snippet.title,
                  channels[0].statistics.viewCount);
    }
  });
}

module.exports = (app, config) => {

  app.get('/youtube/connect', function(req, res) {
    if(!secret) {
      res.sendStatus(500);
      return;
    }
    authorize(secret, (success, result) => {
      if(success)
      {
        oauth2Client = result;
        res.sendStatus(200)
      }
      else
      {
        res.redirect(result);
      }
    });
  });
  
  app.get('/youtube/callback', function(req, res) {
    let code = req.query.code;
    oauth2Client.getToken(code, function(err, token) {
      if (err) {
        console.log('Error while trying to retrieve access token', err);
        return;
      }
      oauth2Client.credentials = token;
      storeToken(token);
      res.sendStatus(200);
    });
  });

  app.get('/youtube/getChannel', function(req, res) {
    getChannel(oauth2Client);
    res.sendStatus(200);
  });
  
}