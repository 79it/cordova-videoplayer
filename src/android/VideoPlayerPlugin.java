package com.seventynineit.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class VideoPlayerPlugin extends CordovaPlugin {
    private static final String LOG_TAG   = "VideoPlayerPlugin";

    private CallbackContext callbackContext = null;
    private Activity activity = null;

    /**
     * Override the plugin initialise method and set the Activity as an
     * instance variable.
     */
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView)
    {
        Log.v(LOG_TAG, "Initialize Plugin");
        super.initialize(cordova, webView);

        Log.v(LOG_TAG, "Plugin Initialized");

        // Set the Activity.
        //Log.v(LOG_TAG, "Set activity");
        this.activity = cordova.getActivity();
        //Log.v(LOG_TAG, "Activity set");
    }

    /**
     * Here you can delegate any JavaScript methods. The "action" argument will contain the
     * name of the delegated method and the "args" will contain any arguments passed from the
     * JavaScript method.
     */

//    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext, Activity myActivity, boolean isNative) throws JSONException
//    {
//        Log.v(LOG_TAG, "Execute: " + action);
//        this.callbackContext = callbackContext;
//
//        Log.v(LOG_TAG, callbackContext.getCallbackId() + ": " + action);
//
//
//        //CordovaResourceApi resourceApi = webView.getResourceApi();
//        String target = args.getString(0);
//        Log.v(LOG_TAG, action + ": " + target);
//        //final JSONObject options = args.getJSONObject(1);
////
////        String fileUriStr;
////        try {
////            Uri targetUri = resourceApi.remapUri(Uri.parse(target));
////            fileUriStr = targetUri.toString();
////        } catch (IllegalArgumentException e) {
////            fileUriStr = target;
////        }
//
//        //Log.v(LOG_TAG, target);
//
//        //final String path = stripFileProtocol(fileUriStr);
//
//        if (action.equals("play"))
//        {
//            if (isNative){
//                Intent myIntent = new Intent(myActivity, VideoPlayer.class);
//                myIntent.putExtra("videoSource", target); //Optional parameters
//                myActivity.startActivity(myIntent);
//            } else {
//                Context myContext=myActivity.getApplicationContext();
//                Intent intent=new Intent(myContext,VideoPlayer.class);
//                intent.putExtra("videoSource", target); //Optional parameters
//
//                myContext.startActivity(intent);
//            }
//        }
//        else
//        {
//            return false;
//        }
//
//        return true;
//    }

//    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException
//    {
//        Log.v(LOG_TAG, "Execute: " + action);
//        return execute(action, args, callbackContext, cordova.getActivity(), false);
//    }

    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException
    {
        Log.v(LOG_TAG, "Execute: " + action);
        this.callbackContext = callbackContext;

        Log.v(LOG_TAG, callbackContext.getCallbackId() + ": " + action);

        final JSONObject options = args.getJSONObject(1);
        final String target = args.getString(0);
        final String loadingTitle = options.getString("loadingTitle");
        final String loadingBody = options.getString("loadingBody");
        
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
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Context context = cordova.getActivity().getApplicationContext();
                    //Uri uri = Uri.parse("content://com.seventynineit.videoplayer");
                    //Intent intent = new Intent("com.seventynineit.videoplayer.VideoPlayer", uri);


                    Intent intent = new Intent(context, VideoPlayer.class);
                    intent.putExtra("videoSource", target);
                    intent.putExtra("loadingTitle", loadingTitle);
                    intent.putExtra("loadingBody", loadingBody);

                    Log.v(LOG_TAG, "Intent: " + intent.toUri(1));

//                    List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
//                    Log.v(LOG_TAG, "Matching activities: " + list.size());

                    PackageManager pm = context.getPackageManager();
                    List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);

                    final  int  count = list.size();
                    Log.v(LOG_TAG, "Matching activities: " + count);
                    for  (int  i = 0; i < count; i++) {
                        final ResolveInfo info = list.get(i);
                        final CharSequence labelSeq = info.loadLabel(pm);
                        String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;
                        Log.v(LOG_TAG, i + ": " + label);
                    }

                    cordova.getActivity().startActivity(intent);
                }
            });

//            Context myContext=cordova.getActivity().getApplicationContext();
//            Intent intent=new Intent(myContext,VideoPlayer.class);
//            //intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            intent.putExtra("videoSource", target); //Optional parameters
//
//            myContext.startActivity(intent);

        }
        else
        {
            return false;
        }

        return true;
    }

    //private void callNativeMethod(String videoSource)
    //{
    //    // Here we simply call the method from the Activity.
    //    this.activity.Play(videoSource);
    //}

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
