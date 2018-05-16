package com.ayumitani.androiddiaryapp;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class FormActivity extends AppCompatActivity {

    private long diaryId;

    private EditText titleText;
    private EditText bodyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        titleText = (EditText) findViewById(R.id.titleText);
        bodyText = (EditText) findViewById(R.id.bodyText);

        Intent intent = getIntent();
        diaryId = intent.getLongExtra(MainActivity.EXTRA_MYID, 0L);

        if (diaryId == 0) {
            // new diary
        } else {
            // show diary
            Uri uri = ContentUris.withAppendedId(
                    DiaryContentProvider.CONTENT_URI,
                    diaryId
            );
            String[] projection = {
                    DiaryContract.Diary.COL_TITLE,
                    DiaryContract.Diary.COL_BODY
            };
            Cursor c = getContentResolver().query(
                    uri,
                    projection,
                    DiaryContract.Diary._ID + " = ?",
                    new String[] { Long.toString(diaryId) },
                    null
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
