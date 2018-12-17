package com.qin.aidl_keeplive.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by qindl
 * on 2018/12/17
 */
public class Music implements Parcelable {
    private String musicName;
    private int musicId;

    public Music() {
    }

    protected Music(Parcel in) {
        musicName = in.readString();
        musicId = in.readInt();
        musicAuthor = in.readString();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getMusicAuthor() {
        return musicAuthor;
    }

    public void setMusicAuthor(String musicAuthor) {
        this.musicAuthor = musicAuthor;
    }

    private String musicAuthor;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(musicName);
        dest.writeInt(musicId);
        dest.writeString(musicAuthor);
    }
}
