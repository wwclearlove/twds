package cdictv.twds.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cdictv.twds.App;

public class Sputils {
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences getSharedPreferences(){
        if(mSharedPreferences==null){
            mSharedPreferences= PreferenceManager.getDefaultSharedPreferences(App.INSTANCE);
        }
        return mSharedPreferences;
    }
    public static void putString(String key,String value){
        getSharedPreferences().edit().putString(key,value).apply();
    }
    public static void putInt(String key,int value){
        getSharedPreferences().edit().putInt(key,value).apply();
    }
    public static void putBoolean(String key,Boolean value){
        getSharedPreferences().edit().putBoolean(key,value).apply();
    }
    public static String getString(String key){
      return   getSharedPreferences().getString(key,null);
    }
    public static int getInt(String key){
        return   getSharedPreferences().getInt(key,0);
    }
    public static Boolean getBolean(String key,Boolean defValue){
        return   getSharedPreferences().getBoolean(key,defValue);
    }
    public static void removeKey(String key){
        getSharedPreferences().edit().remove(key).apply();
    }
}
