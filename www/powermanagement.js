
var PowerManagement = function() {};

/**
 * Acquire a SCREEN_DIM_WAKE_LOCK
 *
 * @param successCallback function to be called when the wake-lock was acquired successfully
 * @param errorCallback function to be called when there was a problem with acquiring the wake-lock
 */
PowerManagement.prototype.dim = function(successCallback, failureCallback) {
    cordova.exec(successCallback, failureCallback, 'PowerManagement', 'acquire', [true]);
}

/**
 * Acquire a PARTIAL_WAKE_LOCK
 *
 * @param successCallback function to be called when the wake-lock was acquired successfully
 * @param errorCallback function to be called when there was a problem with acquiring the wake-lock
 */
PowerManagement.prototype.acquire = function(successCallback, failureCallback) {
    cordova.exec(successCallback, failureCallback, 'PowerManagement', 'acquire', [false]);
}

/**
 * Release the wake-lock
 *
 * @param successCallback function to be called when the wake-lock was released successfully
 * @param errorCallback function to be called when there was a problem while releasing the wake-lock
 */
PowerManagement.prototype.release = function(successCallback, failureCallback) {
    cordova.exec(successCallback, failureCallback, 'PowerManagement', 'release', []);
}

module.exports = new PowerManagement();
