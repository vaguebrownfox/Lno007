package com.example.lno007.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ProfileContract {

    private ProfileContract() { }

    public static final class ProfileEntry implements BaseColumns {

        final static  String TABLE_NAME = "profiles";

        public final static  String _ID = BaseColumns._ID;
        public final static  String NAME = "name";
        public final static  String AGE = "age";
        public final static  String GENDER = "gender";
        public final static  String HEIGHT = "height";
        public final static  String WEIGHT = "weight";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        // Uri bois
        static final String CONTENT_AUTHORITY = "com.example.lno007";
        static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        static final String PATH_PROFILES = "profile";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PROFILES);

        static boolean idGenderValid(Integer gender) {
            return gender != GENDER_UNKNOWN && gender != GENDER_MALE && gender != GENDER_FEMALE;
        }

        // MIME types
        public static final String CONTENT_LIST_TYPES =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILES;
        public static final String CONTENT_ITEM_TYPES =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILES;
    }
}
