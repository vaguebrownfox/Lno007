package com.example.lno007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
// import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

// import com.example.lno007.data.ProfileDbHelper;
import com.example.lno007.data.ProfileContract.ProfileEntry;

public class CatalogActivity extends AppCompatActivity {

    // private ProfileDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // mDbHelper = new ProfileDbHelper(this);

        //displayDatabaseInfo();

    }

    private void displayDatabaseInfo() {
        // ProfileDbHelper mDbHelper = new ProfileDbHelper(this);

        // SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Cursor cursor = db.rawQuery("SELECT * FROM " + ProfileEntry.TABLE_NAME, null);

        String[] project = {
                ProfileEntry._ID,
                ProfileEntry.NAME,
                ProfileEntry.AGE,
                ProfileEntry.GENDER,
                ProfileEntry.HEIGHT,
                ProfileEntry.WEIGHT
        };

        /* Cursor cursor = db.query(
                ProfileEntry.TABLE_NAME,
                project,
                null,
                null,
                null,
                null,
                null
        );
        */
        try (Cursor cursor = getContentResolver().query(
                ProfileEntry.CONTENT_URI,
                project,
                null,
                null,
                null
        )) {
            TextView displayView = findViewById(R.id.text_view_profile);
            assert cursor != null;
            String displayMsg = getString(R.string.db_row_count_msg) + cursor.getCount() + "\n";
            displayView.setText(displayMsg);

            int idColumnIndex = cursor.getColumnIndex(ProfileEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProfileEntry.NAME);
            int heightColumnIndex = cursor.getColumnIndex(ProfileEntry.HEIGHT);
            int weightColumnIndex = cursor.getColumnIndex(ProfileEntry.WEIGHT);

            while (cursor.moveToNext()) {
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentHeight = cursor.getInt(heightColumnIndex);
                int currentWeight = cursor.getInt(weightColumnIndex);
                Log.v("Catalog", "" + currentId);
                displayView.append("\n" + currentId + ". " + currentName + " - " + currentHeight + " - " + currentWeight + "\n\n");
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data :
                insertProfile();
                displayDatabaseInfo();
                return true;
            case R.id.action_delete_all_entries :
                // nothing
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertProfile() {
        // SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ProfileEntry.NAME, "Han Solo");
        values.put(ProfileEntry.AGE, 37);
        values.put(ProfileEntry.GENDER, ProfileEntry.GENDER_MALE);
        values.put(ProfileEntry.HEIGHT, 182);
        values.put(ProfileEntry.WEIGHT, 81);

        // long newRowId = db.insert(ProfileEntry.TABLE_NAME, null, values);

        Uri nerRowUri = getContentResolver().insert(ProfileEntry.CONTENT_URI, values);

        Log.v("CatalogActivity", "New row URI: " + nerRowUri);
    }
}
