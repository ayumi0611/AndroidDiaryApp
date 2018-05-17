package com.ayumitani.androiddiaryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormActivity extends AppCompatActivity {

    private long diaryId;

    private EditText titleText;
    private EditText bodyText;
    private TextView updatedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        titleText = (EditText) findViewById(R.id.titleText);
        bodyText = (EditText) findViewById(R.id.bodyText);
        updatedText = (TextView) findViewById(R.id.updatedText);

        Intent intent = getIntent();
        diaryId = intent.getLongExtra(MainActivity.EXTRA_MYID, 0L);

        if (diaryId == 0) {
            // new diary
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("New diary");
            }
            updatedText.setText("Updated: -------");
        } else {
            // show diary
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit diary");
            }
            Uri uri = ContentUris.withAppendedId(
                    DiaryContentProvider.CONTENT_URI,
                    diaryId
            );
            String[] projection = {
                    DiaryContract.Diary.COL_TITLE,
                    DiaryContract.Diary.COL_BODY,
                    DiaryContract.Diary.COL_UPDATED
            };
            Cursor c = getContentResolver().query(
                    uri,
                    projection,
                    DiaryContract.Diary._ID + " = ?",
                    new String[] { Long.toString(diaryId) },
                    null
            );
            c.moveToFirst();
            titleText.setText(
                    c.getString(c.getColumnIndex(DiaryContract.Diary.COL_TITLE))
            );
            bodyText.setText(
                    c.getString(c.getColumnIndex(DiaryContract.Diary.COL_BODY))
            );
            updatedText.setText(
                    "Updated: " +
                            c.getString(c.getColumnIndex(DiaryContract.Diary.COL_UPDATED))
            );
            c.close();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        if (diaryId == 0L) deleteItem.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    private void deleteDiary() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Diary")
                .setMessage("Are you sure?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = ContentUris.withAppendedId(
                                DiaryContentProvider.CONTENT_URI,
                                diaryId
                        );
                        getContentResolver().delete(
                                uri,
                                DiaryContract.Diary._ID + " = ?",
                                new String[] { Long.toString(diaryId)}
                        );
                        finish();
                    }
                })
                .show();

    }

    private void saveDiary() {
        String title = titleText.getText().toString().trim();
        String body = bodyText.getText().toString().trim();
        String updated =
                new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US)
                        .format(new Date());

        if (title.isEmpty()) {
            Toast.makeText(
                    FormActivity.this,
                    "Please enter title",
                    Toast.LENGTH_LONG
            ).show();
        } else {
            ContentValues values = new ContentValues();
            values.put(DiaryContract.Diary.COL_TITLE, title);
            values.put(DiaryContract.Diary.COL_BODY, body);
            values.put(DiaryContract.Diary.COL_UPDATED, updated);
            if (diaryId == 0L) {
                // new diary
                getContentResolver().insert(
                        DiaryContentProvider.CONTENT_URI,
                        values
                );
            } else {
                // updated diary
                Uri uri = ContentUris.withAppendedId(
                        DiaryContentProvider.CONTENT_URI,
                        diaryId
                );
                getContentResolver().update(
                        uri,
                        values,
                        DiaryContract.Diary._ID + " = ?",
                        new String[] { Long.toString(diaryId) }
                );
            }
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
           case R.id.action_delete:
               deleteDiary();
               break;
           case R.id.action_save:
               saveDiary();
               break;
       }
        return super.onOptionsItemSelected(item);
    }

}
