package com.qin.aidl_keeplive.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.qin.aidl_keeplive.IMusicManager;
import com.qin.aidl_keeplive.MusicCallBack;
import com.qin.aidl_keeplive.bean.Music;
import com.qin.aidl_keeplive.db.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create by qindl
 * on 2018/12/17
 */
public class MusicService extends Service {

    private ArrayList<Music> mMusicList;
    private AtomicBoolean serviceStop = new AtomicBoolean(false);
    private SQLiteDatabase mDb;
    private RemoteCallbackList<MusicCallBack> mCallbackList = new RemoteCallbackList<>();

    @Override
    public void onCreate() {
        initDataBase();
        queryMusicLists();
        startTask();
    }

    private void startTask() {
        ConnectTask connectTask = new ConnectTask();
        new Thread(connectTask).start();
    }

    private void queryMusicLists() {
        mMusicList = new ArrayList<>();
        Cursor cursor = mDb.query("Music", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int musicId = cursor.getInt(cursor.getColumnIndex("musicId"));
            String musicName = cursor.getString(cursor.getColumnIndex("musicName"));
            String musicAuthor = cursor.getString(cursor.getColumnIndex("musicAuthor"));
            Music music = new Music();
            music.setMusicId(musicId);
            music.setMusicName(musicName);
            music.setMusicAuthor(musicAuthor);
            mMusicList.add(music);
        }
        cursor.close();
    }

    private void initDataBase() {
        DataBaseHelper helper = new DataBaseHelper(this);
        mDb = helper.getReadableDatabase();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceStop.set(true);
    }

    IBinder mBinder = new IMusicManager.Stub() {
        @Override
        public void addMusics(Music music) throws RemoteException {
            addMusicLists(music);
        }

        @Override
        public List<Music> getMusics() throws RemoteException {
            return mMusicList;
        }

        @Override
        public void registerMusicCallBack(MusicCallBack callBack) throws RemoteException {
            mCallbackList.register(callBack);
        }

        @Override
        public void unRegisterMusicCallBack(MusicCallBack callBack) throws RemoteException {
            mCallbackList.unregister(callBack);
        }

        // 校验客户端权限是否正确
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());

            if (packages == null || packages.length < 0) {
                return false;
            }
            String packageName = packages[0];
            if (packageName == null || !packageName.startsWith("com.qin.aidl_keeplive")){
                Log.i("TAG", "onTransact: 权限拒绝"+packageName);
                return false;
            }
            return super.onTransact(code, data, reply, flags);
        }
    };

    private void addMusicLists(Music music) {
        ContentValues values = new ContentValues();
        values.put("musicId", music.getMusicId());
        values.put("musicName", music.getMusicName());
        values.put("musicAuthor", music.getMusicAuthor());
        mDb.insert("Music", null, values);
    }

    class ConnectTask implements Runnable {

        @Override
        public void run() {
            while (!serviceStop.get()) {
                SystemClock.sleep(3000);
                int broadcastItem = mCallbackList.beginBroadcast();
                Log.i("TAG", "-----------broadcastItem------------" + broadcastItem);
                for (int i = 0; i < broadcastItem; i++) {
                    MusicCallBack callBack = mCallbackList.getBroadcastItem(i);
                    if (callBack == null) {
                        return;
                    }
                    try {

                        Music music = new Music();
                        music.setMusicAuthor("周杰伦" + new Random().nextInt(1000));
                        music.setMusicName("歌曲名" + new Random().nextInt(1000));

                        callBack.excute(music);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Log.i("TAG", "-----------RemoteException------------" + e.getMessage());
                    }
                }
                mCallbackList.finishBroadcast();
            }
        }
    }
}
