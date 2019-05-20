package com.anokbook.Common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SharedPrefManager {

    private static final String SHARE_PREF_NAME = "fcmsharedprefdemo";
    private static final String KEY_ACCESS_TOKEN = "fcmtoken";
    private static String islagChange;

    private static Context mCtx;
    private static SharedPrefManager mInstance;
    public static SharedPreferences sp;
    private SharedPrefManager(Context context) {
        mCtx = context;
        sp = mCtx.getSharedPreferences("BabbaBazri", Context.MODE_PRIVATE);
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null)
            mInstance = new SharedPrefManager(context);
        return mInstance;
    }

    public static void setUserID(String type ,String userId) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type,userId);
        editor.commit();
    }
    public static String getUserID(String type) {
        String e = sp.getString(type,"");
        return e;
    }

    public static void setUserMobile(String type ,String userId) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type,userId);
        editor.commit();
    }
    public static String getUserMobile(String type) {
        String e = sp.getString(type,"");
        return e;
    }

    public static void setUserEmail(String type ,String userId) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type,userId);
        editor.commit();
    }
    public static String getUserEmail(String type) {
        String e = sp.getString(type,"");
        return e;
    }

    public static void setUserName(String type ,String userId) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type,userId);
        editor.commit();
    }
    public static String getUserName(String type) {
        String e = sp.getString(type,"");
        return e;
    }
    public void storeIsLoggedIn(Boolean isLoggedIn) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putBoolean("key", isLoggedIn);
        editor.commit();
    }

    public boolean getIsLoggedIn(boolean default_value) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("login", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getBoolean("key", default_value);
    }

    public void hideSoftKeyBoard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            //check if no view has focus.
            View v = activity.getCurrentFocus();
            if (v == null)
                return;
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void showMessage(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
    public static boolean isValidEmail(String email){
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public Boolean CheckPassword(String password) {
        String[] re = {"[a-z]", "[?=.*[@#$%!\\-_?&])(?=\\\\S+$]"};
        for (String r : re) {
            if (!Pattern.compile(r).matcher(password).find()) return false;
        }
        return true;
    }



}
