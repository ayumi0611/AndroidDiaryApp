package com.ayumitani.androiddiaryapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DiaryContentProvider extends ContentProvider {

    public static final String AUTHORITY =
            "com.ayumitani.androiddiaryapp.DiaryContentProvider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + DiaryContract.Diary.TABLE_NAME);

    // UriMatcher
    private static final int DIARY = 1;
    private static final int DIARY_ITEM = 2;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, DiaryContract.Diary.TABLE_NAME, DIARY);
        uriMatcher.addURI(AUTHORITY, DiaryContract.Diary.TABLE_NAME+ "/#", DIARY_ITEM);
    }

    private DiaryOpenHelper diaryOpenHelper;


    public DiaryContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        diaryOpenHelper = new DiaryOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder
    ) {
        switch (uriMatcher.match(uri)) {
            case DIARY:
            case DIARY_ITEM:
                break;
                default:
                    throw new IllegalArgumentException("Invalid URI: " + uri);
        }
        SQLiteDatabase db = diaryOpenHelper.getReadableDatabase();
        Cursor c = db.query(
                DiaryContract.Diary.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return  c;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (uriMatcher.match(uri) != DIARY_ITEM) {
            throw new IllegalArgumentException("Invalid URI: " + uri);
        }
        SQLiteDatabase db = diaryOpenHelper.getWritableDatabase();
        int updatedCount = db.update(
                DiaryContract.Diary.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedCount;
    }
}
