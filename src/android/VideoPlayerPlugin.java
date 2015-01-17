package com.seventynineit.videoplayer;

import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoPlayerPlugin extends CordovaPlugin {
    private static final String LOG_TAG   = "VideoPlayerPlugin";

    private CallbackContext callbackContext = null;
    private VideoPlayer activity = null;

    /**
     * Override the plugin initialise method and set the Activity as an
     * instance variable.
     */
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView)
    {
        super.initialize(cordova, webView);

        // Set the Activity.
        this.activity = (VideoPlayer) cordova.getActivity();
    }

    /**
     * Here you can delegate any JavaScript methods. The "action" argument will contain the
     * name of the delegated method and the "args" will contain any arguments passed from the
     * JavaScript method.
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException
    {
        this.callbackContext = callbackContext;

        Log.d(LOG_TAG, callbackContext.getCallbackId() + ": " + action);

        CordovaResourceApi resourceApi = webView.getResourceApi();
        String target = args.getString(0);
        final JSONObject options = args.getJSONObject(1);

        String fileUriStr;
        try {
            Uri targetUri = resourceApi.remapUri(Uri.parse(target));
            fileUriStr = targetUri.toString();
        } catch (IllegalArgumentException e) {
            fileUriStr = target;
        }

        Log.v(LOG_TAG, fileUriStr);

        final String path = stripFileProtocol(fileUriStr);


        if (action.equals("play"))
        {
            this.callNativeMethod(path);
        }
        else
        {
            return false;
        }

        return true;
    }

    private void callNativeMethod(String videoSource)
    {
        // Here we simply call the method from the Activity.
        this.activity.Play(videoSource);
    }

    /**
     * Removes the "file://" prefix from the given URI string, if applicable.
     * If the given URI string doesn't have a "file://" prefix, it is returned unchanged.
     *
     * @param uriString the URI string to operate on
     * @return a path without the "file://" prefix
     */
    public static String stripFileProtocol(String uriString) {
        if (uriString.startsWith("file://")) {
            return Uri.parse(uriString).getPath();
        }
        return uriString;
    }
}