package com.example.lno007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lno007.data.ProfileContract;

public class EditorActivity extends AppCompatActivity {

    // private ProfileDbHelper mDbHelper;

    private EditText mNameEditText;
    private EditText mAgeEditText;
    private EditText mHeightEditText;
    private EditText mWeightEditText;

    private Spinner mGenderSpinner;

    private int mGender = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mNameEditText = findViewById(R.id.edit_profile_name);
        mAgeEditText = findViewById(R.id.edit_profile_age);
        mHeightEditText = findViewById(R.id.edit_profile_height);
        mWeightEditText = findViewById(R.id.edit_profile_weight);
        mGenderSpinner = findViewById(R.id.spinner_gender);
        setupSpinner();

        // mDbHelper = new ProfileDbHelper(this);
    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = ProfileContract.ProfileEntry.GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = ProfileContract.ProfileEntry.GENDER_FEMALE;
                    } else {
                        mGender = ProfileContract.ProfileEntry.GENDER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save :
                // Save
                insertProfile();
                // Exit
                finish();
                return true;
            case R.id.action_delete :
                //nothing
                return true;
            case android.R.id.home :
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertProfile() {
        String nameString = mNameEditText.getText().toString().trim();
        int ageInteger = Integer.parseInt(mAgeEditText.getText().toString().trim());
        int heightInteger = Integer.parseInt(mHeightEditText.getText().toString().trim());
        int weightInteger = Integer.parseInt(mWeightEditText.getText().toString().trim());

        ContentValues values = new ContentValues();
        // SQLiteDatabase db = mDbHelper.getWritableDatabase();

        values.put(ProfileContract.ProfileEntry.NAME, nameString);
        values.put(ProfileContract.ProfileEntry.AGE, ageInteger);
        values.put(ProfileContract.ProfileEntry.GENDER, mGender);
        values.put(ProfileContract.ProfileEntry.HEIGHT, heightInteger);
        values.put(ProfileContract.ProfileEntry.WEIGHT, weightInteger);

        // long newRowId = db.insert(ProfileContract.ProfileEntry.TABLE_NAME, null, values);

        Uri newRowUri = getContentResolver().insert(ProfileContract.ProfileEntry.CONTENT_URI, values);

        if (newRowUri == null) {
            Toast.makeText(this, "Error saving current profile", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Profile saved", Toast.LENGTH_LONG).show();
        }

    }
}
