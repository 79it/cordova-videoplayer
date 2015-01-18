package com.seventynineit.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;

import org.json.JSONException;

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
        Log.v(LOG_TAG, "Initialize Plugin");
        super.initialize(cordova, webView);

        // Set the Activity.
        Log.v(LOG_TAG, "Set activity");
        this.activity = (VideoPlayer) cordova.getActivity();
    }

    /**
     * Here you can delegate any JavaScript methods. The "action" argument will contain the
     * name of the delegated method and the "args" will contain any arguments passed from the
     * JavaScript method.
     */

    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext, Activity myActivity, boolean isNative) throws JSONException
    {
        Log.v(LOG_TAG, "Execute: " + action);
        this.callbackContext = callbackContext;

        Log.v(LOG_TAG, callbackContext.getCallbackId() + ": " + action);


        //CordovaResourceApi resourceApi = webView.getResourceApi();
        String target = args.getString(0);
        Log.v(LOG_TAG, action + ": " + target);
        //final JSONObject options = args.getJSONObject(1);
//
//        String fileUriStr;
//        try {
//            Uri targetUri = resourceApi.remapUri(Uri.parse(target));
//            fileUriStr = targetUri.toString();
//        } catch (IllegalArgumentException e) {
//            fileUriStr = target;
//        }

        //Log.v(LOG_TAG, target);

        //final String path = stripFileProtocol(fileUriStr);

        if (action.equals("play"))
        {
            if (isNative){
                Intent myIntent = new Intent(myActivity, VideoPlayer.class);
                myIntent.putExtra("videoSource", target); //Optional parameters
                myActivity.startActivity(myIntent);
            } else {
                Context myContext=myActivity.getApplicationContext();
                Intent intent=new Intent(myContext,VideoPlayer.class);
                intent.putExtra("videoSource", target); //Optional parameters

                myContext.startActivity(intent);
            }
        }
        else
        {
            return false;
        }

        return true;
    }

    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException
    {
        return execute(action, args, callbackContext, cordova.getActivity(), false);
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
