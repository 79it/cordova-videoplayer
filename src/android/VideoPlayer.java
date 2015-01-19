package com.seventynineit.videoplayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer extends Activity
{

    private static final String LOG_TAG   = "VideoPlayer";
    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreate Video Player");
        super.onCreate(savedInstanceState);

        Log.v(LOG_TAG, "setContentView");
        // Get the layout from video_main.xml
        setContentView(getResources().getIdentifier("activity_video", "layout", getPackageName()));
        //setContentView(R.layout.activity_video);

        String videoSource = "";
        String loadingTitle = "";
        String loadingBody = "";

        Log.v(LOG_TAG, "getIntent().getExtras()");
        Bundle b = getIntent().getExtras();
        if (b!=null){
            videoSource = b.getString("videoSource");
            loadingTitle = b.getString("loadingTitle");
            loadingBody = b.getString("loadingBody");
        }

        Log.v(LOG_TAG, "videoSource: " + videoSource);

        if (videoSource!=null){
            Loading(loadingTitle, loadingBody, false);
            Play(videoSource);
        } else {
            loadingTitle = "No media";
            loadingBody = "No media specified";
            Loading(loadingTitle, loadingBody, true);
        }
    }

    public void Play(String videoSource){
        Log.v(LOG_TAG, "Playing...");
        if (mediaControls == null) {
            mediaControls = new MediaController(VideoPlayer.this);
        }

        // Find your VideoView in your video_main.xml layout
        //myVideoView = (VideoView) findViewById(R.id.video_view);
        myVideoView = (VideoView) findViewById(getResources().getIdentifier("video_view", "id", getPackageName()));

        // Create a progressbar
        try {
            myVideoView.setMediaController(mediaControls);
            //myVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.kitkat));
            myVideoView.setVideoURI(Uri.parse(videoSource));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        myVideoView.requestFocus();
        myVideoView.setOnPreparedListener(new OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    myVideoView.pause();
                }
            }
        });

        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
    }

    public void Loading(String loadingTitle, String loadingBody, Boolean canCancel){
        Log.v(LOG_TAG, "Loading...");
        progressDialog = new ProgressDialog(VideoPlayer.this);
        // Set progressbar title
        progressDialog.setTitle(loadingTitle);
        // Set progressbar message
        progressDialog.setMessage(loadingBody);

        progressDialog.setCancelable(canCancel);
        // Show progressbar
        progressDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("Position");
        myVideoView.seekTo(position);
        myVideoView.resume();
    }



//    @Override
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//        Log.e(LOG_TAG, "MediaPlayer.onError(" + what + ", " + extra + ")");
//        if(mp.isPlaying()) {
//            mp.stop();
//        }
//        mp.release();
//
//        return false;
//    }
//
////    @Override
////    public void onPrepared(MediaPlayer mp) {
////        mp.start();
////    }
//
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        Log.d(LOG_TAG, "MediaPlayer completed");
//        mp.release();
//
//    }

//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        Log.d(LOG_TAG, "Dialog dismissed");
//        if (callbackContext != null) {
//            PluginResult result = new PluginResult(PluginResult.Status.OK);
//            result.setKeepCallback(false); // release status callback in JS side
//            callbackContext.sendPluginResult(result);
//            callbackContext = null;
//        }
//    }
}
