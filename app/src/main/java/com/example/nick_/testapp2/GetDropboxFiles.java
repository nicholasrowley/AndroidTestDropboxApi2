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

/**
 * Created by nicholas.rowley on 9/9/2016.
 */
public class GetDropboxFiles extends AsyncTask {

    private DbxClientV2 dbxClient;
    private Context context;
    private DbxDownloader<FileMetadata> downloader;
    private String url;

    GetDropboxFiles(DbxClientV2 dbxClient, Context context) {
        this.dbxClient = dbxClient;
        this.context = context;
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
            url = dbxClient.files().getTemporaryLink("/3 Fires Gone Out #_converted(1).mp4").getLink();
            Log.d("Create Link", "Success");
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Toast.makeText(context, "Link generated successfully", Toast.LENGTH_SHORT).show();
    }

    public String getTempURL() {
        return url;
    }
}
