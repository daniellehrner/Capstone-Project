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

package me.lehrner.newsgroupsndy.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.lehrner.newsgroupsndy.model.ServerContract.ServerEntry;

public class ServerDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Server.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_SERVERS =
            "CREATE TABLE " + ServerEntry.TABLE_NAME + " (" +
                    ServerEntry._ID + " INTEGER PRIMARY KEY," +
                    ServerEntry.COLUMN_NAME_SERVER_NAME + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.COLUMN_NAME_SERVER_URL + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.COLUMN_NAME_SERVER_USER + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.COLUMN_NAME_SERVER_MAIL + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ServerEntry.TABLE_NAME;

    public ServerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SERVERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
