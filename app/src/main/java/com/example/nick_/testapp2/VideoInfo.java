package com.example.nick_.testapp2;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by nicholas.rowley on 9/12/2016.
 */
public class VideoInfo implements Serializable{
    private String name;
    private String tempUrl;

    VideoInfo(String vidName, String temporaryUrl)
    {
        name = vidName;
        tempUrl = temporaryUrl;
    }

    public String getName() {
        return name;
    }

    public String getTempUrl() {
        return tempUrl;
    }

}
