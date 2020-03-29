/**
 * Cordova (Android) plugin
 */
package pierfabio.plugins.powermanagement;

import android.content.Context;
import android.os.PowerManager;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;


public class PowerManagement extends CordovaPlugin {
	
	// As we only allow one wake-lock, we keep a reference to it here
	private PowerManager.WakeLock wakeLock = null;
	private PowerManager powerManager = null;

	/**
	 * Fetch a reference to the power-service when the plugin is initialized
	 */
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);

		this.powerManager = (PowerManager) cordova.getActivity().getSystemService(Context.POWER_SERVICE);
	}

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {

		PluginResult result = null;

		try {
			if( action.equals("acquire") ) {
				if( args.length() > 0 && args.getBoolean(0) ) {
					result = this.acquire( PowerManager.SCREEN_DIM_WAKE_LOCK );
				}
				else {
					result = this.acquire( PowerManager.PARTIAL_WAKE_LOCK );
				}
			} else if( action.equals("release") ) {
				result = this.release();
			}
		}
		catch( JSONException e ) {
			result = new PluginResult(Status.JSON_EXCEPTION, e.getMessage());
		}

		callbackContext.sendPluginResult(result);
		return true;
	}

	/**
	 * Acquire a wake-lock
	 * @param p_flags Type of wake-lock to acquire
	 * @return PluginResult containing the status of the acquire process
	 */
	private PluginResult acquire( int p_flags ) {
		PluginResult result = null;

		if (this.wakeLock == null) {
			this.wakeLock = this.powerManager.newWakeLock(p_flags, "PowerManagementPlugin");
			try {
				this.wakeLock.acquire();
				result = new PluginResult(PluginResult.Status.OK);
			}
			catch( Exception e ) {
				this.wakeLock = null;
				result = new PluginResult(PluginResult.Status.ERROR,"Can't acquire wake-lock - check your permissions!");
			}
		}
		else {
			result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION,"WakeLock already active - release first");
		}

		return result;
	}

	/**
	 * Release an active wake-lock
	 * @return PluginResult containing the status of the release process
	 */
	private PluginResult release() {
		PluginResult result = null;

		if( this.wakeLock != null ) {
			try {
				this.wakeLock.release();
				result = new PluginResult(PluginResult.Status.OK, "OK");
			}
			catch (Exception e) {
				result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION, "WakeLock already released");
			}

			this.wakeLock = null;
		}
		else {
			result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION, "No WakeLock active - acquire first");
		}

		return result;
	}
}
