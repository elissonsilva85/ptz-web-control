//
// https://betterprogramming.pub/how-to-build-a-reverse-proxy-with-express-87a31ec2ec80
//

const { createProxyMiddleware } = require('http-proxy-middleware');

let ptzAppRoutes = [
  {
    origin: '/PTZ1',
    target: 'http://192.168.0.110'
  },
  {
    origin: '/PTZ2',
    target: 'http://192.168.0.111'
  },
  {
    origin: '/PTZ3',
    target: 'http://192.168.0.112'
  }
]

var restream = function(proxyReq, req, res, options) {
  if (req.body) {
      let bodyData = JSON.stringify(req.body);
      // incase if content-type is application/x-www-form-urlencoded -> we need to change to application/json
      proxyReq.setHeader('Content-Type','application/json');
      proxyReq.setHeader('Content-Length', Buffer.byteLength(bodyData));
      // stream the content
      proxyReq.write(bodyData);
  }
}

module.exports = (app, config) => {
    for (var proxy in config.proxy) {
      let target = config.proxy[proxy].target;
      let pathRewrite = config.proxy[proxy].pathRewrite;
      //
      app.use(proxy, createProxyMiddleware({ 
        target: target, 
        changeOrigin: true,
        pathRewrite: function (path, req) { 
          for (var rule in pathRewrite)
            path = path.replace(new RegExp(rule, "g"), pathRewrite[rule]);
          return path; 
        },
        onProxyReq: restream
      }));
    }
};