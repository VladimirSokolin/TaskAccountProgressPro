package com.mycompany.organaiser;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NoterContentProvider extends ContentProvider {
    MyDatabaseOpenHelper dataHelper;
    static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI("com.mycompany.organaiser.provider.NoterContentProvider", "tableOfTimeSpaces", 1);
        matcher.addURI("com.mycompany.organaiser.provider.NoterContentProvider", "tableOfTimeSpaces/*", 2);
    }
    @Override
    public boolean onCreate() {
        dataHelper = new MyDatabaseOpenHelper(getContext());
        //DaoDayRouting daoDayRouting = new DaoDayRouting(dataHelper);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
            SQLiteDatabase db = dataHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM tableOfTimeSpaces", null);
            Log.d("TAG", "query: " + cursor.getCount());
            return cursor;

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
