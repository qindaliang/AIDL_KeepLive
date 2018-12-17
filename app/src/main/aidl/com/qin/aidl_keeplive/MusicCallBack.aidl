// MusicCallBack.aidl
package com.qin.aidl_keeplive;
import com.qin.aidl_keeplive.bean.Music;
// Declare any non-default types here with import statements

interface MusicCallBack {
   void excute(in Music music);
}
