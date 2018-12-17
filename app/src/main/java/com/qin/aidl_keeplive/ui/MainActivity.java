package com.qin.aidl_keeplive.ui;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.qin.aidl_keeplive.IMusicManager;
import com.qin.aidl_keeplive.MusicCallBack;
import com.qin.aidl_keeplive.R;
import com.qin.aidl_keeplive.adapter.MusicAdapter;
import com.qin.aidl_keeplive.bean.Music;
import com.qin.aidl_keeplive.db.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private ArrayList<Music> mMusicList;
    private SQLiteDatabase mDb;
    private RecyclerView mRecyclerView;
    private MusicAdapter mMusicAdapter;
    private List<Music> mMusicListService;
    private MyMusicCallBack mMusicCallBack;
    private IMusicManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDataBase();
        initView();
        initData();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMusicAdapter = new MusicAdapter(this);
        mRecyclerView.setAdapter(mMusicAdapter);
    }

    public void get(View view) {
        bindService();
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setAction("com.qin.aidl_keeplive.action.IMusicManager.aidl");
        intent.setPackage("com.qin.aidl_keeplive");
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    private void initDataBase() {
        DataBaseHelper helper = new DataBaseHelper(this);
        mDb = helper.getReadableDatabase();
    }

    private void initData() {

        Cursor cursor = mDb.query("Music", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            Log.i(TAG, "--------从数据库读取-------");
            return;
        }
        Log.i(TAG, "--------新添加数据-------");
        mMusicList = new ArrayList<>();
        ContentValues values = new ContentValues();

        values.put("musicId", 0);
        values.put("musicName", "可不可以");
        values.put("musicAuthor", "张子豪");
        mDb.insert("Music", null, values);
        values.clear();

        values.put("musicId", 0);
        values.put("musicName", "光年之外");
        values.put("musicAuthor", "邓紫祺");
        mDb.insert("Music", null, values);
        values.clear();

        values.put("musicId", 0);
        values.put("musicName", "年少有为");
        values.put("musicAuthor", "李荣浩");
        mDb.insert("Music", null, values);
        values.clear();

        values.put("musicId", 0);
        values.put("musicName", "不染");
        values.put("musicAuthor", "毛不易");
        mDb.insert("Music", null, values);
        values.clear();

        values.put("musicId", 0);
        values.put("musicName", "可不可以");
        values.put("musicAuthor", "张子豪");
        mDb.insert("Music", null, values);
        values.clear();

        // mMusicAdapter.setData(mMusicList);
    }


    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "--------onServiceConnected---------");
            mManager = IMusicManager.Stub.asInterface(service);
//            Music music = new Music();
//            music.setMusicAuthor("客户端歌曲");
//            music.setMusicName("客户端");
            try {
                mMusicListService = mManager.getMusics();
                mMusicAdapter.setDataList(mMusicListService);
                mMusicCallBack = new MyMusicCallBack();
                mManager.registerMusicCallBack(mMusicCallBack);
                // 绑定代理对象
                mManager.asBinder().linkToDeath(mDeathRecipient,0 );
//                mManager.addMusics(music);
            } catch (RemoteException e) {
                Log.i(TAG, "--------RemoteException---------" + e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "--------onServiceDisconnected---------");
        }
    };

    private class MyMusicCallBack extends MusicCallBack.Stub {

        //运行在子线程
        @Override
        public void excute(final com.qin.aidl_keeplive.bean.Music music) throws RemoteException {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMusicAdapter.setData(music);
                }
            });

            Log.i(TAG, Thread.currentThread() + "--------远程心跳服务---------" + music.getMusicAuthor());
            mManager.addMusics(music);
        }
    }

    /**
     * 处理服务端异常死亡回调，当服务端crash时回调binderDied（）
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mManager.asBinder()!=null){
                // 解除代理
                mManager.asBinder().unlinkToDeath(this,0 );
                mManager = null;
            }
            // TODO handle net...处理操作，如重连
            bindService();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        if (mManager != null && mMusicCallBack.isBinderAlive()) {
            try {
                mManager.unRegisterMusicCallBack(mMusicCallBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
