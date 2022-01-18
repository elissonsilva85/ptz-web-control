//
// https://developers.google.com/youtube/v3/quickstart/nodejs
//

const path = require('path');
const fs = require('fs');
const google = require('googleapis').google;
const OAuth2 = google.auth.OAuth2;
const service = google.youtube('v3');

// If modifying these scopes, delete your previously saved credentials
// at ~/.credentials/youtube-nodejs-quickstart.json
const SCOPES = [ 'https://www.googleapis.com/auth/youtube' ];
const TOKEN_DIR = (process.env.HOME || process.env.HOMEPATH || process.env.USERPROFILE) + '/.credentials/';
const TOKEN_PATH = TOKEN_DIR + 'ptz-web-control-youtube.json';
const REDIRECT_URL = "http://localhost/youtube/callback";

let oauth2Client = "";
let credentials = "";
let clientSecret = "";
let clientId = "";

// Load client secrets from a local file.
fs.readFile( path.join( __dirname, '../assets/youtube_secret.json') , (err, content) => {
  if (err) {
    console.log('Error loading client secret file: ' + err);
    return;
  }
  credentials = JSON.parse(content);
  clientSecret = credentials.web.client_secret;
  clientId = credentials.web.client_id;

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
 function channelsList() {
  return service.channels.list({
    auth: oauth2Client,
    part: [
      'snippet,contentDetails,statistics'
    ],
    mine: true
  })
  .then((response) => {
    return Promise.resolve({
      data: {
        channelsList: response.data
      }
    });
  });
}

function liveBroadcastsList(videoId) {
  //
  // https://developers.google.com/youtube/v3/live/docs/liveBroadcasts/list
  //

  let params = {
    auth: oauth2Client,
    part: [
      "snippet,contentDetails,status"
    ],
    broadcastType: "event"
  }

  if(videoId) {
    params.id = videoId
  } else {
    params.broadcastStatus = "upcoming";
  }

  return service.liveBroadcasts.list(params)
  .then((response) => {
    return Promise.resolve({
      data: {
        liveBroadcastsList: response.data
      }
    })
  });
}

function liveStreamsList(streamId) {
  //
  // https://developers.google.com/youtube/v3/live/docs/liveStreams/list
  //
  return service.liveStreams.list({
    auth: oauth2Client,
    part: [
      "snippet,cdn,contentDetails,status"
    ],
    mine: true
  })
  .then((response) => {
    return Promise.resolve({
      data: {
        liveStreamsList: response.data
      }
    })
  });
}

function liveBroadcastsInsert(params) {
  //
  // https://developers.google.com/youtube/v3/live/docs/liveBroadcasts/insert
  //
  return service.liveBroadcasts.insert({
    auth: oauth2Client,
    part: [
      "snippet,contentDetails,status"
    ],
    resource: {
      snippet: {
        title: "Teste de agendamento de vídeo",
        description: "Descrição",
        scheduledStartTime: "2021-11-05T08:05:45-03:00"
      },
      contentDetails: {
        enableClosedCaptions: false,
        enableContentEncryption: true,
        enableAutoStart: true,
        enableAutoStop: true,
        enableDvr: true,
        enableEmbed: true,
        recordFromStart: true,
        startWithSlate: true
      },
      status: {
        privacyStatus: "private",
        selfDeclaredMadeForKids: false
      }
    }
  })
  .then((response) => {
    return Promise.resolve({
      data: {
        liveBroadcastsInsert: response.data
      }
    })
  });
}

function thumbnailSet(videoId, fileName) {
  //
  // https://developers.google.com/youtube/v3/docs/thumbnails/set
  //

  //get image file extension name
  let extensionName = path.extname(fileName);

  return service.thumbnails.set({
    auth: oauth2Client,
    videoId: videoId,
    media: {
      mimeType: `image/${extensionName.split('.').pop()}`,
      body: fs.readFileSync(fileName)
    }
  })
  .then((response) => {
    return Promise.resolve({
      data: {
        thumbnailSet: response.data
      }
    });
  });
}

function playlistItemsInsert(videoId, playlistId) {
  //
  // https://developers.google.com/youtube/v3/docs/playlists/insert
  //

  return service.playlistItems.insert({
    auth: oauth2Client,
    part: [
      "snippet"
    ],
    resource: {
      snippet: {
        playlistId: playlistId,
        resourceId: {
          kind: "youtube#video",
          videoId: videoId
        }
      }
    }
  })
  .then((response) => {
    return Promise.resolve({
      data: {
        playlistItemsInsert: response.data
      }
    })
  });
}

module.exports = (app, config) => {

  app.get('/youtube/connect', function(req, res) {
    if(!credentials) {
      res.sendStatus(500);
      return;
    }
    authorize((success, result) => {
      if(success)
      {
        oauth2Client = result;
        res.status(200).end("Voce esta conectado. Pode fechar essa janela.")
      }
      else
      {
        res.redirect(result);
      }
    });
  });

  app.get('/youtube/isConnected', function(req, res) {
    console.log(TOKEN_PATH);
    fs.access(TOKEN_PATH, (err) => {
      res.status(200).json({ connected: (err ? false : true) });
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

  app.get('/youtube/channelsList', function(req, res) {
    channelsList()
      .then((result) => {
          var channels = result.data.channelsList.items;
          if (!channels || channels.length == 0) {
            console.log('No channel found.');
            res.status(404).end('No channel found.');
            return;
          }
          res.status(200).json(result);
      })
      .catch((err) => {
          console.log('The API returned an error: ' + err);
          res.status(500).json(err);
      });
  });

  app.get('/youtube/liveBroadcastsList', function(req, res) {

    liveBroadcastsList()
      .then((response) => {
        res.status(200).json(response);
      })
      .catch((err) => {
        console.log('The API returned an error: ' + err);
        res.status(500).json(err);
      });
    
  });

  app.get('/youtube/liveStreamsList', function(req, res) {

    liveStreamsList()
      .then((response) => {
        res.status(200).json(response);
      })
      .catch((err) => {
        console.log('The API returned an error: ' + err);
        res.status(500).json(err);
      });
    
  });

  app.get('/youtube/liveBroadcastsInsert', function(req, res) {
    let videoId = "";
    let boundStreamId = "";
    let playlistId = ""; //"PL4kdVvykXhfJ6zquEkLO9MI97r_NYJTep";
    let thumbnailFilename = `${process.cwd()}/capa.png`;
    let finalResponse = { data: {} };

    liveBroadcastsInsert()
      .then((response) => {
        videoId = response.data.liveBroadcastsInsert.id;
        //
        finalResponse = { data: {...finalResponse.data, ...response.data} }
        return liveBroadcastsList(videoId);
      })
      .then((response) => {
        //boundStreamId = response.data.liveBroadcastsList.id;
        //
        finalResponse = { data: {...finalResponse.data, ...response.data} }
        return thumbnailSet(videoId, thumbnailFilename);
      })
      .then((response) => {
        finalResponse = { data: {...finalResponse.data, ...response.data} }
        return playlistItemsInsert(videoId, playlistId)
      })
      .then((response) => {
        finalResponse = { data: {...finalResponse.data, ...response.data} }
        res.status(200).json(finalResponse);
      })
      .catch((err) => {
        console.log('The API returned an error: ' + err);
        res.status(500).json(err);
      });
  });
  
}