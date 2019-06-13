package com.anokbook.Utils;


public class Log {

    public static void log(String data)
    {
        android.util.Log.v("AnokBook",data);
    }
    public static void log(String tag, String data)
    {
        android.util.Log.v(tag,data);
    }
}
