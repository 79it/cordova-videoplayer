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
        super.onCreate(savedInstanceState);

        // Get the layout from video_main.xml
        setContentView(R.layout.activity_video);

        String videoSource;

        Bundle b = getIntent().getExtras();
        if (b!=null){
            videoSource = b.getString("videoSource");
        } else {
            videoSource = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        }

        Loading();
        Play(videoSource);
    }

    public void Play(String videoSource){
        if (mediaControls == null) {
            mediaControls = new MediaController(VideoPlayer.this);
        }

        // Find your VideoView in your video_main.xml layout
        myVideoView = (VideoView) findViewById(R.id.video_view);

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

    public void Loading(){
        progressDialog = new ProgressDialog(VideoPlayer.this);
        // Set progressbar title
        progressDialog.setTitle("Selectatrack On Demand");
        // Set progressbar message
        progressDialog.setMessage("Loading...");

        progressDialog.setCancelable(false);
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
