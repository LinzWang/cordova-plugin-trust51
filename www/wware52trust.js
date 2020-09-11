var exec = require('cordova/exec')

exports.coolMethod = function (arg0, success, error) {
  exec(success, error, 'wware52trust', 'coolMethod', [arg0])
}
exports.testing = function (arg0, success, error) {
  exec(success, error, 'wware52trust', 'testing', [arg0])
}
exports.getCertinfo = function (arg0, success, error) {
  exec(success, error, 'wware52trust', 'getCertinfo', [arg0])
}
exports.setServerEnv = function (arg0, success, error) {
  exec(success, error, 'wware52trust', 'setServerEnv', [arg0])
}
exports.certDownload = function (arg0, success, error) {
  exec(success, error, 'wware52trust', 'certDownload', [arg0])
}
exports.echo = function (arg0, success, error) {
  exec(success, error, 'wware52trust', 'echo', [arg0])
}
