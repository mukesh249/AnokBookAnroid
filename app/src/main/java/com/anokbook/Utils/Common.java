package com.anokbook.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class Common {



    public static String unicodeEscaped(char ch) {
        if (ch < 0x10) {
            return "\\u000" + Integer.toHexString(ch);
        } else if (ch < 0x100) {
            return "\\u00" + Integer.toHexString(ch);
        } else if (ch < 0x1000) {
            return "\\u0" + Integer.toHexString(ch);
        }
        return "\\u" + Integer.toHexString(ch);
    }

    public static String unicodeEscaped(Character ch) {
        if (ch == null) {
            return null;
        }
        return unicodeEscaped(ch.charValue());

    }


    /*********************
     * set shared preferences
     **************************/
    public static void SetPreferences(Context con, String key, String value) {
        // save the data
        SharedPreferences preferences = con.getSharedPreferences("prefs_login", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /******************
     * get shared preferences
     *******************/
    public static String getPreferences(Context con, String key) {
        SharedPreferences sharedPreferences = con.getSharedPreferences("prefs_login", 0);
        String value = sharedPreferences.getString(key, "0");
        return value;
    }

    /**********************
     * set shared preferences in int
     *********************************/
    public static void SetPreferencesInteger(Context con, String key, int value) {
        // save the data
        SharedPreferences preferences = con.getSharedPreferences("prefs_login", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /*********************
     * get shared preferences in int
     ***********************/
    public static int getPreferencesInteger(Context con, String key) {
        SharedPreferences sharedPreferences = con.getSharedPreferences("prefs_login", 0);
        int value = sharedPreferences.getInt(key, 0);
        return value;

    }

    /**********************
     * set shared preferences in Long
     *********************************/
    public static void SetPreferencesLong(Context con, String key, long value) {
        // save the data
        SharedPreferences preferences = con.getSharedPreferences("prefs_login", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /*********************
     * get shared preferences in int
     ***********************/
    public static long getPreferencesLong(Context con, String key) {
        SharedPreferences sharedPreferences = con.getSharedPreferences("prefs_login", 0);
        long value = sharedPreferences.getLong(key, 0);
        return value;

    }

    /*********************
     * show toast message
     ***********************/
    public static void showToast(Context context, String message) {
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
    }

//	/******* do animated activity ********/
//	public static void doAnim(Activity act, String flag) {
//		if (flag.equals("left")) {
//			act.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//		} else if (flag.equals("right")) {
//			act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//		} else if (flag.equals("no")) {
//			act.overridePendingTransition(0, 0);
//		}
//	}
//
//	/********************** start activity **************************/
//	public static void doStartActivityForFinishWithourFinish(Activity act, Class cls, String anim_left_right_no) {
//		Intent intent = new Intent(act, cls);
//		act.startActivity(intent);
//		doAnim(act, anim_left_right_no);
//	}
//
//	/**********************
//	 * start activity for finish
//	 **************************/
//	public static void doStartActivityForFinish(Activity act, Class cls, String anim_left_right_no) {
//		Intent intent = new Intent(act, cls);
//		act.startActivity(intent);
//		act.finish();
//		doAnim(act, anim_left_right_no);
//	}
//
//	/**********************
//	 * start activity for finish and send value like Class
//	 **************************/
//	public static void doStartActivityForFinishAndValue(Activity act, Class cls, String anim_left_right_no, String key,
//			String classname, String hash) {
//		Intent intent = new Intent(act, cls);
//		intent.putExtra(key, classname);
//		intent.putExtra("HASH", hash);
//		act.startActivity(intent);
//		act.finish();
//		doAnim(act, anim_left_right_no);
//
//	}

    /**********************
     * Hide keyboard
     **************************/
    public static void hideKeyboard(AppCompatActivity _activity) {
        InputMethodManager inputManager = (InputMethodManager) _activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(_activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /******************
     * Convert input stream into string
     **********************/

    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    public static Set<String> getPreferencesSet(Context con, String key) {
        SharedPreferences sharedPreferences = con.getSharedPreferences("prefs_login", 0);
        Set<String> value = sharedPreferences.getStringSet(key, new HashSet<String>());
        return value;

    }


    public static void SetPreferencesSet(Context con, String key, HashSet<String> value) {
        // save the data
        SharedPreferences preferences = con.getSharedPreferences("prefs_login", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }


    public static SharedPreferences.Editor getEditor(Context con) {

        SharedPreferences sharedPreferences = con.getSharedPreferences("prefs_login", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return editor;

    }

    /**
     * This method is used for get the connectivity status
     *
     * @return
     */
    public static boolean getConnectivityStatus(Activity activity) {
        ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info != null)
            if (info.isConnected()) {
                return true;
            } else {
                return false;
            }
        else
            return false;
    }

    public File String_to_File(String img_url) {
        File casted_image = null;
        try {
            File rootSdDirectory = Environment.getExternalStorageDirectory();

            casted_image = new File(rootSdDirectory, "attachment.jpg");
            if (casted_image.exists()) {
                casted_image.delete();
            }
            casted_image.createNewFile();

            FileOutputStream fos = new FileOutputStream(casted_image);

            URL url = new URL(img_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            InputStream in = connection.getInputStream();

            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = in.read(buffer)) > 0) {
                fos.write(buffer, 0, size);
            }
            fos.close();
            return casted_image;

        } catch (Exception e) {

            System.out.print(e);

        }
        return casted_image;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    } // Author: silentnuke

}

