package com.example.lno007.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lno007.data.ProfileContract.ProfileEntry;

public class ProfileProvider extends ContentProvider {

    public static final String LOG_TAG = ProfileProvider.class.getSimpleName();

    private static final int PROFILES = 107;
    private static final int PROFILE_ID = 207;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ProfileEntry.CONTENT_AUTHORITY, ProfileEntry.PATH_PROFILES, PROFILES);

        sUriMatcher.addURI(ProfileEntry.CONTENT_AUTHORITY, ProfileEntry.PATH_PROFILES + "/#", PROFILE_ID);

    }

    private ProfileDbHelper mDbhelper;

    @Override
    public boolean onCreate() {
        mDbhelper = new ProfileDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mDbhelper.getReadableDatabase();

        Cursor cursor;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PROFILES :
                cursor = db.query(ProfileEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PROFILE_ID :
                selection = ProfileEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = db.query(ProfileEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Wtf is this uri ? uri : " + uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PROFILES:
                return ProfileEntry.CONTENT_LIST_TYPES;
            case PROFILE_ID:
                return  ProfileEntry.CONTENT_ITEM_TYPES;
            default:
                throw new IllegalArgumentException("unknown uri: " + uri + ", with match: " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        assert values != null;
        final int match = sUriMatcher.match(uri);
        if (match == PROFILES) {
            return insertProfile(uri, values);
        }
        throw new IllegalArgumentException("Wtf id this uri for insert? uri: " + uri);
    }

    private Uri insertProfile(Uri uri, ContentValues values) {

        String name = values.getAsString(ProfileEntry.NAME);
        if (name == null) {
            throw new IllegalArgumentException("Profile requires name");
        }

        Integer age = values.getAsInteger(ProfileEntry.AGE);
        if (age == null || age < 0 || age > 120) {
            throw new IllegalArgumentException("Profile requires proper age");
        }

        Integer gender = values.getAsInteger(ProfileEntry.GENDER);
        if (gender == null || ProfileEntry.idGenderValid(gender)) {
            throw new IllegalArgumentException("Profile requires gender");
        }

        Integer height = values.getAsInteger(ProfileEntry.HEIGHT);
        if (height == null || height < 55 || height > 272) {
            throw new IllegalArgumentException("Profile requires proper height");
        }

        Integer weight = values.getAsInteger(ProfileEntry.WEIGHT);
        if (weight == null || weight < 1 || weight > 635) {
            throw new IllegalArgumentException("Profiles needs proper weight");
        }

        SQLiteDatabase db = mDbhelper.getReadableDatabase();

        long newRowId = db.insert(ProfileEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Log.e(LOG_TAG, "Insertion failed!! uri: " + uri);
            return null;
        } else {
            return ContentUris.withAppendedId(ProfileEntry.CONTENT_URI, newRowId);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbhelper.getWritableDatabase();

        switch (match) {
            case PROFILES :
                return db.delete(ProfileEntry.TABLE_NAME, selection, selectionArgs);
            case PROFILE_ID:
                selection = ProfileEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return db.delete(ProfileEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Delete failed! uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        assert values != null;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PROFILES:
                return updatePet(uri, values, selection, selectionArgs);
            case PROFILE_ID:
                selection = ProfileEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Wtf uri? for update, uri: " + uri);
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ProfileEntry.NAME)) {
            String name = values.getAsString(ProfileEntry.NAME);
            if (name == null) {
                throw new IllegalArgumentException("Profile update requires a name");
            }
        }
        if (values.containsKey(ProfileEntry.AGE)) {
            Integer age = values.getAsInteger(ProfileEntry.AGE);
            if (age == null || age < 0 || age > 120) {
                throw new IllegalArgumentException("Profile update requires proper age");
            }
        }
        if (values.containsKey(ProfileEntry.GENDER)) {
            Integer gender = values.getAsInteger(ProfileEntry.GENDER);
            if (gender == null || ProfileEntry.idGenderValid(gender)) {
                throw new IllegalArgumentException("Profile update requires gender");
            }
        }
        if (values.containsKey(ProfileEntry.HEIGHT)) {
            Integer height = values.getAsInteger(ProfileEntry.HEIGHT);
            if (height == null || height < 55 || height > 272) {
                throw new IllegalArgumentException("Profile update requires proper height");
            }
        }
        if (values.containsKey(ProfileEntry.WEIGHT)) {
            Integer weight = values.getAsInteger(ProfileEntry.WEIGHT);
            if (weight == null || weight < 1 || weight > 635) {
                throw new IllegalArgumentException("Profiles update needs proper weight");
            }
        }
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mDbhelper.getWritableDatabase();

        return db.update(ProfileEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
