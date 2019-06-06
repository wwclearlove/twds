package cdictv.twds.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cdictv.twds.App;
import cdictv.twds.bean.MenuDataBean;

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

    public static <T> void setDataList(String tag, List<T> datalist) {

        if (null == datalist || datalist.size() <= 0)
            return;
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);

        getSharedPreferences().edit().putString(tag, strJson).apply();
    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public static  <T> List<T> getDataList(String tag,Class<T[]> clazz) {
        List<T> datalist=new ArrayList<>();
        String strJson = getSharedPreferences().getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        T[] arr = gson.fromJson(strJson, clazz);
        return Arrays.asList(arr);

    }
}
