package com.anokbook.Api;


/**
 * Created by Advosoft2 on 6/28/2017.
 */
public class WebUrls {
    public static final String BASE_URL = "http://escortapp.co.in/anokbook/api";
    public static final String IMG_BASE_URL = "http://escortapp.co.in/anokbook";
//    public static final String BASE_URL = "http://192.168.1.52:8080/anokbook/api";
//    public static final String IMG_BASE_URL = "http://192.168.1.52:8080/anokbook";

    public final static String Image_Url = "/public/uploads/user/";
    public final static String Book_Image_Url= "/public/uploads/books/";
    public final static String register_api = "register";

    //Google api for Tracking
    public static final String baseURL = "https://maps.googleapis.com";

    public static ApiConfig getGoogleAPI(){
        return RetrofitClient.getRetrofitClient(baseURL).create(ApiConfig.class);
    }

}
