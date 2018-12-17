// IMusiceManager.aidl
package com.qin.aidl_keeplive;
import com.qin.aidl_keeplive.bean.Music;
import com.qin.aidl_keeplive.MusicCallBack;
// Declare any non-default types here with import statements

interface IMusicManager {
   void addMusics(in Music music);
   List<Music> getMusics();
   void registerMusicCallBack(MusicCallBack callBack);
   void unRegisterMusicCallBack(MusicCallBack callBack);
}
