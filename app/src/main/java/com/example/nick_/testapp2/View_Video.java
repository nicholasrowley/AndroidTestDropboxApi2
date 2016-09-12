package com.example.nick_.testapp2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

public class View_Video extends AppCompatActivity {

    private VideoInfo videoInfo;

    private static ProgressDialog progressDialog;
    String videourl;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        TextView tview = (TextView) findViewById(R.id.resultText);
        if (extras != null) {
            videoInfo = (VideoInfo) extras.getSerializable("videoIndex");
            tview.setText(videoInfo.getTempUrl());
        }

        //vid view imp onCreate code
        videoView = (VideoView) findViewById(R.id.videoView);



        //progress dialog shows when video is buffering
        progressDialog = ProgressDialog.show(View_Video.this, "", "Buffering video...", true);
        progressDialog.setCancelable(true);

        PlayVideo();
    }

    private String retrieveAccessToken() {
        //check if ACCESS_TOKEN is stored on previous app launches
        SharedPreferences prefs = getSharedPreferences("com.example.nick_.testapp2", Context.MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        if (accessToken == null) {
            Log.d("AccessToken Status", "No token found");
            return null;
        } else {
            //accessToken already exists
            Log.d("AccessToken Status", "Token exists");
            return accessToken;
        }
    }

    //vid view imp play method
    private void PlayVideo()
    {
        try
        {
            getWindow().setFormat(PixelFormat.TRANSLUCENT);
            MediaController mediaController = new MediaController(View_Video.this);
            mediaController.setAnchorView(videoView);

            Uri video = Uri.parse(videoInfo.getTempUrl());
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {

                public void onPrepared(MediaPlayer mp)
                {
                    progressDialog.dismiss();
                    videoView.start();
                }
            });


        }
        catch(Exception e)
        {
            progressDialog.dismiss();
            System.out.println("Video Play Error :"+e.toString());
            finish();
        }

    }

    public void onClickReturnMain(View v) {
        startActivity(new Intent(View_Video.this, MainActivity.class));
    }

}
