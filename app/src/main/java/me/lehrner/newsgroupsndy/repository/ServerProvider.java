/*
 * Copyright (C) 2016 Daniel Lehrner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.lehrner.newsgroupsndy.repository;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.lehrner.newsgroupsndy.model.ServerContract;
import me.lehrner.newsgroupsndy.model.DbHelper;

public class ServerProvider extends ContentProvider {
    private DbHelper mDbHelper;
    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDbHelper = new DbHelper(mContext);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor = mDbHelper.getReadableDatabase().query(
                ServerContract.ServerEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        cursor.setNotificationUri(mContext.getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return ServerContract.ServerEntry.CONTENT_TYPE;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long id = mDbHelper.getWritableDatabase().insert(
                ServerContract.ServerEntry.TABLE_NAME,
                null,
                values);

        Uri newRowUri = null;

        // if insert was successful
        if (id != -1) {
            newRowUri = ContentUris.withAppendedId(uri, id);
            mContext.getContentResolver().notifyChange(newRowUri, null);
        }

        return newRowUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numberOfDeletedRows = mDbHelper.getWritableDatabase().delete(
                ServerContract.ServerEntry.TABLE_NAME,
                selection,
                selectionArgs);

        if (numberOfDeletedRows > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }

        return numberOfDeletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
        int numberOfUpdatedRows = mDbHelper.getWritableDatabase().update(
                ServerContract.ServerEntry.TABLE_NAME,
                values,
                whereClause,
                whereArgs
        );

        if (numberOfUpdatedRows > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }

        return numberOfUpdatedRows;
    }
}
