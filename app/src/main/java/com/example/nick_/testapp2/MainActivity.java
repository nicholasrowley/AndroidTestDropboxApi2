package com.example.nick_.testapp2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 101;
    private String ACCESS_TOKEN;
    private GetDropboxFiles thumbnail;
    private TextView results;
    private List<VideoInfo> videoResults;
    private ImageView iview;
    private Bitmap ithumbnail;
    private Uri uriImage;
    private Button button;
    private LinearLayout lview;
    private List<Button> galleryLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = new Button(this);
        button.setText("Hello World");
        button.setId(0);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        lview = (LinearLayout) findViewById(R.id.gallery);
        lview.addView(button);

        galleryLinks = new ArrayList<Button>();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        results = (TextView) findViewById(R.id.createLinkResults);
        videoResults = new ArrayList<VideoInfo>();

        if (!tokenExists()) {
            //No token
            //Back to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        ACCESS_TOKEN = retrieveAccessToken();
        getUserAccount();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
    }

    protected void getUserAccount() {
        if (ACCESS_TOKEN == null)return;
        new UserAccountTask(DropboxClient.getClient(ACCESS_TOKEN), new UserAccountTask.TaskDelegate() {
            @Override
            public void onAccountReceived(FullAccount account) {
                //Print account's info
                Log.d("User", account.getEmail());
                Log.d("User", account.getName().getDisplayName());
                Log.d("User", account.getAccountType().name());
                updateUI(account);
            }
            @Override
            public void onError(Exception error) {
                Log.d("User", "Error receiving account details.");
            }
        }).execute();
    }

    private void updateUI(FullAccount account) {
        ImageView profile = (ImageView) findViewById(R.id.imageView);
        TextView name = (TextView) findViewById(R.id.name_textView);
        TextView email = (TextView) findViewById(R.id.email_textView);

        name.setText(account.getName().getDisplayName());
        email.setText(account.getEmail());
        Picasso.with(this)
                .load(account.getProfilePhotoUrl())
                .resize(200, 200)
                .into(profile);
    }

    private void upload() {
        if (ACCESS_TOKEN == null)return;
        //Select image to upload
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,
                "Upload to Dropbox"), IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;
        // Check which request we're responding to
        if (requestCode == IMAGE_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //Image URI received
                File file = new File(URI_to_Path.getPath(getApplication(), data.getData()));
                if (file != null) {
                    //Initialize UploadTask
                    new UploadTask(DropboxClient.getClient(ACCESS_TOKEN), file, getApplicationContext()).execute();
                }
            }
        }
    }

    private boolean tokenExists() {
        SharedPreferences prefs = getSharedPreferences("com.example.nick_.testapp2", Context.MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        return accessToken != null;
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

    public void onClick (View v) {
        switch (v.getId()){
            case R.id.showBtn:
                //ibutton.setImageBitmap(ithumbnail);
                //ibutton.setImageURI(uriImage);
                Picasso.with(getApplicationContext())
                        .load(videoResults.get(1).getTempUrl())
                        .into(iview);
                break;
            case R.id.showUrlBtn:
                String result = "";

                for (VideoInfo link : videoResults)
                {
                    result = result + "<Space>" + link.getTempUrl();
                }
                results.setText(result);

                //generate a vid image on a button

                //ithumbnail = ThumbnailUtils.createVideoThumbnail(videoResults.get(0), MediaStore.Images.Thumbnails.MICRO_KIND);
                //uriImage = Uri.parse(videoResults.get(0));
                break;
            case R.id.vidScreenBtn:
                //Proceed to View_Video
                results.setText(videoResults.get(0).getTempUrl());
                Intent intent = new Intent(MainActivity.this, View_Video.class);
                intent.putExtra("videoIndex", videoResults.get(0));
                startActivity(intent);
                break;
            case R.id.gVidLinksBtn:
                thumbnail = new GetDropboxFiles(DropboxClient.getClient(ACCESS_TOKEN), getApplicationContext());
                thumbnail.execute();
                videoResults = thumbnail.getVideoInfos();
                break;
            case R.id.gGalleryLinks:
                int i = 0;
                for (VideoInfo link : videoResults) {
                    galleryLinks.add(new Button(this));
                    galleryLinks.get(i).setText(link.getName());
                    galleryLinks.get(i).setId(i);
                    galleryLinks.get(i).setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    lview.addView(galleryLinks.get(i));
                    galleryLinks.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Proceed to View_Video
                            results.setText(videoResults.get(view.getId()).getTempUrl());
                            Intent intent = new Intent(MainActivity.this, View_Video.class);
                            intent.putExtra("videoIndex", videoResults.get(view.getId()));
                            startActivity(intent);
                        }
                    });
                    i++;
                }
                break;
        }
    }



}
