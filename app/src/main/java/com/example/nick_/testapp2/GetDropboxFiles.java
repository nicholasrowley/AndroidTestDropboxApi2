package com.example.nick_.testapp2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicholas.rowley on 9/9/2016.
 */
public class GetDropboxFiles extends AsyncTask {

    private DbxClientV2 dbxClient;
    private Context context;
    private DbxDownloader<FileMetadata> downloader;
    private List<VideoInfo> videoInfos;
    private List<Metadata> folderContents;
    private List<String> videoUris;

    GetDropboxFiles(DbxClientV2 dbxClient, Context context) {
        this.dbxClient = dbxClient;
        this.context = context;
        folderContents = new ArrayList<Metadata>();
        videoInfos = new ArrayList<VideoInfo>();
        videoUris = new ArrayList<String>();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            // get a thumbnail from to Dropbox
            /*downloader =
                    dbxClient.files().getThumbnailBuilder("/3 Fires Gone Out #_converted(1).mp4")
                            .withFormat(ThumbnailFormat.JPEG)
                            .withSize(ThumbnailSize.W1024H768)
                            .start();*/

            //contains metadata for all contents in the folder such as the URI links to each file.
            folderContents = dbxClient.files().listFolder("/Videos/").getEntries();

            //create temporary links for each file in the folder
            for (Metadata fileItem : folderContents)
            {
                //to store temporary urls into a list
                videoInfos.add( new VideoInfo(fileItem.getName(), dbxClient.files().getTemporaryLink(fileItem.getPathLower()).getLink()));
                //to store uris into a list
                videoUris.add(fileItem.getPathLower());
            }
            Log.d("Create Links", "Success");
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Toast.makeText(context, "Links generated successfully", Toast.LENGTH_SHORT).show();
    }

    public List<VideoInfo> getVideoInfos() {
        return videoInfos;
    }

    public List<String> getVideoUris() {
        return videoUris;
    }
}
