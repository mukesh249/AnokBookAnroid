package com.anokbook.Utils;

import com.anokbook.BuildConfig;

public class Constant {

    public static final int SERVICE_TYPE_GET = 1;
    public static final int SERVICE_TYPE_POST = 2;
    public static final int SERVICE_TYPE_PUT = 3;
    public static final int SERVICE_TYPE_DELETE = 4;


    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    public static final int GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    public static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 2;



    public static class Config {
        public static final boolean DEVELOPER_MODE = BuildConfig.DEBUG;
    }

}
