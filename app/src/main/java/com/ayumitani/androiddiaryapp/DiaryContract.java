package com.ayumitani.androiddiaryapp;

import android.provider.BaseColumns;

public final class DiaryContract {
    public DiaryContract(){}

    public static abstract class Diary implements BaseColumns {

        public static final String TABLE_NAME = "diaries";
        public static final String COL_TITLE = "title";
        public static final String COL_BODY = "body";
        public static final String COL_CREATE = "created";
        public static final String COL_UPDATED = "updated";


    }
}
